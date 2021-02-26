package de.unileipzig.irpact.core.product.interest;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.interest.ThresholdInterest;
import de.unileipzig.irpact.core.product.Product;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterest extends ThresholdInterest<Product> implements ProductInterest {

    @Override
    public int getHashCode() {
        return Objects.hash(
                getThreshold(),
                IsEquals.getMapHashCode(getItems())
        );
    }
}
