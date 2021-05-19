package de.unileipzig.irpact.core.spatial.filter;

/**
 * @author Daniel Abitz
 */
public class MaxDistanceSpatialInformationFilter extends SpatialInformationMetricFilter {

    protected double max;
    protected boolean inclusive = false;

    public MaxDistanceSpatialInformationFilter() {
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMax() {
        return max;
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
                ? distance <= max
                : distance < max;
    }
}
