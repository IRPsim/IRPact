package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.time.TimeModel;
import jadex.bridge.IComponentStep;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Daniel Abitz
 */
public interface JadexTimeModel extends TimeModel {

    IClockService getClockService();

    ISimulationService getSimulationService();

    IFuture<Void> waitUntil(IExecutionFeature exec, JadexTimestamp ts, IComponentStep<Void> task);

    IFuture<Void> wait(IExecutionFeature exec, long delay, IComponentStep<Void> task);

    IFuture<Void> forceWait(IExecutionFeature exec, long delay, IComponentStep<Void> task);

//    IFuture<Void> waitUntil0(IExecutionFeature exec, JadexTimestamp ts, IComponentStep<Void> task);
//
//    //delay = ms or tick!
//    IFuture<Void> wait0(IExecutionFeature exec, long delay, IComponentStep<Void> task);
//
//    IFuture<Void> waitUntilEnd(IExecutionFeature exec, IComponentStep<Void> task);

    @Override
    JadexTimestamp convert(ZonedDateTime zdt);

    @Override
    JadexTimestamp now();

    @Override
    JadexTimestamp startTime();

    @Override
    JadexTimestamp endTime();

    @Override
    default JadexTimestamp plusMillis(long millis) {
        return plusMillis(now(), millis);
    }

    @Override
    default JadexTimestamp plusMillis(Timestamp ts, long millis) {
        ZonedDateTime zdt = ts.getTime().plus(millis, ChronoUnit.MILLIS);
        return new BasicTimestamp(zdt);
    }

    @Override
    default JadexTimestamp plusDays(Timestamp ts, long days) {
        ZonedDateTime zdt = ts.getTime().plusDays(days);
        return new BasicTimestamp(zdt);
    }

    @Override
    default JadexTimestamp plusYears(Timestamp ts, long years) {
        ZonedDateTime zdt = ts.getTime().plusYears(years);
        return new BasicTimestamp(zdt);
    }
}
