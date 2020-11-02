package de.unileipzig.irpact.v2.jadex.time;

import de.unileipzig.irpact.v2.commons.time.*;
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
    protected long storedDelay;

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

    public void setStoredDelay(long storedDelay) {
        this.storedDelay = storedDelay;
    }

    public long getStoredDelay() {
        return storedDelay;
    }

    public void setStartYear(int startYear, long timePerTickInMs) {
        converter.init(startYear, timePerTickInMs, ZoneId.systemDefault());
    }

    public void setStartTick(double tick) {
        converter.setStart(tick);
        setStart(new BasicTimestamp(converter.getStartTime()));
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
        return waitUntil0(false, exec, ts, task);
    }

    @Override
    public IFuture<Void> wait(IExecutionFeature exec, long delayMs, IComponentStep<Void> task) {
        return wait0(false, exec, delayMs, task);
    }

    @Override
    public IFuture<Void> waitUntil0(IExecutionFeature exec, JadexTimestamp ts, IComponentStep<Void> task) {
        return waitUntil0(true, exec, ts, task);
    }

    @Override
    public IFuture<Void> wait0(IExecutionFeature exec, long delayMs, IComponentStep<Void> task) {
        return wait0(true, exec, delayMs, task);
    }

    protected IFuture<Void> waitUntil0(boolean noValidation, IExecutionFeature exec, JadexTimestamp ts, IComponentStep<Void> task) {
        if(noValidation || isValid(ts)) {
            final double tsTick = converter.timeToTick(ts.getTime());
            final double nowTick = clock.getTick();
            if(tsTick <= nowTick) {
                return exec.scheduleStep(task);
            } else {
                return recursiveWait(exec, tsTick, task);
            }
        } else {
             return IFuture.DONE;
        }
    }

    protected IFuture<Void> wait0(boolean noValidation, IExecutionFeature exec, long delayMs, IComponentStep<Void> task) {
        if(noValidation || isValid(delayMs)) {
            if(delayMs <= 0L) {
                return exec.scheduleStep(task);
            }
            final double tickDelta = converter.delayToTick(delayMs);
            if(tickDelta <= 0.0) {
                return exec.scheduleStep(task);
            }
            final double untilTick = clock.getTick() + tickDelta;
            return recursiveWait(exec, untilTick, task);
        } else {
            return IFuture.DONE;
        }
    }

    @Override
    public TimeMode getMode() {
        return TimeMode.DISCRETE;
    }

    @Override
    public JadexTimestamp now() {
        double nowTick = clock.getTick();
        ZonedDateTime nowZdt = converter.tickToTime(nowTick);
        return new BasicTimestamp(nowZdt);
    }
}
