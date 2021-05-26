package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class AdoptionsYearZipCumulative extends AdoptionsCumulative {

    private final VarCollection mapping = new VarCollection(Integer.class, String.class, Integer.class);

    public AdoptionsYearZipCumulative() {
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
            Collection<? extends Integer> years,
            ConsumerAgent agent,
            AdoptedProduct adoptedProduct) {
        int thisYear = adoptedProduct.getYear();
        for(int year: years) {
            if(year < thisYear) {
                continue;
            }
            String zip = agent.findAttribute(RAConstants.ZIP).asValueAttribute().getStringValue();
            mapping.varUpdate(ZERO, ADD, year, zip, 1);
        }
    }

    @Override
    protected void initHeader(StringBuilder sb) {
        sb.append("year,zip,adoptionsCumulative\n");
    }

    @Override
    public VarCollection getMapping() {
        return mapping;
    }
}
