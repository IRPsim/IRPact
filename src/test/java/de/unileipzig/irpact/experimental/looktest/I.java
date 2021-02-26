package de.unileipzig.irpact.experimental.looktest;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
public interface I {

    static Class<?> lookupClass() {
        return MethodHandles.lookup().lookupClass();
    }
}
