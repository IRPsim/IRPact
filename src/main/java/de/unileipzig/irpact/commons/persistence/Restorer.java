package de.unileipzig.irpact.commons.persistence;

/**
 * @author Daniel Abitz
 */
public interface Restorer<T> {

    Class<T> getType();

    T initalize(Persistable persistable);

    void setup(Persistable persistable, T object, RestoreManager manager);

    void finalize(Persistable persistable, T object, RestoreManager manager);
}
