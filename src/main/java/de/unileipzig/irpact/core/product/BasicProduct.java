package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicProduct extends SimulationEntityBase implements Product {

    protected ProductGroup group;
    protected Map<String, ProductAttribute> attributes;
    protected boolean fixed = false;

    public BasicProduct() {
        this(new LinkedHashMap<>());
    }

    public BasicProduct(Map<String, ProductAttribute> attributes) {
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

    public void addAllAttributes(ProductAttribute... attributes) {
        for(ProductAttribute attribute: attributes) {
            addAttribute(attribute);
        }
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public void addAttribute(ProductAttribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }

    public void setAttributes(Set<ProductAttribute> attributes) {
        for(ProductAttribute attribute: attributes) {
            addAttribute(attribute);
        }
    }

    @Override
    public Collection<ProductAttribute> getAttributes() {
        return attributes.values();
    }

    @Override
    public ProductAttribute getAttribute(String name) {
        return attributes.get(name);
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    @Override
    public boolean isFixed() {
        return fixed;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                group.getName(),
                ChecksumComparable.getCollChecksum(getAttributes())
        );
    }
}
