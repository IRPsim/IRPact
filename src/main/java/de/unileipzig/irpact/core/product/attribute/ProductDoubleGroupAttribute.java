package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.derivable.NamendDependentDoubleDerivable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public interface ProductDoubleGroupAttribute
        extends ProductValueGroupAttribute<Number>, UnivariateDoubleDistribution, NamendDependentDoubleDerivable<ProductAttribute> {

    @Override
    default boolean isProductDoubleGroupAttribute() {
        return true;
    }

    @Override
    default ProductDoubleGroupAttribute asProductDoubleGroupAttribute() {
        return this;
    }

    @Override
    ProductDoubleAttribute derive();

    @Override
    ProductDoubleAttribute derive(double value);

    @Override
    ProductDoubleAttribute derive(Number input);

    @Override
    ProductDoubleAttribute derive(String str, Number value);

    @Override
    ProductDoubleAttribute derive(String name, double value);

    UnivariateDoubleDistribution getDistribution();
}
