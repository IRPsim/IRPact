package de.unileipzig.irpact.v2.core.product;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntityBase;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicProduct extends SimulationEntityBase implements Product {

    protected ProductGroup group;
    protected Set<ProductAttribute> attributes;

    public BasicProduct() {
    }

    public BasicProduct(String name, ProductGroup group, Set<ProductAttribute> attributes) {
        setName(name);
        setGroup(group);
        setAttributes(attributes);
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }

    @Override
    public ProductGroup getGroup() {
        return group;
    }

    public void setAttributes(Set<ProductAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<ProductAttribute> getAttributes() {
        return attributes;
    }
}
