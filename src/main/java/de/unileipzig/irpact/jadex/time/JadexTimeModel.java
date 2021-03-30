package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.time.TimeModel;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;

import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Daniel Abitz
 */
public interface JadexTimeModel extends TimeModel {

    IClockService getClockService();

    ISimulationService getSimulationService();

    IFuture<Void> waitUntil(IExecutionFeature exec, JadexTimestamp ts, IInternalAccess access, IComponentStep<Void> task);

    IFuture<Void> waitUntilEnd(IExecutionFeature exec, IInternalAccess access, IComponentStep<Void> task);

    IFuture<Void> wait(IExecutionFeature exec, long delay, IInternalAccess access, IComponentStep<Void> task);

    IFuture<Void> uncheckedWait(IExecutionFeature exec, long delay, IInternalAccess access, IComponentStep<Void> task);

    //naechstbeste zeitpunkt
    IFuture<Void> scheduleImmediately(IExecutionFeature exec, IInternalAccess access, IComponentStep<Void> task);

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
        return convert(zdt);
    }

    @Override
    default JadexTimestamp plusDays(Timestamp ts, long days) {
        ZonedDateTime zdt = ts.getTime().plusDays(days);
        return convert(zdt);
    }

    @Override
    default JadexTimestamp plusYears(Timestamp ts, long years) {
        ZonedDateTime zdt = ts.getTime().plusYears(years);
        return convert(zdt);
    }

    @Override
    default Timestamp atStartOfYear(int year) {
        return at(year, Month.JANUARY, 1);
    }

    @Override
    default Timestamp at(int year, long weeks) {
        ZonedDateTime zdt = TimeUtil.of(year, weeks);
        return convert(zdt);
    }

    @Override
    default Timestamp at(int year, Month month, int day) {
        ZonedDateTime zdt = TimeUtil.of(year, month, day);
        return convert(zdt);
    }
}
