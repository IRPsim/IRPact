package de.unileipzig.irpact.core.persistence.binaryjson2;

import de.unileipzig.irpact.io.param.input.InRoot;

/**
 * @author Daniel Abitz
 */
public interface RestoreManager {

    default boolean isRestored(InRoot root) {
        throw new UnsupportedOperationException();
    }

    default void setup(InRoot root) {
        throw new UnsupportedOperationException();
    }

    Object restoreInstance() throws Throwable;

    Object restoreInstance(long uid) throws Throwable;
}
