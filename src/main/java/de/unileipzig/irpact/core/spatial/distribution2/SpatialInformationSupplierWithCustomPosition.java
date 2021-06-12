package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public class SpatialInformationSupplierWithCustomPosition extends AbstractSpatialInformationSupplier {

    protected UnivariateDoubleDistribution xPosition;
    protected UnivariateDoubleDistribution yPosition;
    protected String xKey;
    protected String yKey;

    public SpatialInformationSupplierWithCustomPosition() {
    }

    public void setXPosition(UnivariateDoubleDistribution xPosition) {
        this.xPosition = xPosition;
    }

    public UnivariateDoubleDistribution getXPosition() {
        return xPosition;
    }

    public void setYPosition(UnivariateDoubleDistribution yPosition) {
        this.yPosition = yPosition;
    }

    public UnivariateDoubleDistribution getYPosition() {
        return yPosition;
    }

    public void setXKey(String xKey) {
        this.xKey = xKey;
    }

    public String getXKey() {
        return xKey;
    }

    public void setYKey(String yKey) {
        this.yKey = yKey;
    }

    public String getYKey() {
        return yKey;
    }

    @Override
    protected void updateInformation(SpatialInformation drawnValue) {
        super.updateInformation(drawnValue);
        double x = xPosition.drawDoubleValue();
        double y = yPosition.drawDoubleValue();
        updateAttribute(drawnValue, xKey, x);
        updateAttribute(drawnValue, yKey, y);
    }

    protected static void updateAttribute(SpatialInformation information, String key, double value) {
        if(information.hasAttribute(key)) {
            Attribute xAttr = information.getAttribute(key);
            xAttr.asValueAttribute().setDoubleValue(value);
        } else {
            BasicSpatialDoubleAttribute xAttr = new BasicSpatialDoubleAttribute(key, value);
            information.addAttribute(xAttr);
        }
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                super.getChecksum(),
                xKey,
                yKey,
                xPosition,
                yPosition
        );
    }
}
