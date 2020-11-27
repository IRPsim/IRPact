package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.ContinuousConverter;
import de.unileipzig.irpact.commons.time.TimeMode;
import jadex.bridge.IComponentStep;
import jadex.bridge.component.IExecutionFeature;
import jadex.commons.future.IFuture;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public class ContinuousTimeModel extends AbstractJadexTimeModel {

    protected final ContinuousConverter converter = new ContinuousConverter();
    protected double storedDilation;
    protected long storedDelay;

    public ContinuousTimeModel() {
    }

    public void setStoredDilation(double storedDilation) {
        this.storedDilation = storedDilation;
    }

    public double getStoredDilation() {
        return storedDilation;
    }

    public void setStoredDelay(long storedDelay) {
        this.storedDelay = storedDelay;
    }

    public long getStoredDelay() {
        return storedDelay;
    }

    public void setStartYear(int year) {
        converter.init(year, ZoneId.systemDefault());
    }

    public void setStartTime(long timeInMs) {
        converter.setStart(timeInMs);
        setStart(new BasicTimestamp(converter.getStartTime()));
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
            long tsTime = converter.fromTime(ts.getTime());
            long nowTime = clock.getTime();
            long delta = tsTime - nowTime;
            return wait(exec, delta, task);
        } else {
            return IFuture.DONE;
        }
    }

    public IFuture<Void> wait0(boolean noValidation, IExecutionFeature exec, long delayMs, IComponentStep<Void> task) {
        if(noValidation || isValid(delayMs)) {
            if(delayMs <= 0L) {
                return exec.scheduleStep(task);
            } else {
                return exec.waitForDelay(delayMs, task);
            }
        } else {
            return IFuture.DONE;
        }
    }

    @Override
    public TimeMode getMode() {
        return TimeMode.CONTINUOUS;
    }

    @Override
    public JadexTimestamp now() {
        long nowMs = clock.getTime();
        ZonedDateTime nowZdt = converter.toTime(nowMs);
        return new BasicTimestamp(nowZdt);
    }
}
