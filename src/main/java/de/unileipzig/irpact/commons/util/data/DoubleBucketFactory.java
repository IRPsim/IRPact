package de.unileipzig.irpact.commons.util.data;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class DoubleBucketFactory implements BucketFactory<Number> {

    protected static final DoubleBucket NAN_BUCKET = new DoubleBucket(Double.NaN, Double.NaN);
    protected final double RANGE;
    protected Map<Double, DoubleBucket> bucketCache;

    public DoubleBucketFactory(double range) {
        this(range, true);
    }

    public DoubleBucketFactory(double range, boolean cacheBuckets) {
        this.RANGE = range;
        cacheBuckets(cacheBuckets);
    }

    public double getRange() {
        return RANGE;
    }

    public void cacheBuckets(boolean enabled) {
        if(enabled) {
            if(bucketCache == null) {
                bucketCache = new TreeMap<>();
            }
        } else {
            bucketCache = null;
        }
    }

    public void clearCache() {
        if(hasBucketCache()) {
            bucketCache.clear();
        }
    }

    public boolean hasBucketCache() {
        return bucketCache != null;
    }

    public Map<Double, DoubleBucket> getBucketCache() {
        return bucketCache;
    }

    protected DoubleBucket tryGetBucket(double value) {
        if(hasBucketCache()) {
            return bucketCache.get(value);
        } else {
            return null;
        }
    }

    @Override
    public DoubleBucket createBucket(Number value) {
        return createBucket(value.doubleValue());
    }

    public DoubleBucket createBucket(double value) {
        if(Double.isNaN(value)) {
            return NAN_BUCKET;
        }

        double from = Math.floor(value / RANGE) * RANGE;
        DoubleBucket bucket = tryGetBucket(from);
        if(bucket == null) {
            return syncCreateBucket(from);
        } else {
            return bucket;
        }
    }

    protected boolean isNaN(Number value) {
        return Double.isNaN(value.doubleValue());
    }

    protected synchronized DoubleBucket syncCreateBucket(double from) {
        DoubleBucket bucket = tryGetBucket(from);
        if(bucket == null) {
            bucket = new DoubleBucket(from, from + RANGE);
            if(hasBucketCache()) {
                bucketCache.put(from, bucket);
            }
        }
        return bucket;
    }

    protected Set<Bucket<Number>> getBuckets(Number from, Number to) {
        double f = from.doubleValue();
        double t = to.doubleValue();
        Set<Bucket<Number>> buckets = new LinkedHashSet<>();
        for(double d = f; d <= t; d += RANGE) {
            buckets.add(createBucket(d));
        }
        //sanity
        buckets.add(createBucket(to.doubleValue()));
        return buckets;
    }

    @Override
    public void createBuckets(Number from, Number to) {
        getBuckets(from, to);
    }

    @Override
    public Collection<DoubleBucket> getAllBuckets() {
        return bucketCache.values();
    }

    @Override
    public Stream<Bucket<Number>> streamBuckets(Number from, Number to) {
        return getBuckets(from, to).stream();
    }

    @Override
    public Bucket<Number> min(Stream<? extends Bucket<Number>> buckets) {
        Optional<? extends Bucket<Number>> min = buckets.min((Comparator<Bucket<Number>>) DoubleBucket::compareNumberBuckets);
        return min.orElse(null);
    }

    @Override
    public Bucket<Number> max(Stream<? extends Bucket<Number>> buckets) {
        Optional<? extends Bucket<Number>> max = buckets.max((Comparator<Bucket<Number>>) DoubleBucket::compareNumberBuckets);
        return max.orElse(null);
    }
}
