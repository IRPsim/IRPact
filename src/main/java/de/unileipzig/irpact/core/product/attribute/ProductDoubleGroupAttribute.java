package de.unileipzig.irpact.core.product.attribute;

/**
 * @author Daniel Abitz
 */
public interface ProductDoubleGroupAttribute extends ProductValueGroupAttribute<Number> {

    @Override
    ProductDoubleAttribute derive();

    ProductDoubleAttribute derive(double value);

    ProductDoubleAttribute derive(String name, double value);
}
