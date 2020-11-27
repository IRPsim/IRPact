package de.unileipzig.irpact.v2.jadex.util;

import jadex.bridge.IExternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.component.IExternalRequiredServicesFeature;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IntermediateDefaultResultListener;

import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public final class JadexUtil2 {

    protected JadexUtil2() {
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
}
