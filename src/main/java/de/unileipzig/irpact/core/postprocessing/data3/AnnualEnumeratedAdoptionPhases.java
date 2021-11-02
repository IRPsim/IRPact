package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.util.AdoptionPhase;

/**
 * @author Daniel Abitz
 */
public class AnnualEnumeratedAdoptionPhases extends AnnualEnumeratedAdoptionData<AdoptionPhase> {

    public AnnualEnumeratedAdoptionPhases() {
    }

    @Override
    protected void update(int year, ConsumerAgent ca, AdoptedProduct ap) {
        data.update(year, ap.getProduct(), ap.getPhase());
    }

    @Override
    protected AnnualEnumeratedAdoptionPhases newInstance() {
        return new AnnualEnumeratedAdoptionPhases();
    }
}
