package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.misc.Initialization;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface ProductManager extends Initialization {

    Collection<ProductGroup> getGroups();

    ProductGroup getGroup(String name);

    Stream<Product> streamFixedProducts();

    Product getFixedProduct(String name);
}
