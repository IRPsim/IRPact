package de.unileipzig.irpact.core.persistence.binaryjson2.restore;

/**
 * @author Daniel Abitz
 */
public interface RestoreHelper {

    Object restore(long uid) throws Throwable;
}
