package de.unileipzig.irpact.core.util.result.adoptions;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.AdoptedProduct;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptionEntry implements AdoptionEntry {

    protected ConsumerAgent agent;
    protected AdoptedProduct product;

    public BasicAdoptionEntry(ConsumerAgent agent, AdoptedProduct product) {
        this.agent = agent;
        this.product = product;
    }

    @Override
    public ConsumerAgent getAgent() {
        return agent;
    }

    @Override
    public AdoptedProduct getAdoptedProduct() {
        return product;
    }
}
