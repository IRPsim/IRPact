package de.unileipzig.irpact.core.product.awareness;

import de.unileipzig.irpact.commons.awareness.BinaryAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.Product;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BinaryProductAwarenessSupplyScheme extends BinaryAwarenessSupplyScheme<Product> implements ProductAwarenessSupplyScheme {

    public BinaryProductAwarenessSupplyScheme() {
    }

    @Override
    public BinaryProductAwareness derive() {
        return new BinaryProductAwareness();
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName()
        );
    }
}
