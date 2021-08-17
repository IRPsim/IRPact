package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.commons.graph.CachedDirectedMultiGraph;
import de.unileipzig.irpact.commons.util.CollectionUtil;

/**
 * @author Daniel Abitz
 */
public enum SupportedGraphStructure implements ChecksumComparable {
    UNKNOWN(0) {
        @Override
        public <V, E, T> DirectedMultiGraph<V, E, T> newInstance() {
            throw new UnsupportedOperationException("unknown structure");
        }
    },
    CACHED_DIRECTED_MULTI_GRAPH(3) {
        @Override
        public <V, E, T> CachedDirectedMultiGraph<V, E, T> newInstance() {
            return new CachedDirectedMultiGraph<>(
                    CollectionUtil.linkedHashMapSupplier(),
                    CollectionUtil.linkedHashMapSupplier(),
                    CollectionUtil.linkedHashMapSupplier(),
                    CollectionUtil.linkedHashMapFunction()
            );
        }
    },
    CONCURRENT_CACHED_DIRECTED_MULTI_GRAPH(4) {
        @Override
        public <V, E, T> CachedDirectedMultiGraph<V, E, T> newInstance() {
            return new CachedDirectedMultiGraph<>(
                    CollectionUtil.concurrentLinkedHashMapSupplier(),
                    CollectionUtil.concurrentLinkedHashMapSupplier(),
                    CollectionUtil.concurrentLinkedHashMapSupplier(),
                    CollectionUtil.concurrentLinkedHashMapFunction()
            );
        }
    };

    private final int ID;

    SupportedGraphStructure(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int getChecksum() {
        return ID;
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }

    public abstract <V, E, T> DirectedMultiGraph<V, E, T> newInstance();

    public static SupportedGraphStructure get(int id) {
        for(SupportedGraphStructure v: values()) {
            if(v.getID() == id) {
                return v;
            }
        }
        return UNKNOWN;
    }

    public static SupportedGraphStructure getDefault() {
        return CONCURRENT_CACHED_DIRECTED_MULTI_GRAPH;
    }
}
