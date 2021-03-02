package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroup extends SimulationEntityBase implements ProductGroup {

    protected int nextProductId = 0;
    protected Map<String, ProductGroupAttribute> attributes;
    protected Map<String, Product> products;

    public BasicProductGroup() {
        this(new LinkedHashMap<>(), new LinkedHashMap<>());
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

    public boolean hasGroupAttribute(ProductGroupAttribute attribute) {
        return hasGroupAttribute(attribute.getName());
    }

    public boolean hasGroupAttribute(String name) {
        return attributes.containsKey(name);
    }

    public void addAllGroupAttributes(ProductGroupAttribute... attributes) {
        for(ProductGroupAttribute attribute: attributes) {
            addGroupAttribute(attribute);
        }
    }

    public void addGroupAttribute(ProductGroupAttribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }

    @Override
    public Collection<ProductGroupAttribute> getGroupAttributes() {
        return attributes.values();
    }

    @Override
    public ProductGroupAttribute getGroupAttribute(String name) {
        return attributes.get(name);
    }

    protected synchronized int nextId() {
        int nextId = nextProductId;
        nextProductId++;
        return nextId;
    }

    public String deriveName() {
        return getName() + "_" + nextId();
    }

    public Set<ProductAttribute> deriveAttributes() {
        Set<ProductAttribute> paSet = new LinkedHashSet<>();
        for(ProductGroupAttribute pga: getGroupAttributes()) {
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
        BasicProduct product = new BasicProduct(
                deriveName(),
                this,
                deriveAttributes()
        );
        product.setEnvironment(getEnvironment());
        return product;
    }

    @Override
    public Product deriveAndAdd() {
        Product product = derive();
        products.put(product.getName(), product);
        return product;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                nextProductId,
                IsEquals.getCollHashCode(getProducts()),
                IsEquals.getCollHashCode(getGroupAttributes())
        );
    }
}
