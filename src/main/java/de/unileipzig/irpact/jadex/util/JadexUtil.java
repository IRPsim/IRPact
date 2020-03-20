package de.unileipzig.irpact.jadex.util;

import jadex.bridge.IComponentStep;
import jadex.bridge.component.IExternalExecutionFeature;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.search.ServiceQuery;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public final class JadexUtil {

    private JadexUtil() {
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
