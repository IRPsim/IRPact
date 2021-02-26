package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.RestoreException;

/**
 * @author Daniel Abitz
 */
public interface Restorer<T> {

    Class<T> getType();

    T initalize(Persistable persistable, RestoreManager manager) throws RestoreException;

    void setup(Persistable persistable, T object, RestoreManager manager) throws RestoreException;

    void finalize(Persistable persistable, T object, RestoreManager manager) throws RestoreException;
}
