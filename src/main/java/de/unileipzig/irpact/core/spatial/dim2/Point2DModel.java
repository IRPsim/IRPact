package de.unileipzig.irpact.core.spatial.dim2;

import de.unileipzig.irpact.core.spatial.MetricalSpatialModelBase;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public abstract class Point2DModel extends MetricalSpatialModelBase {

    public Point2DModel(String name, CartesianMetric metric) {
        super(name, metric);
    }

    protected static Point2D toPoint2D(SpatialInformation si) {
        if(si instanceof Point2D) {
            return (Point2D) si;
        }
        throw new IllegalArgumentException("requires Point2D");
    }

    protected static Comparator<SpatialInformation> distanceComparator(Point2D origin, CartesianMetric metric) {
        return (si1, si2) -> {
            Point2D p1 = toPoint2D(si1);
            Point2D p2 = toPoint2D(si2);
            double distanceTo1 = Point2D.distance(metric, origin, p1);
            double distanceTo2 = Point2D.distance(metric, origin, p2);
            return Double.compare(distanceTo1, distanceTo2);
        };
    }

    @Override
    public CartesianMetric getMetric() {
        return (CartesianMetric) super.getMetric();
    }

    @Override
    public double distance(SpatialInformation sp0, SpatialInformation sp1) {
        Point2D p0 = toPoint2D(sp0);
        Point2D p1 = toPoint2D(sp1);
        return Point2D.distance(getMetric(), p0, p1);
    }

    @Override
    public void sortByDistanceTo(SpatialInformation origin, List<? extends SpatialInformation> list) {
        Point2D pOrigin = toPoint2D(origin);
        list.sort(distanceComparator(pOrigin, getMetric()));
    }

    @Override
    public <T extends SpatialInformation> Stream<? extends T> streamKNearest(T origin, Collection<? extends T> list, int k) {
        return list.stream()
                .filter(item -> item != origin)
                .sorted(distanceComparator(toPoint2D(origin), getMetric()))
                .limit(k);
    }

    @Override
    public <T extends SpatialInformation> List<T> getKNearest(T origin, Collection<? extends T> list, int k) {
        return streamKNearest(origin, list, k).collect(Collectors.toList());
    }
}
