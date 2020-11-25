package de.unileipzig.irpact.core.product.perception;

import de.unileipzig.irpact.v2.commons.Check;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.product.exception.MissingProductAttributePerceptionSchemeException;

import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Deprecated
public class MeanProductPerception implements ProductPerceptionScheme {

    public static final String NAME = MeanProductPerception.class.getSimpleName();

    private Product product;
    private Map<ProductAttribute, ProductAttributePerceptionScheme> schemes;

    public MeanProductPerception(
            Product product,
            Map<ProductAttribute, ProductAttributePerceptionScheme> schemes) {
        this.product = Check.requireNonNull(product, "product");
        this.schemes = Check.requireNonNull(schemes, "schemes");
    }

    @Override
    public double getPerception() {
        Set<ProductAttribute> attributes = product.getAttributes();
        double sum = 0.0;
        for(ProductAttribute attribute: attributes) {
            ProductAttributePerceptionScheme scheme = schemes.get(attribute);
            if(scheme == null) {
                throw new MissingProductAttributePerceptionSchemeException(product.getName());
            }
            sum += scheme.calculateCurrentValue();
        }
        return sum / attributes.size();
    }
}
