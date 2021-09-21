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
    public void update(ConsumerAgent ca, AdoptedProduct ap) {
        int year = ap.isInitial() ? INITIAL_YEAR : ap.getYear();
        data.update(year, ap.getProduct(), ca.getGroup());
    }

    @Override
    protected AnnualEnumeratedConsumerGroups newInstance() {
        return new AnnualEnumeratedConsumerGroups();
    }
}
