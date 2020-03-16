package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.Check;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicProduct implements Product {

    private ProductGroup group;
    private ProductIdentifier identifier;
    private String name;
    private Set<ProductAttribute> attributes;

    public BasicProduct(
            ProductGroup group,
            ProductIdentifier identifier,
            String name,
            Set<ProductAttribute> attributes) {
        this.group = Check.requireNonNull(group, "group");
        this.identifier = Check.requireNonNull(identifier, "identifier");
        this.name = Check.requireNonNull(name, "name");
        this.attributes = Check.requireNonNull(attributes, "attributes");
    }

    @Override
    public ProductGroup getGroup() {
        return group;
    }

    @Override
    public ProductIdentifier getIdentifier() {
        return identifier;
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
