package de.unileipzig.irpact.core.spatial.filter;

/**
 * @author Daniel Abitz
 */
public class MinDistanceSpatialInformationFilter extends SpatialInformationMetricFilter {

    protected double min;
    protected boolean inclusive = false;

    public MinDistanceSpatialInformationFilter() {
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMin() {
        return min;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    @Override
    protected boolean test(double distance) {
        return inclusive
                ? min <= distance
                : min < distance;
    }
}
