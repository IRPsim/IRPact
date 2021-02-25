package de.unileipzig.irpact.core.product.awareness;

import de.unileipzig.irpact.commons.awareness.AwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProductAwarenessSupplyScheme extends AwarenessSupplyScheme<Product> {

    @Override
    ProductAwareness derive();
}
