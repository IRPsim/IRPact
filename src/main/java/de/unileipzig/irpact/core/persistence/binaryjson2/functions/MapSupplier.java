package de.unileipzig.irpact.core.persistence.binaryjson2.functions;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public interface MapSupplier {

    <K, V> Map<K, V> newMap();
}
