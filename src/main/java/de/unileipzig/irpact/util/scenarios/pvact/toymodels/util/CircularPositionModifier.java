package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.commons.geo.GeoMath;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Point2D;

import java.util.List;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
public class CircularPositionModifier implements DataModifier {

    protected Metric2D metric = Metric2D.HAVERSINE_KM;
    protected double radius = GeoMath.EARTH_RADIUS_KILOMETER;
    protected Point2D reference;
    protected Rnd rnd;
    protected double minDist = 0;
    protected double maxDist = 0;

    public CircularPositionModifier(long seed) {
        setRnd(new Rnd(seed));
        setReference(new BasicPoint2D(12.398164822384926, 51.34826339143966));
    }

    public void setReference(Point2D reference) {
        this.reference = reference;
    }

    public Point2D getReference() {
        return reference;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public void setMinDist(double minDist) {
        this.minDist = minDist;
    }

    public void setMaxDist(double maxDist) {
        this.maxDist = maxDist;
    }

    public void setDist(double maxDist, double minDist) {
        setMinDist(minDist);
        setMaxDist(maxDist);
    }

    public Point2D nextPoint() {
        return nextPoint(maxDist, minDist);
    }

    public Point2D nextPoint(double maxDist) {
        return GeoMath.randomCircularLongitudeLatitude(
                reference.getX(), reference.getY(),
                maxDist, 0,
                radius,
                rnd,
                BasicPoint2D::new
        );
    }

    public Point2D nextPoint(double maxDist, double minDist) {
        return GeoMath.randomCircularLongitudeLatitude(
                reference.getX(), reference.getY(),
                maxDist, minDist,
                radius,
                rnd,
                BasicPoint2D::new
        );
    }

    @Override
    public List<SpatialAttribute> modify(List<SpatialAttribute> row) {
        Point2D p = nextPoint();
        setXY(row, p.getX(), p.getY());
        return row;
    }
}
