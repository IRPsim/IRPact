package de.unileipzig.irpact.core.spatial;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class Point2D implements SpatialInformation {

    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(Point2D other) {
        if(other == this) {
            return 0.0;
        }
        double x2 = (x - other.x) * (x - other.x);
        double y2 = (y - other.y) * (y - other.y);
        return Math.sqrt(x2 + y2);
    }

    @Override
    public double distance(SpatialInformation other) {
        if(other instanceof Point2D) {
            return distance((Point2D) other);
        }
        throw new IllegalArgumentException("no Point2D");
    }

    private Comparator<SpatialInformation> distanceToThis = (info1, info2) -> {
        double distanceTo1 = distance(info1);
        double distanceTo2 = distance(info2);
        return Double.compare(distanceTo1, distanceTo2);
    };

    @Override
    public void sortByDistanceToThis(List<? extends SpatialInformation> list) {
        list.sort(distanceToThis);
    }

    @Override
    public <T extends SpatialInformation> Stream<? extends T> streamKNearest(Collection<? extends T> list, int k) {
        return list.stream()
                .sorted(distanceToThis)
                .limit(k);
    }

    @Override
    public <T extends SpatialInformation> List<T> getKNearest(Collection<? extends T> list, int k) {
        return streamKNearest(list, k).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof Point2D) {
            Point2D other = (Point2D) obj;
            return x == other.x
                    && y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point2D[" + x + "," + y + "]";
    }
}
