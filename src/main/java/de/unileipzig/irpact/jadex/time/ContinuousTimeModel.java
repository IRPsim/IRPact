package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.ContinuousConverter;
import de.unileipzig.irpact.commons.time.TimeMode;
import de.unileipzig.irpact.develop.TodoException;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
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
        this.startTime = new BasicTimestamp(converter.getStartTime());
    }

    @Override
    public void setupTimeModel() {
        throw new RuntimeException("TODO");
    }

    public void setEndTime(JadexTimestamp endTime) {
        this.endTime = endTime;
        delayUntilEnd = converter.timeBetween(startTime.getTime(), endTime.getTime());
    }

    public ContinuousConverter getConverter() {
        return converter;
    }

    @Override
    public IFuture<Void> waitUntil(IExecutionFeature exec, JadexTimestamp ts, IInternalAccess access, IComponentStep<Void> task) {
        if(isValid(ts)) {
            JadexTimestamp now = now();
            long delay = converter.timeBetween(now.getTime(), ts.getTime());
            return wait(exec, delay, access, task);
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
        return exec.waitForDelay(delay, task, false);
    }

    @Override
    public IFuture<Void> scheduleImmediately(IExecutionFeature exec, IInternalAccess access, IComponentStep<Void> task) {
        return uncheckedWait(exec, 0L, access, task);
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
        long nowMs = getClockService().getTime();
        ZonedDateTime nowZdt = converter.timeToDate(nowMs);
        return new BasicTimestamp(nowZdt);
    }

    @Override
    public int getFirstSimulationYear() {
        throw new RuntimeException("TODO");
    }

    @Override
    public int getLastSimulationYear() {
        throw new RuntimeException("TODO");
    }

    @Override
    public boolean isValid(long delay) {
        delay = Math.max(delay, 0L);
        return delay < delayUntilEnd;
    }

    @Override
    public boolean hasYearChange() {
        throw new TodoException();
    }

    @Override
    public void performYearChange() {
        throw new TodoException();
    }
}
