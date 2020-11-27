package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ProductGroup extends SimulationEntity {

    Set<Product> getProducts();

    Set<Product> getFixedProducts();

    Set<ProductGroupAttribute> getAttributes();

    ProductGroupAttribute getAttribute(String name);

    boolean register(Product product);

    boolean registerFixed(Product product);

    Product derive();
}
