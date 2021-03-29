package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.need.Need;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptedProduct implements AdoptedProduct {

    protected Need need;
    protected Product product;
    protected Timestamp timestamp;
    protected boolean initial;

    public BasicAdoptedProduct() {
    }

    public BasicAdoptedProduct(Product product) {
        setNeed(null);
        setProduct(product);
        setTimestamp(null);
        setInitial(true);
    }

    public BasicAdoptedProduct(Need need, Product product, Timestamp timestamp) {
        setNeed(need);
        setProduct(product);
        setTimestamp(timestamp);
        setInitial(false);
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    @Override
    public Need getNeed() {
        return need;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isInitial() {
        return initial;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                need,
                product,
                timestamp,
                initial
        );
    }
}
