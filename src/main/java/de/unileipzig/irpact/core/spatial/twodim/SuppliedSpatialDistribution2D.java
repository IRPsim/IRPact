package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public class SuppliedSpatialDistribution2D extends NameableBase implements SpatialDistribution {

    protected UnivariateDoubleDistribution xSupplier;
    protected UnivariateDoubleDistribution ySupplier;

    public SuppliedSpatialDistribution2D() {
    }

    public void setXSupplier(UnivariateDoubleDistribution xSupplier) {
        this.xSupplier = xSupplier;
    }

    public void setYSupplier(UnivariateDoubleDistribution ySupplier) {
        this.ySupplier = ySupplier;
    }

    @Override
    public SpatialInformation drawValue() {
        double x = xSupplier.drawDoubleValue();
        double y = ySupplier.drawDoubleValue();
        return new BasicPoint2D(x, y);
    }
}
