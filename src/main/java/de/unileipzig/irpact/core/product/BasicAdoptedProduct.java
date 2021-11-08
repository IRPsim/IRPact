package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.util.AdoptionPhase;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptedProduct implements AdoptedProduct {

    protected Need need;
    protected Product product;
    protected Timestamp timestamp;
    protected AdoptionPhase phase;
    protected boolean initial;
    protected double utility;

    public BasicAdoptedProduct() {
        this(null, null, null, AdoptionPhase.UNKNOWN, Double.NaN);
    }

    public BasicAdoptedProduct(Need need, Product product, Timestamp timestamp, AdoptionPhase phase) {
        this(need, product, timestamp, phase, Double.NaN);
    }

    public BasicAdoptedProduct(Need need, Product product, Timestamp timestamp, AdoptionPhase phase, double utility) {
        setNeed(need);
        setProduct(product);
        setTimestamp(timestamp);
        setPhase(phase);
        setUtility(utility);
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

    public void setPhase(AdoptionPhase phase) {
        this.phase = phase;
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
    public AdoptionPhase getPhase() {
        return phase;
    }

    @Override
    public boolean isInitial() {
        return phase == AdoptionPhase.INITIAL;
    }

    @Override
    public boolean hasUtility() {
        return !Double.isNaN(utility);
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }

    @Override
    public double getUtility() {
        return utility;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                need,
                product,
                timestamp,
                phase,
                initial
        );
    }
}
