package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.ContinuousConverter;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.core.misc.ValidationException;
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

    protected long delayUntilEnd;

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
        setStartTime(new BasicTimestamp(converter.getStartTime()));
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
        delayUntilEnd = converter.timeBetween(startTime.getTime(), endTime.getTime());
    }

    public ContinuousConverter getConverter() {
        return converter;
    }

    @Override
    public IFuture<Void> waitUntil(IExecutionFeature exec, JadexTimestamp ts, IComponentStep<Void> task) {
        if(isValid(ts)) {
            JadexTimestamp now = now();
            long delay = converter.timeBetween(now.getTime(), ts.getTime());
            return wait(exec, delay, task);
        } else {
            return IFuture.DONE;
        }
    }

    @Override
    public IFuture<Void> wait(IExecutionFeature exec, long delay, IComponentStep<Void> task) {
        if(isValid(delay)) {
            return exec.waitForDelay(delay, task, false);
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
//            long tsTime = converter.fromTime(ts.getTime());
//            long nowTime = clock.getTime();
//            long delta = tsTime - nowTime;
//            return wait(exec, delta, task);
//        } else {
//            return IFuture.DONE;
//        }
//    }
//
//    public IFuture<Void> wait0(boolean noValidation, IExecutionFeature exec, long delayMs, IComponentStep<Void> task) {
//        if(noValidation || isValid(delayMs)) {
//            if(delayMs <= 0L) {
//                return exec.scheduleStep(task);
//            } else {
//                return exec.waitForDelay(delayMs, task);
//            }
//        } else {
//            return IFuture.DONE;
//        }
//    }

    @Override
    public TimeMode getMode() {
        return TimeMode.CONTINUOUS;
    }

    @Override
    public JadexTimestamp convert(ZonedDateTime zdt) {
        return new BasicTimestamp(zdt);
    }

    //TODO instanzerzeugung optimieren
    @Override
    public JadexTimestamp now() {
        long nowMs = clock.getTime();
        ZonedDateTime nowZdt = converter.timeToDate(nowMs);
        return new BasicTimestamp(nowZdt);
    }

    @Override
    public boolean isValid(long delay) {
        delay = Math.max(delay, 0L);
        return delay < delayUntilEnd;
    }
}
