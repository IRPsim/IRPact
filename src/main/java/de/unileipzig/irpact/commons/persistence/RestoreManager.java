package de.unileipzig.irpact.commons.persistence;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.util.MetaData;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

/**
 * @author Daniel Abitz
 */
public interface RestoreManager extends Nameable {

    void unregisterAll();

    void register(Persistable persistable);

    void register(Collection<? extends Persistable> coll);

    void restore() throws RestoreException;

    void restore(MetaData metaData) throws RestoreException;

    void setRestoredInstance(Object restored);

    <T> T getRestoredInstance() throws NoSuchElementException;

    void setValidationChecksum(int checksum);

    int getValidationChecksum() throws NoSuchElementException;

    <T> T ensureGet(long uid) throws RestoreException;

    default <T> T uncheckedEnsureGet(long uid) throws UncheckedRestoreException {
        try {
            return ensureGet(uid);
        } catch (RestoreException e) {
            throw e.unchecked();
        }
    }

    <T> LongFunction<T> ensureGetFunction();

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
