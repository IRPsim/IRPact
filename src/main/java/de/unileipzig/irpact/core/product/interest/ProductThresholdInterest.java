package de.unileipzig.irpact.core.product.interest;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.interest.ThresholdInterest;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterest extends ThresholdInterest<Product, ProductGroup> implements ProductInterest {

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                ChecksumComparable.getMapChecksum(getThresholds()),
                ChecksumComparable.getMapChecksum(getItems())
        );
    }

    @Override
    protected double getThresholdFor(Product item) {
        return getThreshold(item.getGroup());
    }

    @Override
    public boolean hasThreshold(ProductGroup group) {
        return thresholds.containsKey(group);
    }
}
