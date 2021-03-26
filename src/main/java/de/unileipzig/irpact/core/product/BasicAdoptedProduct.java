package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.need.Need;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptedProduct implements AdoptedProduct {

    protected Need need;
    protected Product product;
    protected Timestamp timestamp;

    public BasicAdoptedProduct() {
    }

    public BasicAdoptedProduct(Need need, Product product, Timestamp timestamp) {
        setNeed(need);
        setProduct(product);
        setTimestamp(timestamp);
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
    public int getChecksum() {
        return Objects.hash(need.getChecksum(), product.getChecksum(), timestamp.getChecksum());
    }
}
