package de.unileipzig.irpact.jadex.util;

import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.search.ServiceQuery;

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
}
