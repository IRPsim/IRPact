package de.unileipzig.irpact.util;

import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.search.ServiceQuery;

/**
 * @author Daniel Abitz
 * @since 0.0
 */
public final class JadexUtil {

    public static <T> ServiceQuery<T> searchNetwork(Class<T> serviceType) {
        return new ServiceQuery<>(serviceType, ServiceScope.NETWORK);
    }
}
