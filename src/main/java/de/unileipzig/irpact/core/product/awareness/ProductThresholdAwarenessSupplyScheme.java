package de.unileipzig.irpact.core.product.awareness;

import de.unileipzig.irpact.commons.awareness.ThresholdAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdAwarenessSupplyScheme extends ThresholdAwarenessSupplyScheme<Product> implements ProductAwarenessSupplyScheme {

    public ProductThresholdAwarenessSupplyScheme() {
    }

    @Override
    public ProductThresholdAwareness derive() {
        ProductThresholdAwareness awareness = new ProductThresholdAwareness();
        awareness.setThreshold(distribution.drawDoubleValue());
        return awareness;
    }
}
