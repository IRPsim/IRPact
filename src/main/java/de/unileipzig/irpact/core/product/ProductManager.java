package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.misc.InitalizablePart;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface ProductManager extends InitalizablePart {

    Collection<ProductGroup> getGroups();

    default int getNumberOfProductGroups() {
        return getGroups().size();
    }

    ProductGroup getGroup(String name);

    default Stream<Product> streamProducts() {
        return getGroups().stream()
                .flatMap(g -> g.getProducts().stream());
    }

    void makeKnownInSimulation(Product product);
}
