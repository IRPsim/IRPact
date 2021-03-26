package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.misc.Initialization;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface ProductManager extends Initialization {

    Collection<ProductGroup> getGroups();

    default int getNumberOfProductGroups() {
        return getGroups().size();
    }

    ProductGroup getGroup(String name);
}
