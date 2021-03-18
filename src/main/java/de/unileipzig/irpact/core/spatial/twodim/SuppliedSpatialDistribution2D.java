package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.attribute.SuppliedSpatialAttribute;
import de.unileipzig.irpact.core.spatial.distribution.ResettableSpatialDistributionBase;
import de.unileipzig.irpact.core.spatial.distribution.SpatialDistribution;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.util.Todo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Todo("aenderung uebernehmen")
public class SuppliedSpatialDistribution2D extends ResettableSpatialDistributionBase {

    protected UnivariateDoubleDistribution xSupplier;
    protected UnivariateDoubleDistribution ySupplier;
    protected Map<String, SuppliedSpatialAttribute<SpatialAttribute<?>>> suppliedAttributes;

    public SuppliedSpatialDistribution2D() {
        this(new LinkedHashMap<>());
    }

    public SuppliedSpatialDistribution2D(Map<String, SuppliedSpatialAttribute<SpatialAttribute<?>>> suppliedAttributes) {
        this.suppliedAttributes = suppliedAttributes;
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
        BasicPoint2D p = new BasicPoint2D(x, y);
        for(SuppliedSpatialAttribute<SpatialAttribute<?>> suppliedAttribute: suppliedAttributes.values()) {
            SpatialAttribute<?> attr = suppliedAttribute.derive();
            p.addAttribute(attr);
        }
        return p;
    }
}
