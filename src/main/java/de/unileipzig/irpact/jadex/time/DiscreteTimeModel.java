package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.TickConverter;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.misc.ValidationException;
import jadex.bridge.IComponentStep;
import jadex.bridge.component.IExecutionFeature;
import jadex.commons.future.IFuture;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public class DiscreteTimeModel extends AbstractJadexTimeModel {

    protected final TickConverter converter = new TickConverter();
    protected long storedDelta;
    protected long storedTimePerTickInMs;

    protected double nowTick;
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
        converter.init(startYear, timePerTickInMs, ZoneId.systemDefault());
    }

    public void setStartTick(double tick) {
        converter.setStart(tick);
        setStartTime(new BasicTimestamp(converter.getStartTime()));
    }

    public TickConverter getConverter() {
        return converter;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void validate() throws ValidationException {
    }

    @Override
    public void setEndTime(JadexTimestamp endTime) {
        super.setEndTime(endTime);
        delayTillEnd = converter.ticksUntil(startTime.getTime(), endTime.getTime());
    }

    protected IFuture<Void> recursiveWait(IExecutionFeature exec, double tsTick, IComponentStep<Void> task) {
        return exec.waitForTick(ia -> {
            if(clock.getTick() < tsTick) {
                return recursiveWait(exec, tsTick, task);
            } else {
                return task.execute(ia);
            }
        });
    }

    @Override
    public IFuture<Void> waitUntil(IExecutionFeature exec, JadexTimestamp ts, IComponentStep<Void> task) {
        if(isValid(ts)) {
            JadexTimestamp now = now();
            long delay = (long) converter.ticksUntil(now.getTime(), ts.getTime());
            return wait(exec, delay, task);
        } else {
            return IFuture.DONE;
        }
    }

    @Override
    public IFuture<Void> wait(IExecutionFeature exec, long delay, IComponentStep<Void> task) {
        if(isValid(delay)) {
            return exec.waitForDelay(delay, task);
        } else {
            return IFuture.DONE;
        }
    }

    @Override
    public IFuture<Void> forceWait(IExecutionFeature exec, long delay, IComponentStep<Void> task) {
        return exec.waitForDelay(delay, task);
    }

//    @Override
//    public IFuture<Void> waitUntil0(IExecutionFeature exec, JadexTimestamp ts, IComponentStep<Void> task) {
//        return waitUntil0(true, exec, ts, task);
//    }
//
//    @Override
//    public IFuture<Void> wait0(IExecutionFeature exec, long delayMs, IComponentStep<Void> task) {
//        return wait0(true, exec, delayMs, task);
//    }

//    protected IFuture<Void> waitUntil0(boolean noValidation, IExecutionFeature exec, JadexTimestamp ts, IComponentStep<Void> task) {
//        if(noValidation || isValid(ts)) {
//            final double tsTick = converter.timeToTick(ts.getTime());
//            final double nowTick = clock.getTick();
//            if(tsTick <= nowTick) {
//                return exec.scheduleStep(task);
//            } else {
//                return recursiveWait(exec, tsTick, task);
//            }
//        } else {
//             return IFuture.DONE;
//        }
//    }
//
//    protected IFuture<Void> wait0(boolean noValidation, IExecutionFeature exec, long delayMs, IComponentStep<Void> task) {
//        if(noValidation || isValid(delayMs)) {
//            if(delayMs <= 0L) {
//                return exec.scheduleStep(task);
//            }
//            final double tickDelta = converter.delayToTick(delayMs);
//            if(tickDelta <= 0.0) {
//                return exec.scheduleStep(task);
//            }
//            final double untilTick = clock.getTick() + tickDelta;
//            return recursiveWait(exec, untilTick, task);
//        } else {
//            return IFuture.DONE;
//        }
//    }

    @Override
    public TimeMode getMode() {
        return TimeMode.DISCRETE;
    }

    @Override
    public JadexTimestamp convert(ZonedDateTime zdt) {
        return new BasicTimestamp(zdt);
    }

    @Override
    public JadexTimestamp now() {
        double nt = clock.getTick();
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
        nowStamp = new BasicTimestamp(nowZdt);
        return nowStamp;
    }

    @Override
    public boolean isValid(long delay) {
        delay = Math.max(delay, 0L);
        return delay < delayTillEnd;
    }

    @Override
    public boolean isValid(Timestamp ts) {
        if(startTime == null || endTime == null || ts == null) {
            return false;
        }
        return ts.isAfterOrEquals(startTime) && ts.isBeforeOrEqual(endTime);
    }
}
