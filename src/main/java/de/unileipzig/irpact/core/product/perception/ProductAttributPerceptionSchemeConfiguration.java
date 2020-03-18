package de.unileipzig.irpact.core.product.perception;

import de.unileipzig.irpact.core.perception.PerceptionSchemeConfiguration;

/**
 * @author Daniel Abitz
 */
public interface ProductAttributPerceptionSchemeConfiguration extends PerceptionSchemeConfiguration {

    @Override
    ProductAttributePerceptionScheme newScheme();
}
