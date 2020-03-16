package de.unileipzig.irpact.core.spatial;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface SpatialInformation {

    double distance(SpatialInformation other);

    void sortByDistanceToThis(List<? extends SpatialInformation> list);

    <T extends SpatialInformation> Stream<? extends T> streamKNearest(Collection<? extends T> list, int k);

    <T extends SpatialInformation> List<T> getKNearest(Collection<? extends T> list, int k);
}
