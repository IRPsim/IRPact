package de.unileipzig.irpact.core.product.perception;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.product.ProductAttribute;

/**
 * @author Daniel Abitz
 */
public class TrueValueProductAttributePerception implements ProductAttributePerceptionScheme {

    public static final String NAME = TrueValueProductAttributePerception.class.getSimpleName();

    private ProductAttribute attribute;

    public TrueValueProductAttributePerception(ProductAttribute attribute) {
        this.attribute = Check.requireNonNull(attribute, "attribute");
    }

    @Override
    public double calculateCurrentValue() {
        return attribute.getValue();
    }

    @Override
    public void modifyValue(double perceptionValue, double informationWeight) {
        //ignore
    }
}
