package de.unileipzig.irpact.commons.checksum;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.function.Function;

/**
 * For equals-checks without using {@link #equals(Object)}. Used to validate the restore process.
 *
 * @author Daniel Abitz
 */
public interface ChecksumComparable {

    IRPLogger LOGGER0 = IRPLogging.getLogger(ChecksumComparable.class);

    int NULL_CHECKSUM = 0;
    int DEFAULT_NONNULL_CHECKSUM = 31;

    static int getCollChecksum(Collection<?> coll) {
        if(coll instanceof Set) {
            return getSetChecksum((Set<?>) coll);
        }
        if(coll instanceof List) {
            return getListChecksum((List<?>) coll);
        }
        //fallback
        int h = 0;
        for(Object e: coll) {
            if(e != null) {
                h += getChecksum(e);
            }
        }
        return h;
    }

    //copy from ArrayList
    static int getListChecksum(List<?> coll) {
        int h = 1;
        for(Object e: coll) {
            h = 31 * h + (e == null ? 0 : getChecksum(e));
        }
        return h;
    }

    //copy from set
    static int getSetChecksum(Set<?> coll) {
        int h = 0;
        for(Object e: coll) {
            if(e != null) {
                h += getChecksum(e);
            }
        }
        return h;
    }

    @SuppressWarnings("unchecked")
    static int getCollCollChecksum(Collection<? extends Collection<?>> coll) {
        if(coll instanceof Set) {
            return getSetCollChecksum((Set<? extends Collection<?>>) coll);
        }
        if(coll instanceof List) {
            return getListCollChecksum((List<? extends Collection<?>>) coll);
        }
        //fallback, like set
        int h = 0;
        for(Collection<?> c: coll) {
            if(c != null) {
                h += getCollChecksum(c);
            }
        }
        return h;
    }

    static int getListCollChecksum(List<? extends Collection<?>> list) {
        int h = 1;
        for(Collection<?> coll: list) {
            h = 31 * h + (coll == null ? 0 : getCollChecksum(coll));
        }
        return h;
    }

    static int getSetCollChecksum(Set<? extends Collection<?>> set) {
        int h = 0;
        for(Collection<?> coll: set) {
            if(coll != null) {
                h += getCollChecksum(coll);
            }
        }
        return h;
    }

    //kopiert aus map + entry
    static int getMapChecksum(Map<?, ?> map) {
        int h = 0;
        for(Map.Entry<?, ?> entry: map.entrySet()) {
            int keyHash = getChecksum(entry.getKey());
            int valueHash = getChecksum(entry.getValue());
            h += keyHash ^ valueHash;
        }
        return h;
    }

    static <K, V> int getMapChecksumWithMapping(
            Map<K, V> map,
            Function<? super K, ?> keyMapper,
            Function<? super V, ?> valueMapper) {
        int h = 0;
        for(Map.Entry<K, V> entry: map.entrySet()) {
            Object mappedKey = keyMapper.apply(entry.getKey());
            Object mappedValue = valueMapper.apply(entry.getValue());
            int keyHash = getChecksum(mappedKey);
            int valueHash = getChecksum(mappedValue);
            h += keyHash ^ valueHash;
        }
        return h;
    }

    static int getMapMapChecksum(Map<?, ? extends Map<?, ?>> map) {
        int h = 0;
        for(Map.Entry<?, ? extends Map<?, ?>> entry: map.entrySet()) {
            int keyCs = getChecksum(entry.getKey());
            int valueCs = getMapChecksum(entry.getValue());
            h += keyCs ^ valueCs;
        }
        return h;
    }

    static <K, V extends Nameable> int getMapChecksumWithNamedValue(Map<K, V> map) {
        return getMapChecksumWithMapping(
                map,
                Function.identity(),
                Nameable.toStringFunction()
        );
    }

    static <K extends Nameable, V extends Nameable> int getNamedMapChecksum(Map<K, V> map) {
        return getMapChecksumWithMapping(
                map,
                Nameable.toStringFunction(),
                Nameable.toStringFunction()
        );
    }

    static int getMapCollChecksum(Map<?, ? extends Collection<?>> map) {
        int h = 0;
        for(Map.Entry<?, ? extends Collection<?>> entry: map.entrySet()) {
            int keyHash = getChecksum(entry.getKey());
            int valueHash = getCollChecksum(entry.getValue());
            h += keyHash ^ valueHash;
        }
        return h;
    }

    static <K> int getMapCollChecksumWithMappedKey(Map<K, ? extends Collection<?>> map, Function<? super K, ?> keyMapper) {
        int h = 0;
        for(Map.Entry<K, ? extends Collection<?>> entry: map.entrySet()) {
            K key = entry.getKey();
            Object mappedKey = keyMapper.apply(key);
            int keyHash = getChecksum(mappedKey);
            int valueHash = getCollChecksum(entry.getValue());
            h += keyHash ^ valueHash;
        }
        return h;
    }

    static int getChecksum(long value) {
        return Long.hashCode(value);
    }

    //nutzt bei IsEquals-Objekten den hashCode
    static int getChecksum(Object value) {
        if(value == null) {
            return NULL_CHECKSUM;
        }
        if(value instanceof ChecksumComparable) {
            return ((ChecksumComparable) value).getChecksum();
        }
        return value.hashCode();
    }

    //nutzt fuer IsEquals-Objekte getChecksum
    static int getChecksum(Object... values) {
        for(int i = 0; i < values.length; i++) {
            Object v = values[i];
            if(v instanceof ChecksumComparable) {
                values[i] = getChecksum(v);
            }
        }
        return Objects.hash(values);
    }

    static int getNameChecksum(Nameable nameable) {
        return nameable == null
                ? NULL_CHECKSUM
                : Objects.hashCode(nameable.getName());
    }

    //=========================
    //printing
    //=========================

    static void logChecksum(Object value) {
        int checksum = ChecksumComparable.getChecksum(value);
        LOGGER0.trace("checksum {} ({}): {}",
                value instanceof Nameable ? ((Nameable) value).getName() : "-",
                value == null ? "null" : value.getClass(),
                checksum);
    }

    static void logChecksums(Object... values) {
        for(Object value: values) {
            logChecksum(value);
        }
    }

    //=========================
    //
    //=========================

    static int unsupportedChecksum(Class<?> c) {
        throw new UnsupportedOperationException("missing checksum: '" + c.getName() + "'");
    }

    int getChecksum();

    default void logChecksum() {
    }
}
