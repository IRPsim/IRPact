package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.TickConverter;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.types.clock.IClock;
import jadex.commons.future.IFuture;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

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

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName()
        );
    }

    public double getStartTick() {
        return startTick;
    }

    public double getEndTick() {
        return endTick;
    }

    public double getNowTick() {
        return nowTick;
    }

    public double getDelayTillEnd() {
        return delayTillEnd;
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
        LOGGER.trace("set start year: {}", startYear);
        LOGGER.trace("set time per tick in ms: {}", timePerTickInMs);
        converter.init(startYear, timePerTickInMs, ZoneId.systemDefault());
    }

    public void setStartTick(double tick) {
        converter.setStart(tick);
        startTick = tick;
        this.startTime = new BasicTimestamp(converter.getStartTime(), tick, 0.0);
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
        LOGGER.trace("setup clock '{}'", IClock.TYPE_EVENT_DRIVEN);
        getSimulationService().setClockType(IClock.TYPE_EVENT_DRIVEN).get();
        LOGGER.trace("set tick delta: {}", getStoredDelta());
        getClockService().setDelta(getStoredDelta());

        setupNextYear();
    }

    public void setupNextYear() {
        setStartYear(
                getFirstSimulationYear(),
                getStoredTimePerTickInMs()
        );
        setStartTick(getModifiedClockTick());
        LOGGER.trace("internal simulation start time: {}", startTime());

        //endjahr ist inklusiv, hier wird aber exklusiv betrachtet!
        int yearDelta = getLastSimulationYear() - getFirstSimulationYear();
        JadexTimestamp endStamp = plusYears(startTime(), yearDelta + 1L);
        setEndTime(endStamp);

        LOGGER.trace("internal simulation end time: {}", endTime());
        LOGGER.trace("end tick: {}", endTick);
        LOGGER.trace("end delay (ticks): {}", delayTillEnd);

        LOGGER.info("decrement tick for init");
        LOGGER.trace("old 'now': {}", now());
        decTickModifier();
        LOGGER.trace("new 'now': {}", now());
    }

    @Override
    public int getFirstSimulationYear() {
        return environment.getSettings()
                .getFirstSimulationYear();
    }

    @Override
    public int getLastSimulationYear() {
        int start = getFirstSimulationYear();
        int end = environment.getSettings()
                .getLastSimulationYear();
        return Math.max(start, end);
    }

    public void setEndTime(JadexTimestamp endTime) {
        this.endTime = endTime;
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
    public IFuture<Void> waitUntilEnd(IExecutionFeature exec, IInternalAccess access, IComponentStep<Void> task) {
        throw new TodoException();
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
    public boolean hasYearChange() {
        throw new TodoException();
    }

    @Override
    public void performYearChange(Set<SyncTask> lastYearTasks, Set<SyncTask> newYearTasks) {
        throw new TodoException();
    }
}
