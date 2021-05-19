package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.types.clock.IClock;
import jadex.commons.future.IFuture;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class UnitStepDiscreteTimeModel extends AbstractJadexTimeModel {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UnitStepDiscreteTimeModel.class);

    protected static final long DELTA = 1L;
    protected TimeAdvanceFunction timeAdvanceFunction;

    protected JadexTimestamp referenceTime;

    protected double referenceClockTick;
    protected double startClockTick;
    protected double endClockTick;
    protected double ticksTillEnd;

    protected double nowClockTick;
    protected JadexTimestamp nowStamp;
    protected Duration dateModifier;

    public UnitStepDiscreteTimeModel() {
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                getTimeAdvanceFunction()
        );
    }

    public void setTimeAdvanceFunction(TimeAdvanceFunction timeAdvanceFunction) {
        this.timeAdvanceFunction = timeAdvanceFunction;
    }

    public TimeAdvanceFunction getTimeAdvanceFunction() {
        return timeAdvanceFunction;
    }

    @Override
    public void setupTimeModel() {
        LOGGER.trace(IRPSection.GENERAL, "setup clock '{}'", IClock.TYPE_EVENT_DRIVEN);
        getSimulationService().setClockType(IClock.TYPE_EVENT_DRIVEN).get();

        LOGGER.trace(IRPSection.GENERAL, "tick delta: {}", DELTA);
        getClockService().setDelta(DELTA);

        ZonedDateTime startDate = TimeUtil.startOfYear(getFirstSimulationYear());
        ZonedDateTime initDate = timeAdvanceFunction.moveBackwards(startDate, 1);
        ZonedDateTime endDate = startDate.plusYears(getNumberOfSimulationYears());

        referenceClockTick = getClockTick();
        startClockTick = referenceClockTick + 1;
        referenceTime = new BasicTimestamp(initDate, referenceClockTick, 0);
        startTime = new BasicTimestamp(startDate, startClockTick, 1);

        ticksTillEnd = timeAdvanceFunction.calculateDelta(initDate, endDate);
        endClockTick = referenceClockTick + ticksTillEnd;
        endTime = new BasicTimestamp(endDate, endClockTick, ticksTillEnd);

        currentYearForValidation = getFirstSimulationYear();

        LOGGER.trace(IRPSection.GENERAL, "current simulation year: {}", currentYearForValidation);
        LOGGER.trace(IRPSection.GENERAL, "simulation init time: {}", referenceTime.printComplex());
        LOGGER.trace(IRPSection.GENERAL, "simulation start time: {}", startTime().printComplex());
        LOGGER.trace(IRPSection.GENERAL, "simulation end time: {}", endTime().printComplex());
        LOGGER.trace(IRPSection.GENERAL, "init clock tick: {}", referenceClockTick);
        LOGGER.trace(IRPSection.GENERAL, "start clock tick: {}", startClockTick);
        LOGGER.trace(IRPSection.GENERAL, "end clock tick: {}", endClockTick);
        LOGGER.trace(IRPSection.GENERAL, "ticks till end: {}", ticksTillEnd);
        LOGGER.trace(IRPSection.GENERAL, "current simulation time: {}", now().printComplex());
    }

    @Override
    public boolean hasYearChange() {
        int currentYear = getCurrentYear();
        return currentYear != currentYearForValidation
                && currentYear >= getFirstSimulationYear();
    }

    @Override
    public void performYearChange() {
        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "perform year change");

        ZonedDateTime nowDate = unmodifiedNowDate();
        ZonedDateTime modified = modify(nowDate);
        ZonedDateTime startThisYear = TimeUtil.startOfYear(modified.getYear());

        Duration currentModifier = Duration.between(modified, startThisYear);

        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "new partial date modifier: '{}'", currentModifier);
        updateModifier(currentModifier);

        resetNow();

        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "using new date modifer: {} -> {}", nowDate, modify(nowDate));
    }

    @Override
    public int getFirstSimulationYear() {
        return environment.getSettings()
                .getActualFirstSimulationYear();
    }

    @Override
    public int getLastSimulationYear() {
        return environment.getSettings()
                .getLastSimulationYear();
    }

    protected int getNumberOfSimulationYears() {
        return environment.getSettings()
                .getActualNumberOfSimulationYears();
    }

    @Override
    public IFuture<Void> waitUntil(IExecutionFeature exec, JadexTimestamp ts, IInternalAccess access, IComponentStep<Void> task) {
        if(isValid(ts)) {
            JadexTimestamp now = now();
            long delay = timeAdvanceFunction.calculateDelta(now.getTime(), ts.getTime());
            return uncheckedWait(exec, delay, access, task);
        } else {
            return JadexUtil.END_TIME_REACHED;
        }
    }

    @Override
    public IFuture<Void> waitUntilEnd(IExecutionFeature exec, IInternalAccess access, IComponentStep<Void> task) {
        JadexTimestamp now = now();
        long delay = timeAdvanceFunction.calculateDelta(now.getTime(), endTime.getTime());
        return uncheckedWait(exec, delay, access, task);
    }

    @Override
    public IFuture<Void> wait(IExecutionFeature exec, long delay, IInternalAccess access, IComponentStep<Void> task) {
        if(isValid(delay)) {
            return uncheckedWait(exec, delay, access, task);
        } else {
            return JadexUtil.END_TIME_REACHED;
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
        long tick = timeAdvanceFunction.calculateDelta(referenceTime.getTime(), zdt);
        double clockTick = referenceClockTick + tick;
        return new BasicTimestamp(zdt, clockTick, tick);
    }

    @Override
    public JadexTimestamp now() {
        double clockTick = getClockTick();
        if(nowStamp != null && nowClockTick == clockTick) {
            return nowStamp;
        }
        return syncNow(clockTick);
    }

    protected void resetNow() {
        nowStamp = null;
    }

    protected ZonedDateTime unmodifiedNowDate() {
        return unmodifiedNowDate(getClockTick());
    }

    protected ZonedDateTime unmodifiedNowDate(double clockTick) {
        double tick = clockTick - referenceClockTick;
        return tickToTime(tick);
    }

    protected ZonedDateTime modify(ZonedDateTime date) {
        return dateModifier == null
                ? date
                : date.plus(dateModifier);
    }

    protected void updateModifier(Duration modifier) {
        if(!modifier.isZero()) {
            dateModifier = dateModifier == null
                    ? modifier
                    : dateModifier.plus(modifier);
        }
        if(dateModifier != null && dateModifier.isZero()) {
            dateModifier = null;
        }

        if(dateModifier == null) {
            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "updated date modifier is null");
        } else {
            LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "updated date modifier: '{}'", dateModifier);
        }
    }

    protected synchronized JadexTimestamp syncNow(double clockTick) {
        if(nowStamp != null && nowClockTick == clockTick) {
            return nowStamp;
        }
        nowClockTick = clockTick;
        double tick = clockTick - referenceClockTick;
        nowStamp = new BasicTimestamp(
                modify(tickToTime(tick)),
                clockTick,
                tick
        );
        return nowStamp;
    }

    protected double getClockTick() {
        return getClockService().getTick();
    }

    protected ZonedDateTime tickToTime(double tick) {
        return timeAdvanceFunction.moveForward(referenceTime.getTime(), (long) tick);
    }

    @Override
    public boolean isValid(long delay) {
        long delay0 = Math.max(delay, 0L);
        return getClockTick() + delay0 < endClockTick;
    }

    /**
     * @author Daniel Abitz
     */
    public interface TimeAdvanceFunction extends ChecksumComparable {

        ZonedDateTime moveForward(ZonedDateTime in, long delta);

        ZonedDateTime moveBackwards(ZonedDateTime in, long delta);

        long calculateDelta(ZonedDateTime startInclusive, ZonedDateTime endExclusive);
    }

    /**
     * @author Daniel Abitz
     */
    @SuppressWarnings("unused")
    public static class SimpleTimeAdvanceFunction implements TimeAdvanceFunction {

        protected long amountToAdd;
        protected ChronoUnit unit;

        public SimpleTimeAdvanceFunction() {
        }

        public SimpleTimeAdvanceFunction(long amountToAdd, ChronoUnit unit) {
            setAmountToAdd(amountToAdd);
            setUnit(unit);
        }

        @Override
        public int getChecksum() {
            return ChecksumComparable.getChecksum(
                    amountToAdd,
                    getName(unit)
            );
        }

        public static String getName(ChronoUnit unit) {
            return unit.name();
        }

        public static ChronoUnit forName(String name) {
            for(ChronoUnit unit: ChronoUnit.values()) {
                if(unit.name().equals(name)) {
                    return unit;
                }
            }
            throw new NoSuchElementException(name);
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
        public ZonedDateTime moveForward(ZonedDateTime in, long delta) {
            return delta == 0
                    ? in
                    : in.plus(getAmountToAdd() * delta, getUnit());
        }

        @Override
        public ZonedDateTime moveBackwards(ZonedDateTime in, long delta) {
            return delta == 0
                    ? in
                    : in.minus(getAmountToAdd() * delta, getUnit());
        }

        protected long between(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            return getUnit().between(startInclusive, endExclusive);
        }

        @Override
        public long calculateDelta(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            return between(startInclusive, endExclusive);
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static class CeilingTimeAdvanceFunction extends SimpleTimeAdvanceFunction {

        public CeilingTimeAdvanceFunction() {
            super();
        }

        public CeilingTimeAdvanceFunction(long amountToAdd, ChronoUnit unit) {
            super(amountToAdd, unit);
        }

        @Override
        public int getChecksum() {
            return ChecksumComparable.getChecksum(
                    amountToAdd,
                    getName(unit)
            );
        }

        protected long ceilingBetween(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            long delta = between(startInclusive, endExclusive);
            ZonedDateTime startWithDelta = moveForward(startInclusive, delta);
            return endExclusive.equals(startWithDelta)
                    ? delta
                    : delta + 1;
        }

        protected long calculateToStartOfNextYear(ZonedDateTime start) {
            int year = start.getYear();
            ZonedDateTime end = TimeUtil.startOfYear(year + 1);
            return ceilingBetween(start, end);
        }

        protected long calculateFromStartOfSameYear(ZonedDateTime end) {
            int year = end.getYear();
            ZonedDateTime start = TimeUtil.startOfYear(year);
            return ceilingBetween(start, end);
        }

        @Override
        public long calculateDelta(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            int startYear = startInclusive.getYear();
            int endYear = endExclusive.getYear();

            if(startYear == endYear) {
                return ceilingBetween(startInclusive, endExclusive);
            }

            long firstPart = calculateToStartOfNextYear(startInclusive);
            long lastPart = calculateFromStartOfSameYear(endExclusive);

            if(startYear + 1 == endYear) {
                return firstPart + lastPart;
            }

            long partSum = 0L;
            for(int y = startYear + 1; y < endYear; y++) {
                ZonedDateTime from = TimeUtil.startOfYear(y);
                ZonedDateTime to = TimeUtil.startOfYear(y + 1);
                partSum += ceilingBetween(from, to);
            }

            return firstPart + partSum + lastPart;
        }
    }
}
