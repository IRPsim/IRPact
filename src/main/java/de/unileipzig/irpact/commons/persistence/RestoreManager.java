package de.unileipzig.irpact.commons.persistence;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;

/**
 * @author Daniel Abitz
 */
public interface RestoreManager {

    void restore(Collection<? extends Persistable> coll) throws RestoreException;

    void setInitialInstance(Object initial);

    <T> T getInitialInstance();

    void setRestoredInstance(Object restored);

    <T> T getRestoredInstance();

    void setValidationChecksum(int checksum);

    int getValidationChecksum();

    <T> T ensureGet(long uid) throws RestoreException;

    <T> T[] ensureGetAll(long[] uids, IntFunction<T[]> arrCreator) throws RestoreException;

    <T> boolean ensureGetAll(long[] uids, Collection<? super T> target) throws RestoreException;

    <K, V> Map<K, V> ensureGetAll(Map<Long, Long> idMap) throws RestoreException;

    /**
     * Search for the exact class (c == x.getClass)
     *
     * @param c class of the searched instance
     * @param <T> type of the searched instance
     * @return found instance
     * @throws RestoreException If no instance was found.
     */
    <T> T ensureGetSameClass(Class<T> c) throws RestoreException;

    /**
     * Search for the instance with {@link Class#isInstance(Object)}.
     *
     * @param c class of the searched instance
     * @param <T> type of the searched instance
     * @return found instance
     * @throws RestoreException If no instance was found.
     */
    <T> T ensureGetInstanceOf(Class<T> c) throws RestoreException;

    <T> T ensureGetByName(String name) throws RestoreException;
}
