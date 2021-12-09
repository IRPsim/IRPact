package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class AnnualEnumeratedAdoptionPhases extends AnnualEnumeratedAdoptionData<AdoptionPhase> {

    public AnnualEnumeratedAdoptionPhases() {
    }

    public void initalize(
            Collection<? extends Integer> years,
            Collection<? extends Product> products,
            Collection<? extends AdoptionPhase> phases) {
        for(Product product: products) {
            for(AdoptionPhase phase: phases) {
                initial.init(product, phase, 0);
                for(int year: years) {
                    data.init(year, product, phase, 0);
                }
            }
        }
    }

    @Override
    protected void update(int year, ConsumerAgent ca, AdoptedProduct ap) {
        data.update(year, ap.getProduct(), ap.getPhase());
    }

    @Override
    protected void updateInitial(ConsumerAgent ca, AdoptedProduct ap) {
        initial.update(ap.getProduct(), ap.getPhase());
    }

    @Override
    protected AnnualEnumeratedAdoptionPhases newInstance() {
        return new AnnualEnumeratedAdoptionPhases();
    }
}
