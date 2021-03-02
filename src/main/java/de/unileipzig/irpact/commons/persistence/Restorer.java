package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.RestoreException;

/**
 * @author Daniel Abitz
 */
public interface Restorer<T> {

    Class<T> getType();

    /**
     * Phase 1: creates instance and sets independent values
     *
     * @param persistable
     * @param manager
     * @return
     * @throws RestoreException
     */
    T initalizeRestore(Persistable persistable, RestoreManager manager) throws RestoreException;

    /**
     * Phase 2: sets direct-dependent values (based on id)
     *
     * @param persistable
     * @param object
     * @param manager
     * @throws RestoreException
     */
    void setupRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException;

    /**
     * Phase 3: sets indirect-dependent or transitive values (e.g. searched by name).
     *
     * @param persistable
     * @param object
     * @param manager
     * @throws RestoreException
     */
    void finalizeRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException;

    /**
     * Phase 4: allows instance validation
     *
     * @param persistable
     * @param object
     * @param manager
     * @throws RestoreException
     */
    void validateRestore(Persistable persistable, T object, RestoreManager manager) throws RestoreException;
}
