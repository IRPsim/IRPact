package de.unileipzig.irpact.core.product.perception;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.product.ProductAttribute;

/**
 * @author Daniel Abitz
 */
public class TrueValueProductAttributePerceptionConfiguration implements ProductAttributPerceptionSchemeConfiguration {

    public static final String NAME = TrueValueProductAttributePerceptionConfiguration.class.getSimpleName();

    private TrueValueProductAttributePerception scheme;
    private ProductAttribute attribute;

    public TrueValueProductAttributePerceptionConfiguration(ProductAttribute attribute) {
        this.attribute = Check.requireNonNull(attribute, "attribute");
    }

    public ProductAttribute getAttribute() {
        return attribute;
    }

    @Override
    public ProductAttributePerceptionScheme newScheme() {
        if(scheme == null) {
            scheme = new TrueValueProductAttributePerception(attribute);
        }
        return scheme;
    }
}
