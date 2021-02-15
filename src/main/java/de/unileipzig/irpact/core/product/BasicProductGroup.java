package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.simulation.SimulationEntityBase;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroup extends SimulationEntityBase implements ProductGroup {

    protected Map<String, ProductGroupAttribute> attributes;
    protected Set<Product> products;
    protected Set<Product> fixedProducts;

    public BasicProductGroup() {
        this(new HashMap<>(), new HashSet<>(), new HashSet<>());
    }

    public BasicProductGroup(
            Map<String, ProductGroupAttribute> attributes,
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

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public void addAttribute(ProductGroupAttribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }

    @Override
    public Collection<ProductGroupAttribute> getAttributes() {
        return attributes.values();
    }

    @Override
    public ProductGroupAttribute getAttribute(String name) {
        return attributes.get(name);
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
