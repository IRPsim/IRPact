package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.simulation.SimulationEntityBase;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroup extends SimulationEntityBase implements ProductGroup {

    protected Map<String, ProductGroupAttribute> attributes;
    protected Map<String, Product> products;

    public BasicProductGroup() {
        this(new HashMap<>(), new HashMap<>());
    }

    public BasicProductGroup(
            Map<String, ProductGroupAttribute> attributes,
            Map<String, Product> products) {
        this.attributes = attributes;
        this.products = products;
    }

    @Override
    public Collection<Product> getProducts() {
        return products.values();
    }

    @Override
    public Product getProduct(String name) {
        return products.get(name);
    }

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public void addAllAttributes(ProductGroupAttribute... attributes) {
        for(ProductGroupAttribute attribute: attributes) {
            addAttribute(attribute);
        }
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
    public boolean hasProduct(String name) {
        return products.containsKey(name);
    }

    @Override
    public void addProduct(Product product) {
        if(hasProduct(product.getName())) {
            throw new IllegalArgumentException("name '" + product.getName() + "' already exists");
        }
        if(product.getGroup() != this) {
            throw new IllegalArgumentException("group mismatch");
        }
        if(product.getAttributes().size() != attributes.size()) {
            throw new IllegalArgumentException("number of ProductGroupAttribute mismatch: " + product.getAttributes().size() + " != " + attributes.size());
        }
        for(ProductAttribute pa: product.getAttributes()) {
            ProductGroupAttribute pga = attributes.get(pa.getGroup().getName());
            if(pga != pa.getGroup()) {
                throw new IllegalArgumentException("ProductGroupAttribute mismatch: " + pga.getName());
            }
        }
        products.put(product.getName(), product);
    }

    @Override
    public Product derive() {
        return new BasicProduct(
                deriveName(),
                this,
                deriveAttributes()
        );
    }

    @Override
    public Product deriveAndAdd() {
        Product product = derive();
        products.put(product.getName(), product);
        return product;
    }
}
