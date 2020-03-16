package de.unileipzig.irpact.jadex.examples.deprecated.old.sharedData;

import java.util.HashSet;
import java.util.Set;

public class Store {

    protected Set<String> products = new HashSet<>();

    public Store() {
    }

    public void addProduct(String product) {
        products.add(product);
    }

    public boolean hasProduct(String product) {
        return products.contains(product);
    }

    public boolean buyProduct(String product) {
        return hasProduct(product);
    }

    public int countProducts() {
        return products.size();
    }

    @Override
    public String toString() {
        return products.toString();
    }
}
