package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.exception.RestoreException;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface RestoreManager {

    void restore(Collection<? extends Persistable> coll) throws RestoreException;

    void setInitialInstance(Object initial);

    <T> T getInitialInstance();

    void setRestoredInstance(Object restored);

    <T> T getRestoredInstance();

    void setValidationHash(int hash);

    int getValidationHash();

    <T> T ensureGet(long uid) throws NoSuchElementException;

    <T> T[] ensureGetAll(long[] uids, IntFunction<T[]> arrCreator) throws NoSuchElementException;

    <K, V> Map<K, V> ensureGetAll(Map<Long, Long> idMap) throws NoSuchElementException;

    /**
     * Search for the exact class (c == x.getClass)
     *
     * @param c class of the searched instance
     * @param <T> type of the searched instance
     * @return found instance
     * @throws NoSuchElementException If no instance was found.
     */
    <T> T ensureGetSameClass(Class<T> c) throws NoSuchElementException;

    /**
     * Search for the instance with {@link Class#isInstance(Object)}.
     *
     * @param c class of the searched instance
     * @param <T> type of the searched instance
     * @return found instance
     * @throws NoSuchElementException If no instance was found.
     */
    <T> T ensureGetInstanceOf(Class<T> c) throws NoSuchElementException;

    <T> T ensureGetByName(String name) throws NoSuchElementException;
}
