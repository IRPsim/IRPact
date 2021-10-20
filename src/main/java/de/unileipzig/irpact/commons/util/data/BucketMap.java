package de.unileipzig.irpact.commons.util.data;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * @author Daniel Abitz
 */
public class BucketMap<E> {

    protected static final Bucket NAN = new Bucket(Double.NaN, Double.NaN);

    protected final Map<Double, Bucket> BUCKET_CACHE = new HashMap<>();
    protected Map<Bucket, E> map;
    protected double range;

    public BucketMap(double range) {
        this(new TreeMap<>(), range);
    }

    public BucketMap(Map<Bucket, E> map, double range) {
        this.map = map;
        this.range = range;
    }

    public double getRange() {
        return range;
    }

    public Map<Bucket, E> getMap() {
        return map;
    }

    public Bucket calculateBucket(double value) {
        if(Double.isNaN(value)) {
            return NAN;
        } else {
            double from = Math.floor(value / range) * range;
            Bucket bucket = BUCKET_CACHE.get(from);
            if(bucket == null) {
                bucket = new Bucket(from, from + range);
                BUCKET_CACHE.put(from, bucket);
            }
            return bucket;
        }
    }

    public E put(double value, E element) {
        Bucket bucket = calculateBucket(value);
        return map.put(bucket, element);
    }

    public E get(double value) {
        Bucket bucket = calculateBucket(value);
        return map.get(bucket);
    }

    public E update(double value, UnaryOperator<E> op) {
        return update(value, null, op);
    }

    public E update(double value, E ifMissing, UnaryOperator<E> op) {
        Bucket bucket = calculateBucket(value);
        E current = map.getOrDefault(bucket, ifMissing);
        E newValue = op.apply(current);
        return map.put(bucket, newValue);
    }

    public Set<Bucket> buckets() {
        return map.keySet();
    }

    public Collection<E> values() {
        return map.values();
    }

    public Set<Map.Entry<Bucket, E>> entries() {
        return map.entrySet();
    }

    public void addMissingBuckets(double from, double to, E nullValue) {
        for(double d = from; d <= to; d += range) {
            Bucket b = calculateBucket(d);
            map.putIfAbsent(b, nullValue);
        }
        //aritmetic
        Bucket b = calculateBucket(to);
        map.putIfAbsent(b, nullValue);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void addMissingBuckets(E nullValue) {
        if(map.isEmpty()) {
            return;
        }

        double min = buckets().stream()
                .mapToDouble(Bucket::getFrom)
                .min()
                .getAsDouble();

        double max = buckets().stream()
                .mapToDouble(Bucket::getFrom)
                .max()
                .getAsDouble();

        addMissingBuckets(min, max, nullValue);
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Bucket implements Comparable<Bucket> {

        private final double FROM;
        private final double TO;

        private Bucket(double from, double to) {
            this.FROM = from;
            this.TO = to;
        }

        public boolean isNaN() {
            return Double.isNaN(FROM);
        }

        public double getFrom() {
            return FROM;
        }

        public double getTo() {
            return TO;
        }

        public String print() {
            return isNaN()
                    ? "NaN"
                    : "[" + getFrom() + ", " + getTo() + ")";
        }

        public String print(DecimalFormat format) {
            if(format == null || isNaN()) {
                return print();
            } else {
                return "[" + format.format(getFrom()) + ", " + format.format(getTo()) + ")";
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Bucket bucket = (Bucket) o;
            return Double.compare(bucket.FROM, FROM) == 0 && Double.compare(bucket.TO, TO) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(FROM, TO);
        }

        @Override
        public int compareTo(Bucket o) {
            if(isNaN()) {
                if(o.isNaN()) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if(o.isNaN()) {
                    return 1;
                } else {
                    return Double.compare(getFrom(), o.getFrom());
                }
            }
        }

        @Override
        public String toString() {
            return Double.toString(FROM);
        }
    }
}
