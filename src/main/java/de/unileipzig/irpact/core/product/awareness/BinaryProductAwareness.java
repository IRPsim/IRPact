package de.unileipzig.irpact.core.product.awareness;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.awareness.BinaryAwareness;
import de.unileipzig.irpact.core.product.Product;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BinaryProductAwareness extends BinaryAwareness<Product> implements ProductAwareness {

    @Override
    public int getChecksum() {
        return Objects.hash(
                ChecksumComparable.getSetChecksum(getItems())
        );
    }
}
