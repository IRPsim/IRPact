package de.unileipzig.irpact.commons.util.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * @author Daniel Abitz
 */
public class BucketMap<K, V> {

    protected BucketFactory<K> factory;
    protected Map<Bucket<K>, V> map;

    public BucketMap(BucketFactory<K> factory, Map<Bucket<K>, V> map) {
        this.factory = factory;
        this.map = map;
    }

    public Map<Bucket<K>, V> getMap() {
        return map;
    }

    public BucketFactory<K> getFactory() {
        return factory;
    }

    protected Bucket<K> getBucket(K value) {
        return getFactory().createBucket(value);
    }

    public V put(K key, V value) {
        return map.put(getBucket(key), value);
    }

    public V get(K key) {
        return map.get(getBucket(key));
    }

    public V update(K key, V ifMissing, UnaryOperator<V> updater) {
        Bucket<K> bucket = getBucket(key);
        V current = map.getOrDefault(bucket, ifMissing);
        V newValue = updater.apply(current);
        return map.put(bucket, newValue);
    }

    public void addMissingBuckets(V nullValue) {
        if(map.isEmpty()) {
            return;
        }

        Bucket<K> min = factory.min(buckets().stream());
        Bucket<K> max = factory.max(buckets().stream());

        addMissingBuckets(min.getFrom(), max.getFrom(), nullValue);
    }

    public void addMissingBuckets(K from, K to, V nullValue) {
        factory.streamBuckets(from, to)
                .forEach(bucket -> map.putIfAbsent(bucket, nullValue));
    }

    public Set<Bucket<K>> buckets() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public Set<Map.Entry<Bucket<K>, V>> entries() {
        return map.entrySet();
    }
}
