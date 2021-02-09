package de.unileipzig.irpact.core.misc;

/**
 * Markerinterface for placeholder objects.
 *
 * @author Daniel Abitz
 */
public interface Placeholder {

    static boolean isPlaceholder(Object obj) {
        return obj instanceof Placeholder;
    }
}
