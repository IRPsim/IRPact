package de.unileipzig.irpact.core.product.interest;

import de.unileipzig.irpact.commons.interest.ThresholdInterestSupplyScheme;
import de.unileipzig.irpact.core.product.Product;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterestSupplyScheme extends ThresholdInterestSupplyScheme<Product> implements ProductInterestSupplyScheme {

    public ProductThresholdInterestSupplyScheme() {
    }

    @Override
    public ProductThresholdInterest derive() {
        ProductThresholdInterest awareness = new ProductThresholdInterest();
        awareness.setThreshold(distribution.drawDoubleValue());
        return awareness;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                getDistribution().getHashCode()
        );
    }
}
