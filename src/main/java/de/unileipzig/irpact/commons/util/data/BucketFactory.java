package de.unileipzig.irpact.commons.util.data;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface BucketFactory<T> {

    Bucket<T> createBucket(T value);

    void createBuckets(T from, T to);

    Collection<? extends Bucket<T>> getAllBuckets();

    Stream<Bucket<T>> streamBuckets(T from, T to);

    Bucket<T> min(Stream<? extends Bucket<T>> buckets);

    Bucket<T> max(Stream<? extends Bucket<T>> buckets);
}
