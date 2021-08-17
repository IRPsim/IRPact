package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.product.attribute.ProductGroupAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProductGroup extends SimulationEntity {

    Collection<Product> getProducts();

    Product getProduct(String name);

    Collection<ProductGroupAttribute> getGroupAttributes();

    ProductGroupAttribute getGroupAttribute(String name);

    boolean hasProduct(String name);

    void addProduct(Product product);

    Product derive();

    Product deriveAndAdd();
}
