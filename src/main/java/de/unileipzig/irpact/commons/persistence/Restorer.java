package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.RestoreException;

/**
 * @author Daniel Abitz
 */
public interface Restorer<T> {

    Class<T> getType();

    T initalizeRestore(Persistable persistable, RestoreManager manager) throws RestoreException;

    void setupRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException;

    void finalizeRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException;

    void validateRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException;
}
