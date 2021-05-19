package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.AdoptedProduct;

/**
 * @author Daniel Abitz
 */
public class AdoptionResult {

    protected ConsumerAgent agent;
    protected AdoptedProduct product;

    public AdoptionResult(ConsumerAgent agent, AdoptedProduct product) {
        this.agent = agent;
        this.product = product;
    }

    public ConsumerAgent getAgent() {
        return agent;
    }

    public AdoptedProduct getProduct() {
        return product;
    }
}
