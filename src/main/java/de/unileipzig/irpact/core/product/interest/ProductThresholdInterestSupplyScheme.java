package de.unileipzig.irpact.core.product.interest;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.interest.ThresholdInterestSupplyScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterestSupplyScheme extends ThresholdInterestSupplyScheme<Product, ProductGroup> implements ProductInterestSupplyScheme {

    public ProductThresholdInterestSupplyScheme() {
    }

    public ProductThresholdInterestSupplyScheme(String name) {
        setName(name);
    }

    @Override
    public boolean hasThresholdDistribution(ProductGroup group) {
        return distributions.containsKey(group);
    }

    @Override
    public UnivariateDoubleDistribution getThresholdDistribution(ProductGroup group) {
        return distributions.get(group);
    }

    @Override
    public void setThresholdDistribution(ProductGroup group, UnivariateDoubleDistribution distribution) {
        distributions.put(group, distribution);
    }

    @Override
    public boolean addMissing(ProductInterest interest) {
        boolean changed = false;
        for(Map.Entry<ProductGroup, UnivariateDoubleDistribution> entry: getDistributions().entrySet()) {
            ProductGroup group = entry.getKey();
            if(!interest.hasThreshold(group)) {
                double threshold = entry.getValue().drawDoubleValue();
                interest.setThreshold(group, threshold);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public ProductThresholdInterest derive() {
        ProductThresholdInterest interest = new ProductThresholdInterest();
        for(Map.Entry<ProductGroup, UnivariateDoubleDistribution> entry: getDistributions().entrySet()) {
            ProductGroup group = entry.getKey();
            double threshold = entry.getValue().drawDoubleValue();
            interest.setThreshold(group, threshold);
        }
        return interest;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                ChecksumComparable.getMapChecksum(getDistributions())
        );
    }
}
