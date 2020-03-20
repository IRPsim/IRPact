package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.simulation.Timestamp;

/**
 * @author Daniel Abitz
 */
public final class AdoptedProduct {

    protected Timestamp timestamp;
    protected Need need;
    protected Product product;

    public AdoptedProduct(
            Timestamp timestamp,
            Need need,
            Product product) {
        this.timestamp = Check.requireNonNull(timestamp, "timestamp");
        this.need = Check.requireNonNull(need, "need");
        this.product = Check.requireNonNull(product, "product");
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Need getNeed() {
        return need;
    }

    public Product getProduct() {
        return product;
    }
}
