package de.unileipzig.irpact.v2.core.product;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntityBase;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroup extends SimulationEntityBase implements ProductGroup {

    protected Set<ProductGroupAttribute> attributes;
    protected Set<Product> products;
    protected Set<Product> fixedProducts;

    public BasicProductGroup() {
        this(new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public BasicProductGroup(
            Set<ProductGroupAttribute> attributes,
            Set<Product> products,
            Set<Product> fixedProducts) {
        this.attributes = attributes;
        this.products = products;
        this.fixedProducts = fixedProducts;
    }

    @Override
    public Set<Product> getProducts() {
        return products;
    }

    @Override
    public Set<Product> getFixedProducts() {
        return fixedProducts;
    }

    public boolean addAttribute(ProductGroupAttribute attribute) {
        return attributes.add(attribute);
    }

    public void setAttributes(Set<ProductGroupAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<ProductGroupAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public ProductGroupAttribute getAttribute(String name) {
        for(ProductGroupAttribute attr: attributes) {
            if(Objects.equals(attr.getName(), name)) {
                return attr;
            }
        }
        return null;
    }

    public String deriveName() {
        return getName();
    }

    public Set<ProductAttribute> deriveAttributes() {
        Set<ProductAttribute> paSet = new HashSet<>();
        for(ProductGroupAttribute pga: getAttributes()) {
            ProductAttribute pa = pga.derive();
            paSet.add(pa);
        }
        return paSet;
    }

    @Override
    public boolean register(Product product) {
        if(product.getGroup() != this) {
            throw new IllegalArgumentException();
        }
        return products.add(product);
    }

    @Override
    public boolean registerFixed(Product product) {
        if(product.getGroup() != this) {
            throw new IllegalArgumentException();
        }
        return fixedProducts.add(product);
    }

    @Override
    public Product derive() {
        return new BasicProduct(
                deriveName(),
                this,
                deriveAttributes()
        );
    }
}
