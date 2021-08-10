package de.unileipzig.irpact.jadex.util;

import jadex.bridge.IExternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.component.IExternalRequiredServicesFeature;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IntermediateDefaultResultListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public final class JadexUtil {

    public static final IFuture<Void> END_TIME_REACHED = new Future<>((Void) null);

    protected JadexUtil() {
    }

    public static boolean endTimeReached(IFuture<Void> future) {
        return future == END_TIME_REACHED;
    }

    public static <T> void searchPlatformServices(
            IRequiredServicesFeature feature,
            Class<T> c,
            Consumer<T> consumer) {
        feature.searchServices(new ServiceQuery<>(c, ServiceScope.PLATFORM))
                .addResultListener(new IntermediateDefaultResultListener<T>() {
                    @Override
                    public void intermediateResultAvailable(T result) {
                        consumer.accept(result);
                    }
                });
    }

    public static <T> T getPlatformService(IExternalRequiredServicesFeature feature, Class<T> c) {
        return feature.searchService(new ServiceQuery<>(c, ServiceScope.PLATFORM)).get();
    }

    public static <T> T getService(Class<T> c, IExternalAccess access) {
        return access.searchService(new ServiceQuery<>(c)).get();
    }

    public static IClockService getClockService(IExternalAccess access) {
        return getService(IClockService.class, access);
    }

    public static ISimulationService getSimulationService(IExternalAccess access) {
        return getService(ISimulationService.class, access);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void killComponents(Collection<? extends IExternalAccess> accesses) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(IExternalAccess access: accesses) {
            exec.execute(() -> access.killComponent().get());
        }
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    public static void killComponents2(Collection<? extends IExternalAccess> accesses) throws InterruptedException {
        List<Thread> killThreads = new ArrayList<>(accesses.size());
        for(final IExternalAccess access: accesses) {
            Thread t = new Thread(() -> access.killComponent().get());
            killThreads.add(t);
        }
        for(Thread t: killThreads) {
            t.start();
        }
        for(Thread t: killThreads) {
            t.join();
        }
        killThreads.clear();
    }
}
