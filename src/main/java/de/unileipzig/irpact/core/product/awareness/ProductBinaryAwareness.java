package de.unileipzig.irpact.core.product.awareness;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.awareness.BinaryAwareness;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ProductBinaryAwareness extends BinaryAwareness<Product, ProductGroup> implements ProductAwareness {

    @Override
    public int getChecksum() {
        return Objects.hash(
                ChecksumComparable.getSetChecksum(getItems())
        );
    }
}
