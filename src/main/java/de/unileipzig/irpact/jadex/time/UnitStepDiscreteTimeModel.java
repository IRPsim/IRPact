package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.core.simulation.tasks.Task;
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

    protected static final ZoneId ZONE = ZoneId.systemDefault();
    protected static final long DELTA = 1L;
    protected TimeAdvanceFunction timeAdvanceFunction;

    protected double clockStartTick;
    protected double clockEndTick;
    protected double ticksTillEnd;

    protected double tickModifier = 0.0;
    protected double nowClockTick;
    protected JadexTimestamp nowStamp;
    protected Duration dateModifier = null;

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
        LOGGER.trace(IRPSection.GENERAL, "setup clock '{}'", IClock.TYPE_EVENT_DRIVEN);
        getSimulationService().setClockType(IClock.TYPE_EVENT_DRIVEN).get();

        LOGGER.trace(IRPSection.GENERAL, "tick delta: {}", DELTA);
        getClockService().setDelta(DELTA);

        clockStartTick = getClockTick();
        ZonedDateTime startDate = LocalDate.of(getFirstSimulationYear(), Month.JANUARY, 1).atStartOfDay(ZONE);
        startTime = new BasicTimestamp(startDate, clockStartTick, 0);

        endTime = plusYears(startTime(), getNumberOfSimulationYears());
        clockEndTick = endTime.getClockTick();
        ticksTillEnd = endTime.getTick();

        currentYearForValidation = getFirstSimulationYear();

        LOGGER.trace(IRPSection.GENERAL, "simulation start time: {}", startTime().printComplex());
        LOGGER.trace(IRPSection.GENERAL, "simulation end time: {}", endTime().printComplex());
        LOGGER.trace(IRPSection.GENERAL, "start clock tick: {}", clockEndTick);
        LOGGER.trace(IRPSection.GENERAL, "end clock tick: {}", clockEndTick);
        LOGGER.trace(IRPSection.GENERAL, "ticks till end: {}", ticksTillEnd);

        LOGGER.trace(IRPSection.GENERAL, "modify tick for upcoming simulation year");
        LOGGER.trace(IRPSection.GENERAL, "unmodified current time: {}", now().printComplex());
        decTickModifier();
        LOGGER.trace(IRPSection.GENERAL, "modified current time: {}", now().printComplex());
    }

    @Override
    public boolean hasYearChange() {
        return getCurrentYear() != currentYearForValidation;
    }

    @Override
    public void performYearChange() {
        Timestamp now = now();
        ZonedDateTime nowTime = now.getTime();

        ZonedDateTime currentYearStartDate = LocalDate.of(now.getYear(), Month.JANUARY, 1).atStartOfDay(ZONE);

        if(nowTime.equals(currentYearStartDate)) {
            dateModifier = null;
        } else {
            dateModifier = Duration.between(nowTime, currentYearStartDate);
        }

        currentYearForValidation = now.getYear();
        resetNow();
    }

    private SyncTask createNewYearTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public int priority() {
                return Task.MAX_PRIORITY;
            }

            @Override
            public void run() {
                //setupNextYear();
            }
        };
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
        double tick = clockStartTick + tickDelta;
        return new BasicTimestamp(zdt, tick, tickDelta);
    }

    protected void resetNow() {
        nowStamp = null;
        now();
    }

    @Override
    public JadexTimestamp now() {
        double modifiedClockTick = getModifiedClockTick();
        if(nowStamp != null && nowClockTick == modifiedClockTick) {
            return nowStamp;
        }
        return syncNow(modifiedClockTick);
    }

    protected synchronized JadexTimestamp syncNow(double modifiedClockTick) {
        if(nowStamp != null && nowClockTick == modifiedClockTick) {
            return nowStamp;
        }
        nowClockTick = modifiedClockTick;
        double modifiedTick = modifiedClockTick - clockStartTick;
        ZonedDateTime nowData = tickToTime(modifiedTick);
        if(dateModifier != null) {
            nowData = nowData.plus(dateModifier);
        }
        nowStamp = new BasicTimestamp(nowData, modifiedClockTick, modifiedTick);
        return nowStamp;
    }

    protected double getClockTick() {
        return getClockService().getTick();
    }

    protected double getModifiedClockTick() {
        return getClockTick() + tickModifier;
    }

    protected void incTickModifier() {
        tickModifier++;
    }

    protected void decTickModifier() {
        tickModifier--;
    }
    protected ZonedDateTime tickToTime(double tick) {
        return timeAdvanceFunction.moveOn(startTime.getTime(), (long) tick);
    }

    @Override
    public boolean isValid(long delay) {
        long delay0 = Math.max(delay, 0L);
        return getModifiedClockTick() + delay0 < clockEndTick;
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
        public ZonedDateTime moveOn(ZonedDateTime in, long delta) {
            return in.plus(getAmountToAdd() * delta, getUnit());
        }

        @Override
        public long calculateDelta(ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
            return getUnit().between(startInclusive, endExclusive);
        }
    }
}
