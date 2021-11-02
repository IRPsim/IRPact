package de.unileipzig.irpact.commons.util.data;

import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface BucketFactory<T> {

    Bucket<T> createBucket(T value);

    Stream<Bucket<T>> streamBuckets(T from, T to);

    Bucket<T> min(Stream<? extends Bucket<T>> buckets);

    Bucket<T> max(Stream<? extends Bucket<T>> buckets);
}
