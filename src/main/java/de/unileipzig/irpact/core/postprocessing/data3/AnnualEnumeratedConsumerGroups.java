package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.AdoptedProduct;

/**
 * @author Daniel Abitz
 */
public class AnnualEnumeratedConsumerGroups extends AnnualEnumeratedAdoptionData<ConsumerAgentGroup> {

    public AnnualEnumeratedConsumerGroups() {
    }

    @Override
    protected void update(int year, ConsumerAgent ca, AdoptedProduct ap) {
        data.update(year, ap.getProduct(), ca.getGroup());
    }

    @Override
    protected void updateInitial(ConsumerAgent ca, AdoptedProduct ap) {
        initial.update(ap.getProduct(), ca.getGroup());
    }

    @Override
    protected AnnualEnumeratedConsumerGroups newInstance() {
        return new AnnualEnumeratedConsumerGroups();
    }
}
