package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.types.clock.IClock;
import jadex.commons.future.IFuture;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Daniel Abitz
 */
public class UnitStepDiscreteTimeModel extends AbstractJadexTimeModel {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UnitStepDiscreteTimeModel.class);

    protected static final ZoneId ZONE = ZoneId.systemDefault();
    protected static final long DELTA = 1L;
    protected TimeAdvanceFunction timeAdvanceFunction;

    protected double unmodifiedStartTick;
    protected double endTick;
    protected double nowTick;
    protected double tickModifier = 0.0;
    protected JadexTimestamp nowStamp;
    protected double deltaTillEnd;

    public UnitStepDiscreteTimeModel() {
    }

    public void setTimeAdvanceFunction(TimeAdvanceFunction timeAdvanceFunction) {
        this.timeAdvanceFunction = timeAdvanceFunction;
    }

    public TimeAdvanceFunction getTimeAdvanceFunction() {
        return timeAdvanceFunction;
    }

    @Override
    public void setupTimeModel() {
        LOGGER.debug("setup clock '{}'", IClock.TYPE_EVENT_DRIVEN);
        getSimulationService().setClockType(IClock.TYPE_EVENT_DRIVEN).get();
        LOGGER.debug("set tick delta: {}", DELTA);
        getClockService().setDelta(DELTA);

        setupNextYear();
    }

    @Override
    public void setupNextYear() {
        setStartTick(getModifiedClockTick());
        LOGGER.debug("internal simulation start time: {}", startTime());

        //endjahr ist inklusiv, hier wird aber exklusiv betrachtet!
        long yearDelta = getEndYearInclusive() - getStartYear();
        JadexTimestamp endTime = plusYears(startTime(), yearDelta + 1L);
        setEndTime(endTime);
        LOGGER.debug("internal simulation end time: {}", endTime());
        LOGGER.debug("end tick: {}", endTick);
        LOGGER.debug("end delta (ticks): {}", deltaTillEnd);

        LOGGER.info("decrement tick for init");
        LOGGER.debug("old 'now': {}", now());
        decTickModifier();
        LOGGER.debug("new 'now': {}", now());
    }

    public void setStartTick(double tick) {
        unmodifiedStartTick = tick;
        ZonedDateTime startZdt = LocalDate.of(getStartYear(), Month.JANUARY, 1).atStartOfDay(ZONE);
        startTime = new BasicTimestamp(startZdt, tick, 0);
    }

    @Override
    public int getStartYear() {
        return environment.getInitializationData().getStartYear();
    }

    @Override
    public void setEndTime(JadexTimestamp endTime) {
        super.setEndTime(endTime);
        endTick = endTime.getSimulationTick();
        deltaTillEnd = endTime.getNormalizedTick();
    }

    @Override
    public int getEndYearInclusive() {
        int start = getStartYear();
        int end = environment.getInitializationData().getEndYear();
        return Math.max(start, end);
    }

    @Override
    public IFuture<Void> waitUntil(IExecutionFeature exec, JadexTimestamp ts, IInternalAccess access, IComponentStep<Void> task) {
        if(isValid(ts)) {
            JadexTimestamp now = now();
            long delay = timeAdvanceFunction.calculateDelta(now.getTime(), ts.getTime());
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
        long tickDelta = timeAdvanceFunction.calculateDelta(startTime().getTime(), zdt);
        double tick = unmodifiedStartTick + tickDelta;
        return new BasicTimestamp(zdt, tick, tickDelta);
    }

    @Override
    public JadexTimestamp now() {
        double tick = getModifiedClockTick();
        if(nowStamp != null && nowTick == tick) {
            return nowStamp;
        }
        return syncNow(tick);
    }

    protected double getModifiedClockTick() {
        return getModifiedTick(getClockService().getTick());
    }

    protected double getModifiedTick(double tick) {
        return tick + tickModifier;
    }

    protected void incTickModifier() {
        tickModifier++;
    }

    protected void decTickModifier() {
        tickModifier--;
    }

    protected synchronized JadexTimestamp syncNow(double modifiedTick) {
        if(nowStamp != null && nowTick == modifiedTick) {
            return nowStamp;
        }
        nowTick = modifiedTick;
        long modifiedTickDelta = (long) (modifiedTick - unmodifiedStartTick);
        ZonedDateTime startZdt = startTime.getTime();
        ZonedDateTime nowZdt = timeAdvanceFunction.moveOn(startZdt, modifiedTickDelta);
        nowStamp = new BasicTimestamp(nowZdt, modifiedTick, modifiedTickDelta);
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

    /**
     * @author Daniel Abitz
     */
    public interface TimeAdvanceFunction {

        ZonedDateTime moveOn(ZonedDateTime in, long delta);

        long calculateDelta(ZonedDateTime startInclusive, ZonedDateTime endExclusive);
    }

    /**
     * @author Daniel Abitz
     */
    public static class SimpleTimeAdvanceFunction implements TimeAdvanceFunction {

        protected long amountToAdd;
        protected ChronoUnit unit;

        public SimpleTimeAdvanceFunction() {
        }

        public SimpleTimeAdvanceFunction(long amountToAdd, ChronoUnit unit) {
            setAmountToAdd(amountToAdd);
            setUnit(unit);
        }

        public void setAmountToAdd(long amountToAdd) {
            this.amountToAdd = amountToAdd;
        }

        public long getAmountToAdd() {
            return amountToAdd;
        }

        public void setUnit(ChronoUnit unit) {
            this.unit = unit;
        }

        public void setMillis() {
            setUnit(ChronoUnit.MILLIS);
        }

        public void setSeconds() {
            setUnit(ChronoUnit.SECONDS);
        }

        public void setMinutes() {
            setUnit(ChronoUnit.MINUTES);
        }

        public void setHours() {
            setUnit(ChronoUnit.HOURS);
        }

        public void setDays() {
            setUnit(ChronoUnit.DAYS);
        }

        public void setWeeks() {
            setUnit(ChronoUnit.WEEKS);
        }

        public void setMonts() {
            setUnit(ChronoUnit.MONTHS);
        }

        public ChronoUnit getUnit() {
            return unit;
        }

        @Override
        public ZonedDateTime moveOn(ZonedDateTime in, long delta) {
            return in.plus(getAmountToAdd() * delta, getUnit());
        }

        @Override
        public long calculateDelta(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            return getUnit().between(startInclusive, endExclusive);
        }
    }
}
