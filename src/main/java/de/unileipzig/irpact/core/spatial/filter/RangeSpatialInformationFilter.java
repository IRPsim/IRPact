package de.unileipzig.irpact.core.spatial.filter;

/**
 * @author Daniel Abitz
 */
public class RangeSpatialInformationFilter extends SpatialInformationMetricFilter {

    protected double min;
    protected boolean minInclusive = true;
    protected double max;
    protected boolean maxInclusive = false;

    public RangeSpatialInformationFilter() {
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMin() {
        return min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMax() {
        return max;
    }

    public void setMinInclusive(boolean minInclusive) {
        this.minInclusive = minInclusive;
    }

    public boolean isMinInclusive() {
        return minInclusive;
    }

    public void setMaxInclusive(boolean maxInclusive) {
        this.maxInclusive = maxInclusive;
    }

    public boolean isMaxInclusive() {
        return maxInclusive;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    protected boolean test(double distance) {
        if(minInclusive ? distance < min : distance <= min) {
            return false;
        }
        if(maxInclusive ? max < distance : max <= distance) {
            return false;
        }
        return true;
    }
}
