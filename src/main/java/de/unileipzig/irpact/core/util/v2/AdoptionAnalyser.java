package de.unileipzig.irpact.core.util.v2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface AdoptionAnalyser {

    void add(AdoptionInfo info);

    default void addAll(Iterable<? extends AdoptionInfo> infos) {
        for(AdoptionInfo info: infos) {
            add(info);
        }
    }

    default void addAll(Iterator<? extends AdoptionInfo> infos) {
        while(infos.hasNext()) {
            add(infos.next());
        }
    }

    default void addAll(Stream<? extends AdoptionInfo> infos) {
        infos.forEach(this::add);
    }

    VarCollection getData();
}
