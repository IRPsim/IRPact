package de.unileipzig.irpact.start.hardcodeddemo;

import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class AdaptedProducts {

    private Map<Integer, Set<Product>> map = new LinkedHashMap<>();

    public AdaptedProducts() {
    }

    public boolean isAdapted(Product product) {
        for(Set<Product> set: map.values()) {
            if(set.contains(product)) {
                return true;
            }
        }
        return false;
    }

    public void adapt(int year, Product product) {
        Set<Product> set = map.computeIfAbsent(year, _year -> new LinkedHashSet<>());
        set.add(product);
    }

    public Map<Integer, Set<Product>> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
