package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.Util;
import de.unileipzig.irpact.commons.geo.GeoMath;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public enum Metric2D implements Metric {
    EUCLIDEAN(0) {
        @Override
        public double distance(Point2D from, Point2D to) {
            if(from == to) return 0.0;
            double d2 = EUCLIDEAN2.distance(from, to);
            return Math.sqrt(d2);
        }
    },
    MANHATTEN(1) {
        @Override
        public double distance(Point2D from, Point2D to) {
            if(from == to) return 0.0;
            double mx = Math.abs(from.getX() - to.getX());
            double my = Math.abs(from.getY() - to.getY());
            return mx + my;
        }
    },
    MAXIMUM(2) {
        @Override
        public double distance(Point2D from, Point2D to) {
            if(from == to) return 0.0;
            double mx = Math.abs(from.getX() - to.getX());
            double my = Math.abs(from.getY() - to.getY());
            return Math.max(mx, my);
        }
    },
    EUCLIDEAN2(3) {
        @Override
        public double distance(Point2D from, Point2D to) {
            if(from == to) return 0.0;
            double x2 = (from.getX() - to.getX()) * (from.getX() - to.getX());
            double y2 = (from.getY() - to.getY()) * (from.getY() - to.getY());
            return x2 + y2;
        }
    },
    HAVERSINE_M(4) {
        @Override
        public double distance(Point2D from, Point2D to) {
            if(from == to) return 0.0;
            return GeoMath.haversineDistance(
                    from.getX(), from.getY(),
                    to.getX(), to.getY(),
                    GeoMath.EARTH_RADIUS_METER
            );
        }
    },
    HAVERSINE_KM(5) {
        @Override
        public double distance(Point2D from, Point2D to) {
            if(from == to) return 0.0;
            return GeoMath.haversineDistance(
                    from.getX(), from.getY(),
                    to.getX(), to.getY(),
                    GeoMath.EARTH_RADIUS_KILOMETER
            );
        }
    };

    private static Point2D cast(SpatialInformation information) {
        if(information instanceof Point2D) {
            return (Point2D) information;
        }
        throw new IllegalArgumentException("no Point2D: " + Util.printClass(information));
    }

    public static Metric2D get(int id) {
        for(Metric2D m: values()) {
            if(id == m.id()) {
                return m;
            }
        }
        throw new IllegalArgumentException("unknown id: " + id);
    }

    private final int ID;

    Metric2D(int id) {
        ID = id;
    }

    public int id() {
        return ID;
    }

    public abstract double distance(Point2D from, Point2D to);

    @Override
    public double distance(SpatialInformation from, SpatialInformation to) {
        if(from == to) return 0.0;
        Point2D f = Metric2D.cast(from);
        Point2D t = Metric2D.cast(to);
        return distance(f, t);
    }
}
