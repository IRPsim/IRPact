package de.unileipzig.irpact.v2.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDistributionAttributeGroup<T> extends UnivariateDistributionAttribute, AttributeGroup<T> {

    T derive(double value);
}
