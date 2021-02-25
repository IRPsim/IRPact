package de.unileipzig.irpact.commons.persistence;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface RestoreManager {

    <T> T ensureGet(long uid) throws NoSuchElementException;

    <T> T[] ensureGetAll(long[] uids, IntFunction<T[]> arrCreator) throws NoSuchElementException;

    <K, V> Map<K, V> ensureGetAll(Map<Long, Long> idMap) throws NoSuchElementException;

    <T> Stream<T> streamSameClass(Class<T> c);

    <T> Stream<T> streamIsInstance(Class<T> c);

    <T> T ensureGetSameClass(Class<T> c) throws NoSuchElementException;
}
