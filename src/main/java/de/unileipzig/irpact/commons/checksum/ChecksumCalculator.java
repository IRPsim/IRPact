package de.unileipzig.irpact.commons.checksum;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.data.MutableInt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public abstract class ChecksumCalculator {

    protected static final int PRIM = 31;

    public ChecksumCalculator() {
    }

    //=========================
    //general
    //=========================

    public String printChecksum(Object obj) {
        return Integer.toHexString(getChecksum(obj));
    }

    public int getChecksum(int value) {
        return Integer.hashCode(value);
    }

    public int getChecksum(long value) {
        return Long.hashCode(value);
    }

    public int getChecksum(double value) {
        return Double.hashCode(value);
    }

    public abstract int getChecksum(Object value);

    public int getChecksum(Object... values) {
        if(values == null) {
            return ChecksumComparable.DEFAULT_NONNULL_CHECKSUM;
        }
        int cs = 1;
        for(Object obj: values) {
            cs = PRIM * cs + getChecksum(obj);
        }
        return cs;
    }

    public int getNamedChecksum(Nameable nameable) {
        if(nameable == null) {
            return ChecksumComparable.DEFAULT_NONNULL_CHECKSUM;
        }
        if(nameable.getName() == null) {
            return ChecksumComparable.DEFAULT_NONNULL_CHECKSUM;
        }
        return nameable.getName().hashCode();
    }

    public int getSystemChecksum(Object value) {
        return value == null
                ? ChecksumComparable.NULL_CHECKSUM
                : System.identityHashCode(value);
    }

    //=========================
    // simple collections
    //=========================

    public int getCollectionChecksum(Collection<?> coll) {
        return getCollectionChecksum(coll, null);
    }
    public <E> int getCollectionChecksum(Collection<E> coll, Function<? super E, ?> elementMapper) {
        if(coll instanceof List) {
            return getListChecksum((List<E>) coll, elementMapper);
        }
        if(coll instanceof Set) {
            return getSetChecksum((Set<E>) coll, elementMapper);
        }
        //fallback - same as set
        int cs = 0;
        for(E e: coll) {
            Object obj = elementMapper == null ? e : elementMapper.apply(e);
            cs += getChecksum(obj);
        }
        return cs;
    }

    public int getListChecksum(List<?> list) {
        return getListChecksum(list, null);
    }
    public <E> int getListChecksum(List<E> list, Function<? super E, ?> elementMapper) {
        int cs = 1;
        for(E e: list) {
            Object obj = elementMapper == null ? e : elementMapper.apply(e);
            cs = PRIM * cs + getChecksum(obj);
        }
        return cs;
    }

    public int getSetChecksum(Set<?> set) {
        return getSetChecksum(set, null);
    }
    public <E> int getSetChecksum(Set<E> set, Function<? super E, ?> elementMapper) {
        int cs = 0;
        for(E e: set) {
            Object obj = elementMapper == null ? e : elementMapper.apply(e);
            cs += getChecksum(obj);
        }
        return cs;
    }

    public int getMapChecksum(Map<?, ?> map) {
        return getMapChecksum(map, null, null);
    }
    public <K, V> int getMapChecksum(
            Map<K, V> map,
            Function<? super K, ?> keyMapper,
            Function<? super V, ?> valueMapper) {
        int cs = 0;
        for(Map.Entry<K, V> entry: map.entrySet()) {
            Object key = keyMapper == null ? entry.getKey() : keyMapper.apply(entry.getKey());
            Object value = valueMapper == null ? entry.getValue() : valueMapper.apply(entry.getValue());
            int keyChecksum = getChecksum(key);
            int valueChecksum = getChecksum(value);
            cs += keyChecksum ^ valueChecksum;
        }
        return cs;
    }

    //=========================
    // stream
    //=========================

    public int getUnorderedStreamChecksum(Stream<?> stream) {
        return getUnorderedStreamChecksum(stream, null);
    }
    public <E> int getUnorderedStreamChecksum(Stream<E> stream, Function<? super E, ?> elementMapper) {
        MutableInt cs = MutableInt.zero();
        stream.forEach(e -> {
            Object obj = elementMapper == null ? e : elementMapper.apply(e);
            cs.update(getChecksum(obj));
        });
        return cs.get();
    }
}
