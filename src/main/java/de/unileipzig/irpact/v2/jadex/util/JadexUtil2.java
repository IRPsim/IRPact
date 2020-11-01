package de.unileipzig.irpact.v2.jadex.util;

import de.unileipzig.irpact.temp.jadex.TimerService;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
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
}
