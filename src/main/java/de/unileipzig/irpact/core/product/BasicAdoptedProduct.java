package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.persistence.annotation.ChecksumAndPersistentValue;
import de.unileipzig.irpact.commons.persistence.annotation.UsedPersisterRestorer;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.jadex.persistance.binary.data.BasicAdoptedProductPR;

/**
 * @author Daniel Abitz
 */
@UsedPersisterRestorer(BasicAdoptedProductPR.class)
public class BasicAdoptedProduct implements AdoptedProduct {

    @ChecksumAndPersistentValue
    protected Need need;
    @ChecksumAndPersistentValue
    protected Product product;
    @ChecksumAndPersistentValue("epoch millis")
    protected Timestamp timestamp;
    @ChecksumAndPersistentValue
    protected AdoptionPhase phase;
    @ChecksumAndPersistentValue
    protected boolean initial;

    public BasicAdoptedProduct() {
        this(null, null, null, AdoptionPhase.UNKNOWN);
    }

    public BasicAdoptedProduct(Product product) {
        this(null, product, null, AdoptionPhase.INITIAL);
    }

    public BasicAdoptedProduct(Need need, Product product, Timestamp timestamp) {
        this(need, product, timestamp, AdoptionPhase.UNKNOWN);
    }

    public BasicAdoptedProduct(Need need, Product product, Timestamp timestamp, AdoptionPhase phase) {
        setNeed(need);
        setProduct(product);
        setTimestamp(timestamp);
        setPhase(phase);
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
