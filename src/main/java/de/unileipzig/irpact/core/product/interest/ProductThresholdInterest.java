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

    @Override
    public boolean isInterested(Product item, double interest) {
        Double t = thresholds.get(item.getGroup());
        return t != null && interest >= t;
    }

    @Override
    public String printInfo(Product product) {
        if(hasThreshold(product.getGroup())) {
            double v = getValue(product);
            double t = getThresholdFor(product);
            boolean i = isInterested(product);
            return "value=" + v + ",threshold=" + t + ",interested=" + i;
        } else {
            return "MISSING";
        }
    }
}
