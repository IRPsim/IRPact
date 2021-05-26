package de.unileipzig.irpact.core.util.v2;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.AdoptedProduct;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptionInfo implements AdoptionInfo {

    protected ConsumerAgent agent;
    protected AdoptedProduct product;

    public BasicAdoptionInfo(ConsumerAgent agent, AdoptedProduct product) {
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
