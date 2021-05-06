package de.unileipzig.irpact.commons.persistence;

/**
 * @author Daniel Abitz
 */
public interface Persister<T> {

    Class<T> getType();

    /**
     * Creates the initial persistable reference.
     * Id reference calls should only be used in {@link #setupPersist(Object, Persistable, PersistManager)} to avoid
     * infinite loops.
     *
     * @param object object to persist
     * @param manager used manager for reference calls
     * @return persited object
     * @throws PersistException
     */
    Persistable initalizePersist(T object, PersistManager manager) throws PersistException;

    /**
     * Allows to set references to other objects. Reference calls can now be used without problem.
     *
     * @param object object to persist
     * @param persistable persisted object
     * @param manager used manager for reference calls
     * @throws PersistException
     */
    void setupPersist(T object, Persistable persistable, PersistManager manager) throws PersistException;
}
