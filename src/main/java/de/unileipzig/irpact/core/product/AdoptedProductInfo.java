package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.need.Need;

/**
 * @author Daniel Abitz
 */
public class AdoptedProductInfo {

    protected Need need;
    protected Product product;

    public AdoptedProductInfo(Need need, Product product) {
        this.need = Check.requireNonNull(need, "need");
        this.product = Check.requireNonNull(product, "product");
    }

    public Need getNeed() {
        return need;
    }

    public Product getProduct() {
        return product;
    }
}
