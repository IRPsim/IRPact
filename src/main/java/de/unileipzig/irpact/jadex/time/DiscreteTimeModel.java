package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.TickConverter;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.types.clock.IClock;
import jadex.commons.future.IFuture;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public class DiscreteTimeModel extends AbstractJadexTimeModel {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DiscreteTimeModel.class);

    protected final TickConverter converter = new TickConverter();
    protected long storedDelta;
    protected long storedTimePerTickInMs;

    protected double startTick;
    protected double endTick;
    protected double nowTick;
    protected double tickModifier;
    protected JadexTimestamp nowStamp;
    protected double delayTillEnd;

    public DiscreteTimeModel() {
    }

    public void setStoredDelta(long storedDelta) {
        this.storedDelta = storedDelta;
    }

    public long getStoredDelta() {
        return storedDelta;
    }

    public void setStoredTimePerTickInMs(long storedTimePerTickInMs) {
        this.storedTimePerTickInMs = storedTimePerTickInMs;
    }

    public long getStoredTimePerTickInMs() {
        return storedTimePerTickInMs;
    }

    public void setStartYear(int startYear, long timePerTickInMs) {
        LOGGER.debug("set start year: {}", startYear);
        LOGGER.debug("set time per tick in ms: {}", timePerTickInMs);
        converter.init(startYear, timePerTickInMs, ZoneId.systemDefault());
    }

    public void setStartTick(double tick) {
        converter.setStart(tick);
        startTick = tick;
        setStartTime(new BasicTimestamp(converter.getStartTime(), tick, 0.0));
    }

    public double getTickModifier() {
        return tickModifier;
    }

    public void setTickModifier(double tickModifier) {
        this.tickModifier = tickModifier;
    }

    public JadexTimestamp getNowStamp() {
        return nowStamp;
    }

    public void setNowStamp(JadexTimestamp nowStamp) {
        this.nowStamp = nowStamp;
    }

    public TickConverter getConverter() {
        return converter;
    }

    protected double normalizeTick(double tick) {
        return tick - startTick;
    }

    @Override
    public void setupTimeModel() {
        LOGGER.debug("setup clock '{}'", IClock.TYPE_EVENT_DRIVEN);
        getSimulationService().setClockType(IClock.TYPE_EVENT_DRIVEN).get();
        LOGGER.debug("set tick delta: {}", getStoredDelta());
        getClockService().setDelta(getStoredDelta());

        setupNextYear();
    }

    @Override
    public void setupNextYear() {
        setStartYear(
                getStartYear(),
                getStoredTimePerTickInMs()
        );
        setStartTick(getModifiedClockTick());
        LOGGER.debug("internal simulation start time: {}", startTime());

        //endjahr ist inklusiv, hier wird aber exklusiv betrachtet!
        int yearDelta = getEndYearInclusive() - getStartYear();
        JadexTimestamp endStamp = plusYears(startTime(), yearDelta + 1L);
        setEndTime(endStamp);

        LOGGER.debug("internal simulation end time: {}", endTime());
        LOGGER.debug("end tick: {}", endTick);
        LOGGER.debug("end delay (ticks): {}", delayTillEnd);

        LOGGER.info("decrement tick for init");
        LOGGER.debug("old 'now': {}", now());
        decTickModifier();
        LOGGER.debug("new 'now': {}", now());
    }

    @Override
    public int getStartYear() {
        return environment.getInitializationData().getStartYear();
    }

    @Override
    public int getEndYearInclusive() {
        int start = getStartYear();
        int end = environment.getInitializationData().getEndYear();
        return Math.max(start, end);
    }

    @Override
    public void setEndTime(JadexTimestamp endTime) {
        super.setEndTime(endTime);
        delayTillEnd = converter.ticksUntil(startTime.getTime(), endTime.getTime());
        endTick = startTick + delayTillEnd;
    }

    @Override
    public IFuture<Void> waitUntil(IExecutionFeature exec, JadexTimestamp ts, IInternalAccess access, IComponentStep<Void> task) {
        if(isValid(ts)) {
            JadexTimestamp now = now();
            long delay = (long) converter.ticksUntil(now.getTime(), ts.getTime());
            return uncheckedWait(exec, delay, access, task);
        } else {
            return IFuture.DONE;
        }
    }

    @Override
    public IFuture<Void> wait(IExecutionFeature exec, long delay, IInternalAccess access, IComponentStep<Void> task) {
        if(isValid(delay)) {
            return uncheckedWait(exec, delay, access, task);
        } else {
            return IFuture.DONE;
        }
    }

    @Override
    public IFuture<Void> uncheckedWait(IExecutionFeature exec, long delay, IInternalAccess access, IComponentStep<Void> task) {
        if(delay == 0L) {
            return task.execute(access);
        }
        if(delay == 1L) {
            return exec.waitForTick(task);
        }
        return exec.waitForDelay(delay - 1L, ia -> exec.waitForTick(task));
    }

    @Override
    public IFuture<Void> scheduleImmediately(IExecutionFeature exec, IInternalAccess access, IComponentStep<Void> task) {
        return uncheckedWait(exec, 1L, access, task);
    }

    @Override
    public TimeMode getMode() {
        return TimeMode.DISCRETE;
    }

    @Override
    public JadexTimestamp convert(ZonedDateTime zdt) {
        double tick = converter.timeToTick(zdt);
        return new BasicTimestamp(zdt, tick, normalizeTick(tick));
    }

    protected double getModifiedClockTick() {
        return getClockService().getTick() + tickModifier;
    }

    protected void incTickModifier() {
        tickModifier++;
    }

    protected void decTickModifier() {
        tickModifier--;
    }

    @Override
    public JadexTimestamp now() {
        double nt = getModifiedClockTick();
        if(nowStamp != null && nowTick == nt) {
            return nowStamp;
        }
        return syncNow(nt);
    }

    protected synchronized JadexTimestamp syncNow(double nt) {
        if(nowStamp != null && nowTick == nt) {
            return nowStamp;
        }
        nowTick = nt;
        ZonedDateTime nowZdt = converter.tickToTime(nt);
        nowStamp = new BasicTimestamp(nowZdt, nt, normalizeTick(nt));
        return nowStamp;
    }

    @Override
    public boolean isValid(long delay) {
        delay = Math.max(delay, 0L);
        return getModifiedClockTick() + delay < endTick;
    }

    @Override
    public boolean isValid(Timestamp ts) {
        if(startTime == null || endTime == null || ts == null) {
            return false;
        }
        return ts.isAfterOrEquals(startTime) && ts.isBeforeOrEqual(endTime);
    }
}
