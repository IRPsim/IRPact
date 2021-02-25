package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

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

    public boolean has(String name) {
        return products.containsKey(name);
    }

    public void add(ProductGroup group) {
        products.put(group.getName(), group);
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
