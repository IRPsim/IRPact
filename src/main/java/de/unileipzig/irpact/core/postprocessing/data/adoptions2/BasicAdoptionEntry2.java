package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.AdoptedProduct;

/**
 * @author Daniel Abitz
 */
public class BasicAdoptionEntry2 implements AdoptionEntry2 {

    protected ConsumerAgent agent;
    protected AdoptedProduct product;

    public BasicAdoptionEntry2(ConsumerAgent agent, AdoptedProduct product) {
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
