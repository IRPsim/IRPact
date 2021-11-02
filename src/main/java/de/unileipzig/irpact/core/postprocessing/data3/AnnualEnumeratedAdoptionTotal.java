package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public class AnnualEnumeratedAdoptionTotal extends AnnualEnumeratedAdoptionData<Object> {

    private static final Object TOTAL = new Object();

    public AnnualEnumeratedAdoptionTotal() {
    }

    @Override
    public int getCount(int year, Product product, Object value) {
        return getCount(year, product);
    }

    public int getCount(int year, Product product) {
        return data.getCount(year, product, TOTAL);
    }

    @Override
    protected void update(int year, ConsumerAgent ca, AdoptedProduct ap) {
        data.update(year, ap.getProduct(), TOTAL);
    }

    @Override
    protected AnnualEnumeratedAdoptionTotal newInstance() {
        return new AnnualEnumeratedAdoptionTotal();
    }
}
