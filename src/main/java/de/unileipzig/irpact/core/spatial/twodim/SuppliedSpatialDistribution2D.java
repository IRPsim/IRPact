package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.spatial.ResettableSpatialDistributionBase;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public class SuppliedSpatialDistribution2D extends ResettableSpatialDistributionBase {

    protected UnivariateDoubleDistribution xSupplier;
    protected UnivariateDoubleDistribution ySupplier;

    public SuppliedSpatialDistribution2D() {
    }

    public UnivariateDoubleDistribution getXSupplier() {
        return xSupplier;
    }

    public void setXSupplier(UnivariateDoubleDistribution xSupplier) {
        this.xSupplier = xSupplier;
    }

    public UnivariateDoubleDistribution getYSupplier() {
        return ySupplier;
    }

    public void setYSupplier(UnivariateDoubleDistribution ySupplier) {
        this.ySupplier = ySupplier;
    }

    @Override
    public boolean isShareble(SpatialDistribution target) {
        return false;
    }

    @Override
    public void addComplexDataTo(SpatialDistribution target) {
        //ignore
    }

    @Override
    public void reset() {
    }

    @Override
    public void initalize() {
    }

    @Override
    public void call() {
    }

    @Override
    public void call(int times) {
    }

    @Override
    public SpatialInformation drawValue() {
        double x = xSupplier.drawDoubleValue();
        double y = ySupplier.drawDoubleValue();
        numberOfCalls++;
        return new BasicPoint2D(x, y);
    }
}
