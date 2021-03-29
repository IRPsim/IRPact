package de.unileipzig.irpact.core.product.awareness;

import de.unileipzig.irpact.commons.awareness.BinaryAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ProductBinaryAwarenessSupplyScheme extends BinaryAwarenessSupplyScheme<Product, ProductGroup> implements ProductAwarenessSupplyScheme {

    public ProductBinaryAwarenessSupplyScheme() {
    }

    @Override
    public ProductBinaryAwareness derive() {
        return new ProductBinaryAwareness();
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName()
        );
    }
}
