package de.unileipzig.irpact.core.persistence.binaryjson2.restore;

/**
 * @author Daniel Abitz
 */
public interface RestoreManager {

    Object restoreInstance() throws Throwable;

    Object restoreInstance(long uid) throws Throwable;
}
