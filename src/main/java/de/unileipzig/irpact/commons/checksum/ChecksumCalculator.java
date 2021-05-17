package de.unileipzig.irpact.commons.checksum;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public abstract class ChecksumCalculator {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ChecksumCalculator.class);

    public static final int NUll_CHECKSUM = 0;
    public static final int DEFAULT_NONNULL_CHECKSUM = 31;
    private static final int PRIM = 31;

    public ChecksumCalculator() {
    }

    //=========================
    //util
    //=========================

    public static int getDefaultChecksum(int modifier) {
        return PRIM + modifier;
    }

    //=========================
    //logging
    //=========================

    private static String printClass(Object obj) {
        if(obj == null) {
            return "null";
        } else {
            return obj.getClass().getSimpleName();
        }
    }

    private String printChecksum(Object obj) {
        return Integer.toHexString(getChecksum(obj));
    }

    public void log(ChecksumInfo info) {
        LOGGER.info("[checksum] '{}' ({}): {}",
                info.getText(),
                printClass(info.getValue()),
                printChecksum(info.getValue())
        );
    }

    public void log(ChecksumInfo... infos) {
        log(Arrays.asList(infos));
    }

    public void log(Collection<? extends ChecksumInfo> infos) {
        for(ChecksumInfo info: infos) {
            log(info);
        }
    }

    //=========================
    //general
    //=========================

    public int getChecksum(long value) {
        return Long.hashCode(value);
    }

    public abstract int getChecksum(Object value);

    public int getChecksum(Object... values) {
        if(values == null) {
            return NUll_CHECKSUM;
        }
        int cs = 1;
        for(Object obj: values) {
            cs = PRIM * cs + getChecksum(obj);
        }
        return cs;
    }

    public int getNamedChecksum(Nameable nameable) {
        if(nameable == null) {
            return NUll_CHECKSUM;
        }
        if(nameable.getName() == null) {
            return NUll_CHECKSUM;
        }
        return nameable.getName().hashCode();
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
}
