package de.unileipzig.irpact.experimental;

import jadex.bridge.IComponentStep;
import jadex.bridge.IExternalAccess;
import jadex.bridge.component.IExternalExecutionFeature;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.component.IExternalRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.bridge.service.types.threadpool.IThreadPoolService;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public final class ExperimentalUtil {

    private ExperimentalUtil() {
    }

    public static IExternalExecutionFeature getIExternalExecutionFeature(IExternalAccess access) {
        return access.getExternalFeature(IExternalExecutionFeature.class);
    }

    public static IExternalRequiredServicesFeature getIExternalRequiredServicesFeature(IExternalAccess access) {
        return access.getExternalFeature(IExternalRequiredServicesFeature.class);
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

    public static IThreadPoolService getThreadPoolService(IExternalAccess access) {
        ServiceQuery<IThreadPoolService> query = new ServiceQuery<>(IThreadPoolService.class);
        return access.searchService(query)
                .get();
    }

    public static <T> T findService(IExternalRequiredServicesFeature reqService, Class<T> c) {
        return findService(reqService, c, ServiceScope.PLATFORM);
    }

    public static <T> T findService(IExternalRequiredServicesFeature reqService, Class<T> c, ServiceScope scope) {
        return reqService.searchService(new ServiceQuery<>(c, scope))
                .get();
    }

    public static IFuture<Void> waitForTicks(
            IExternalExecutionFeature exec,
            long ticks,
            IComponentStep<Void> run) {
        return exec.waitForTick(access -> {
            long t = ticks;
            while(t-- > 1L) {
                exec.waitForTick().get();
            }
            return run.execute(access);
        });
    }
}
