package de.unileipzig.irpact.jadex.util;

import jadex.bridge.IComponentStep;
import jadex.bridge.IExternalAccess;
import jadex.bridge.component.IExternalExecutionFeature;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;
import org.slf4j.Logger;

import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class JadexUtil {

    private static PrintStream systemOut;

    static {
        systemOut = System.out;
    }

    private JadexUtil() {
    }

    //=========================
    //Terminate Message
    //=========================

    private static PrintStream oldOutStream;

    public static void ignoreTerminateMessage() {
        oldOutStream = System.out;
        JadexIgnoreTerminatedPrintStream ignoreStream = new JadexIgnoreTerminatedPrintStream(oldOutStream);
        System.setOut(ignoreStream);
    }

    public static void redirectTerminateMessage() {
        redirectTerminateMessageTo(null);
    }

    public static void redirectTerminateMessageTo(Logger logger) {
        oldOutStream = System.out;
        JadexRedirectPrintStream redirectStream = new JadexRedirectPrintStream(oldOutStream, logger);
        System.setOut(redirectStream);
    }

    public static void printTerminateMessage() {
        if(oldOutStream != null) {
            System.setOut(systemOut);
            try {
                oldOutStream.close();
            } finally {
                oldOutStream = null;
            }
        }
    }

    //=========================
    //
    //=========================

    public static boolean isContinous(IExternalAccess access) {
        IClockService clock = getClockService(access);
        return isContinous(clock);
    }

    public static boolean isContinous(IClockService clock) {
        return Objects.equals(IClock.TYPE_CONTINUOUS, clock.getClockType());
    }

    public static boolean isEventDriven(IExternalAccess access) {
        IClockService clock = getClockService(access);
        return isEventDriven(clock);
    }

    public static boolean isEventDriven(IClockService clock) {
        return Objects.equals(IClock.TYPE_EVENT_DRIVEN, clock.getClockType());
    }

    public static IClockService getClockService(IExternalAccess access) {
        ServiceQuery<IClockService> query = new ServiceQuery<>(IClockService.class);
        return access.searchService(query)
                .get();
    }

    public static ISimulationService getSimulationService(IExternalAccess access) {
        ServiceQuery<ISimulationService> query = new ServiceQuery<>(ISimulationService.class);
        return access.searchService(query)
                .get();
    }

    public static <T> ServiceQuery<T> searchNetwork(Class<T> serviceType) {
        return new ServiceQuery<>(serviceType, ServiceScope.NETWORK);
    }

    public static <T> ServiceQuery<T> searchPlatform(Class<T> serviceType) {
        return new ServiceQuery<>(serviceType, ServiceScope.PLATFORM);
    }

    public static IFuture<Void> waitForTick(
            IExternalExecutionFeature exec,
            int count) {
        if(count < 2) {
            return exec.waitForTick();
        } else {
            return exec.waitForTick(ia -> {
                for(int i = 0; i < count - 1; i++) {
                    exec.waitForTick().get();
                }
                return IFuture.DONE;
            });
        }
    }

    public static IFuture<Void> waitForTick(
            IExternalExecutionFeature exec,
            int count,
            IComponentStep<Void> step) {
        if(count < 2) {
            return exec.waitForTick(step);
        } else {
            return exec.waitForTick(ia -> {
                for(int i = 0; i < count - 1; i++) {
                    exec.waitForTick().get();
                }
                return step.execute(ia);
            });
        }
    }
}
