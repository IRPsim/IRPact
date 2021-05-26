package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class AdoptionsYearZip extends AdoptionsCumulative {

    private final VarCollection mapping = new VarCollection(Integer.class, String.class, Integer.class);

    public AdoptionsYearZip() {
    }

    public void init(
            Collection<? extends Integer> years,
            Collection<? extends String> zips) {
        for(Integer year: years) {
            for(String zip: zips) {
                mapping.varAdd(year, zip, ZERO.get());
            }
        }
    }

    public void add(
            ConsumerAgent agent,
            AdoptedProduct adoptedProduct) {
        int year = adoptedProduct.getYear();
        String zip = agent.findAttribute(RAConstants.ZIP).asValueAttribute().getStringValue();
        mapping.varUpdate(ZERO, ADD, year, zip, 1);
    }

    @Override
    protected void initHeader(StringBuilder sb) {
        sb.append("year,zip,adoptions\n");
    }

    @Override
    public VarCollection getMapping() {
        return mapping;
    }
}
