package de.unileipzig.irpact.core.spatial;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface SpatialModel {

    String getName();

    double distance(SpatialInformation si0, SpatialInformation si1);

    void sortByDistanceTo(SpatialInformation origin, List<? extends SpatialInformation> list);

    <T extends SpatialInformation> Stream<? extends T> streamKNearest(T origin, Collection<? extends T> list, int k);

    <T extends SpatialInformation> List<T> getKNearest(T origin, Collection<? extends T> list, int k);
}
