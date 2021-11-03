package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.types.clock.IClock;
import jadex.commons.future.IFuture;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.function.LongConsumer;

/**
 * @author Daniel Abitz
 */
public class FixedUnitStepDiscreteTimeModel extends AbstractJadexTimeModel {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FixedUnitStepDiscreteTimeModel.class);

    protected static final long DELTA = 1L;
    protected TimeAdvanceFunction timeAdvanceFunction;

    protected JadexTimestamp referenceTime;
    protected JadexTimestamp annualReferenceTime;

    protected double referenceClockTick;
    protected double annualReferenceClockTick;
    protected double startClockTick;
    protected double endClockTick;
    protected double ticksTillEnd;
    protected double tickModifier = 0;

    protected boolean endTimeReached = false;

    protected double nowClockTick;
    protected JadexTimestamp nowStamp;

    public FixedUnitStepDiscreteTimeModel() {
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
        annualReferenceClockTick = referenceClockTick;
        startClockTick = referenceClockTick + 1;
        referenceTime = new BasicTimestamp(initDate, referenceClockTick, 0);
        annualReferenceTime = referenceTime;
        startTime = new BasicTimestamp(startDate, startClockTick, 1);

        ticksTillEnd = calculateSteps(initDate, endDate);
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

    protected long calculateSteps(ZonedDateTime start, ZonedDateTime end) {
        long steps = 0;
        int currentYear = start.getYear();
        ZonedDateTime next = start;
        while(next.isBefore(end) && !next.isEqual(end)) {
            next = timeAdvanceFunction.moveForward(next, 1);
            int year = next.getYear();

            if(currentYear != year) {
                next = LocalDate.of(year, 1, 1).atStartOfDay(next.getZone());
                currentYear = year;
            }

            steps++;
        }
        return steps;
    }

    @Override
    public boolean hasYearChange() {
        return isYearChange(getCurrentYear());
    }

    protected boolean isYearChange(JadexTimestamp stamp) {
        return isYearChange(stamp.getYear());
    }

    protected boolean isYearChange(int year) {
        return year != currentYearForValidation
                && year >= getFirstSimulationYear();
    }

    @Override
    public void performYearChange(LongConsumer lastYearTasks, LongConsumer newYearTasks) {
        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "perform year change");

        JadexTimestamp firstStepInNewYear = now();
        if(!isYearChange(firstStepInNewYear)) {
            LOGGER.warn("no year change ({} <> {}), cancel 'performYearChange'", firstStepInNewYear.getYear(), currentYearForValidation);
            return;
        }

        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "starting the time machine...");

        resetNow();
        tickModifier--;
        JadexTimestamp lastStepInOldYear = now();
        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "to the past: {} -> {}", firstStepInNewYear, lastStepInOldYear);

        LOGGER.trace("execute old year tasks");
        lastYearTasks.accept(currentYearForValidation);

        resetNow();
        tickModifier++;
        advanceYear(firstStepInNewYear);
        JadexTimestamp realFirstStepInNewYear = now();
        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "back to the (modified) future: {} -> {}", lastStepInOldYear, realFirstStepInNewYear);

        currentYearForValidation = realFirstStepInNewYear.getYear();
        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "set currentYearForValidation = {}", currentYearForValidation);

        LOGGER.trace("execute new year tasks");
        newYearTasks.accept(currentYearForValidation);
    }

    protected void advanceYear(JadexTimestamp firstStampInNewYear) {
        annualReferenceTime = createActualFirstStamp(firstStampInNewYear);
        annualReferenceClockTick = annualReferenceTime.getClockTick();
    }

    protected JadexTimestamp createActualFirstStamp(JadexTimestamp firstStampInNewYear) {
        ZonedDateTime realNewStart = TimeUtil.startOfYear(firstStampInNewYear.getYear());
        double clockTick = firstStampInNewYear.getClockTick();
        double tick = firstStampInNewYear.getTick();
        return new BasicTimestamp(realNewStart, clockTick, tick);
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
            long delay = calculateSteps(now.getTime(), ts.getTime());
            return uncheckedWait(exec, delay, access, task);
        } else {
            return JadexUtil.END_TIME_REACHED;
        }
    }

    @Override
    public IFuture<Void> waitUntilEnd(IExecutionFeature exec, IInternalAccess access, IComponentStep<Void> task) {
        JadexTimestamp now = now();
        long delay = calculateSteps(now.getTime(), endTime.getTime());
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
        long tick = calculateSteps(referenceTime.getTime(), zdt);
        double clockTick = referenceClockTick + tick;
        return new BasicTimestamp(zdt, clockTick, tick);
    }

    @Override
    public JadexTimestamp now() {
        double clockTick = getClockTick();
        JadexTimestamp nowStamp = this.nowStamp;
        if(nowStamp != null && nowClockTick == clockTick) {
            return nowStamp;
        }
        return syncNow(clockTick);
    }

    protected void resetNow() {
        nowStamp = null;
    }

    protected synchronized JadexTimestamp syncNow(double clockTick) {
        JadexTimestamp nowStamp = this.nowStamp;
        if(nowStamp != null && nowClockTick == clockTick) {
            return nowStamp;
        }

        this.nowClockTick = clockTick;
        double tick = clockTick - referenceClockTick + tickModifier;
        double annualTick = clockTick - annualReferenceClockTick + tickModifier;
        JadexTimestamp newNowStamp = new BasicTimestamp(
                timeAdvanceFunction.moveForward(annualReferenceTime.getTime(), (long) annualTick),
                clockTick,
                tick
        );

        this.nowStamp = newNowStamp;
        return newNowStamp;
    }

    protected double getClockTick() {
        return getClockService().getTick();
    }

    @Override
    public boolean isValid(long delay) {
        long delay0 = Math.max(delay, 0L);
        return getClockTick() + delay0 < endClockTick;
    }

    @Override
    public boolean endTimeReached() {
        if(endTimeReached) {
            return true;
        } else {
            endTimeReached = super.endTimeReached();
            return endTimeReached;
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface TimeAdvanceFunction extends ChecksumComparable {

        ZonedDateTime moveForward(ZonedDateTime in, long delta);

        ZonedDateTime moveBackwards(ZonedDateTime in, long delta);
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

        protected long stepsBetween(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            long between = getUnit().between(startInclusive, endExclusive);
            return between / getAmountToAdd();
        }

        public long calculateDelta(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            return stepsBetween(startInclusive, endExclusive);
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

        protected long ceilStepsBetween(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            long delta = stepsBetween(startInclusive, endExclusive);
            ZonedDateTime startWithDelta = moveForward(startInclusive, delta);
            return endExclusive.equals(startWithDelta)
                    ? delta
                    : delta + 1;
        }

        protected long calculateToStartOfNextYear(ZonedDateTime start) {
            int year = start.getYear();
            ZonedDateTime end = TimeUtil.startOfYear(year + 1);
            return ceilStepsBetween(start, end);
        }

        protected long calculateFromStartOfSameYear(ZonedDateTime end) {
            int year = end.getYear();
            ZonedDateTime start = TimeUtil.startOfYear(year);
            return ceilStepsBetween(start, end);
        }

        @Override
        public long calculateDelta(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            int startYear = startInclusive.getYear();
            int endYear = endExclusive.getYear();

            if(startYear == endYear) {
                return ceilStepsBetween(startInclusive, endExclusive);
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
                partSum += ceilStepsBetween(from, to);
            }

            return firstPart + partSum + lastPart;
        }
    }
}
