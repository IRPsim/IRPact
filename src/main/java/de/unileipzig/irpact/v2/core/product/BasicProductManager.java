package de.unileipzig.irpact.v2.core.product;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicProductManager implements ProductManager {

    private Map<String, ProductGroup> products;

    public BasicProductManager() {
        this(new HashMap<>());
    }

    public BasicProductManager(Map<String, ProductGroup> products) {
        this.products = products;
    }

    public boolean add(ProductGroup group) {
        if(products.containsKey(group.getName())) {
            return false;
        }
        products.put(group.getName(), group);
        return true;
    }

    @Override
    public Collection<ProductGroup> getGroups() {
        return products.values();
    }

    @Override
    public ProductGroup getGroup(String name) {
        return products.get(name);
    }
}
