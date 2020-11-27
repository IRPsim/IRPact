package de.unileipzig.irpact.core.product;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface ProductManager {

    boolean add(ProductGroup group);

    Collection<ProductGroup> getGroups();

    ProductGroup getGroup(String name);

    Stream<Product> streamFixedProducts();

    Product getFixedProduct(String name);
}
