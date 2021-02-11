package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.simulation.SimulationEntityBase;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicProduct extends SimulationEntityBase implements Product {

    protected ProductGroup group;
    protected Set<ProductAttribute> attributes;
    protected boolean fixed = false;

    public BasicProduct() {
        this(new LinkedHashSet<>());
    }

    public BasicProduct(Set<ProductAttribute> attributes) {
        this.attributes = attributes;
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

    public boolean addAttribute(ProductAttribute attribute) {
        return attributes.add(attribute);
    }

    public void setAttributes(Set<ProductAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<ProductAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public ProductAttribute getAttribute(String name) {
        for(ProductAttribute attr: attributes) {
            if(Objects.equals(attr.getName(), name)) {
                return attr;
            }
        }
        return null;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    @Override
    public boolean isFixed() {
        return fixed;
    }
}
