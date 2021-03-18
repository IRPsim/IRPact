package de.unileipzig.irpact.core.product.awareness;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.awareness.BinaryAwareness;
import de.unileipzig.irpact.core.product.Product;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BinaryProductAwareness extends BinaryAwareness<Product> implements ProductAwareness {

    @Override
    public int getHashCode() {
        return Objects.hash(
                IsEquals.getSetHashCode(getItems())
        );
    }
}
