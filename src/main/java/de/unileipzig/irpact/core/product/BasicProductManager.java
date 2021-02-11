package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class BasicProductManager implements ProductManager {

    private SimulationEnvironment environment;
    private Map<String, ProductGroup> products;

    public BasicProductManager() {
        this(new HashMap<>());
    }

    public BasicProductManager(Map<String, ProductGroup> products) {
        this.products = products;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void validate() throws ValidationException {
    }

    @Override
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

    @Override
    public Stream<Product> streamFixedProducts() {
        return getGroups().stream()
                .flatMap(grp -> grp.getFixedProducts().stream());
    }

    @Override
    public Product getFixedProduct(String name) {
        return streamFixedProducts()
                .filter(p -> Objects.equals(p.getName(), name))
                .findFirst()
                .orElse(null);
    }
}
