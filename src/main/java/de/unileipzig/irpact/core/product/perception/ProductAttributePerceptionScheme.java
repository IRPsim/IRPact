package de.unileipzig.irpact.core.product.perception;

import de.unileipzig.irpact.core.perception.PerceptionScheme;

/**
 * @author Daniel Abitz
 */
public interface ProductAttributePerceptionScheme extends PerceptionScheme {

    double calculateCurrentValue();

    void modifyValue(double perceptionValue, double informationWeight);
}
