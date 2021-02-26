package de.unileipzig.irpact.commons;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.function.Function;

/**
 * For equals-checks without using {@link #equals(Object)}. Used to validate the restore process.
 *
 * @author Daniel Abitz
 */
public interface IsEquals {

    IRPLogger LOGGER = IRPLogging.getLogger(IsEquals.class);

    int NULL_HASH = 0;
    int DEFAULT_HASH = 31;

    static boolean isEquals(Object a, Object b) {
        //es muss nur a getestet werden, da die isEquals nur bei Gleichheit der Klasse anderst arbeiten
        if(a instanceof IsEquals) {
            return ((IsEquals) a).isEquals(b);
        } else {
            return Objects.equals(a, b);
        }
    }

    static boolean isEquals(
            Object a0, Object b0,
            Object a1, Object b1) {
        return isEquals(a0, b0)
                && isEquals(a1, b1);
    }

    static boolean isEquals(
            Object a0, Object b0,
            Object a1, Object b1,
            Object a2, Object b2) {
        return isEquals(a0, b0)
                && isEquals(a1, b1)
                && isEquals(a2, b2);
    }

    static boolean isEquals(
            Object a0, Object b0,
            Object a1, Object b1,
            Object a2, Object b2,
            Object a3, Object b3) {
        return isEquals(a0, b0)
                && isEquals(a1, b1)
                && isEquals(a2, b2)
                && isEquals(a3, b3);
    }

    static int getCollHashCode(Collection<?> coll) {
        if(coll instanceof Set) {
            return getSetHashCode((Set<?>) coll);
        }
        if(coll instanceof List) {
            return getListHashCode((List<?>) coll);
        }
        //fallback
        int h = 0;
        for(Object e: coll) {
            if(e != null) {
                h += getHashCode(e);
            }
        }
        return h;
    }

    //copy from ArrayList
    static int getListHashCode(List<?> coll) {
        int h = 1;
        for(Object e: coll) {
            h = 31 * h + (e == null ? 0 : getHashCode(e));
        }
        return h;
    }

    //copy from set
    static int getSetHashCode(Set<?> coll) {
        int h = 0;
        for(Object e: coll) {
            if(e != null) {
                h += getHashCode(e);
            }
        }
        return h;
    }

    @SuppressWarnings("unchecked")
    static int getCollCollHashCode(Collection<? extends Collection<?>> coll) {
        if(coll instanceof Set) {
            return getSetCollHashCode((Set<? extends Collection<?>>) coll);
        }
        if(coll instanceof List) {
            return getListCollHashCode((List<? extends Collection<?>>) coll);
        }
        //fallback
        int h = 0;
        for(Collection<?> c: coll) {
            if(c != null) {
                h += getCollHashCode(c);
            }
        }
        return h;
    }

    static int getListCollHashCode(List<? extends Collection<?>> list) {
        int h = 1;
        for(Collection<?> coll: list) {
            h = 31 * h + (coll == null ? 0 : getCollHashCode(coll));
        }
        return h;
    }

    static int getSetCollHashCode(Set<? extends Collection<?>> set) {
        int h = 0;
        for(Collection<?> coll: set) {
            if(coll != null) {
                h += getCollHashCode(coll);
            }
        }
        return h;
    }

    //kopiert aus map + entry
    static int getMapHashCode(Map<?, ?> map) {
        int h = 0;
        for(Map.Entry<?, ?> entry: map.entrySet()) {
            int keyHash = getHashCode(entry.getKey());
            int valueHash = getHashCode(entry.getValue());
            h += keyHash ^ valueHash;
        }
        return h;
    }

    static int getMapCollHashCode(Map<?, ? extends Collection<?>> map) {
        int h = 0;
        for(Map.Entry<?, ? extends Collection<?>> entry: map.entrySet()) {
            int keyHash = getHashCode(entry.getKey());
            int valueHash = getCollHashCode(entry.getValue());
            h += keyHash ^ valueHash;
        }
        return h;
    }

    static <K> int getMapCollHashCodeWithMappedKey(Map<K, ? extends Collection<?>> map, Function<? super K, ?> keyMapper) {
        int h = 0;
        for(Map.Entry<K, ? extends Collection<?>> entry: map.entrySet()) {
            K key = entry.getKey();
            Object mappedKey = keyMapper.apply(key);
            int keyHash = getHashCode(mappedKey);
            int valueHash = getCollHashCode(entry.getValue());
            h += keyHash ^ valueHash;
        }
        return h;
    }

    //nutzt bei IsEquals-Objekten den getHashCode
    static int getHashCode(Object value) {
        if(value == null) {
            return NULL_HASH;
        }
        if(value instanceof IsEquals) {
            return ((IsEquals) value).getHashCode();
        }
        return value.hashCode();
    }

    //nutzt fuer IsEquals-Objekte den getHashCode
    static int getHashCode(Object... values) {
        for(int i = 0; i < values.length; i++) {
            Object v = values[i];
            if(v instanceof IsEquals) {
                values[i] = getHashCode(v);
            }
        }
        return Objects.hash(values);
    }

    //=========================
    //
    //=========================

    default int getHashCode() {
        LOGGER.warn("called default 'getHashCode': '{}'", getClass().getName());
        return System.identityHashCode(this);
    }

    default boolean isEquals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(obj.getClass() == getClass()) return isEqualsSameClass(obj);
        return false;
    }

    default boolean isEqualsSameClass(Object obj) {
        if(obj == this) {
            return true;
        }
        IsEquals other = (IsEquals) obj;
        return getHashCode() == other.getHashCode();
    }
}
