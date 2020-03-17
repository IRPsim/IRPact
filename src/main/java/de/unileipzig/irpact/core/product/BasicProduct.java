package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.Check;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicProduct implements Product {

    private ProductGroup group;
    private String name;
    private Set<ProductAttribute> attributes;

    public BasicProduct(
            ProductGroup group,
            String name,
            Set<ProductAttribute> attributes) {
        this.group = Check.requireNonNull(group, "group");
        this.name = Check.requireNonNull(name, "name");
        this.attributes = Check.requireNonNull(attributes, "attributes");
    }

    @Override
    public ProductGroup getGroup() {
        return group;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<ProductAttribute> getAttributes() {
        return attributes;
    }
}
