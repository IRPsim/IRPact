package de.unileipzig.irpact.core.persistence.binaryjson2;

/**
 * @author Daniel Abitz
 */
public interface PersistHelper {

    long getClassId(Class<?> c);

    void peek(Object obj);

    long getUid(Object obj) throws Throwable;
}
