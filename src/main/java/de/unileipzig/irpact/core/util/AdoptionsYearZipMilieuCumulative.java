package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class AdoptionsYearZipMilieuCumulative extends AdoptionsCumulative {

    private final VarCollection mapping = new VarCollection(Integer.class, String.class, String.class, Integer.class);

    public AdoptionsYearZipMilieuCumulative() {
    }

    public void init(
            Collection<? extends Integer> years,
            Collection<? extends String> zips,
            Collection<? extends String> milieus) {
        for(Integer year: years) {
            for(String zip: zips) {
                for(String milieu: milieus) {
                    mapping.varAdd(year, zip, milieu, ZERO.get());
                }
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
            String milieu = agent.getGroup().getName();
            mapping.varUpdate(ZERO, ADD, year, zip, milieu, 1);
        }

    }

    @Override
    public VarCollection getMapping() {
        return mapping;
    }

    @Override
    protected void initHeader(StringBuilder sb) {
        sb.append("year,zip,milieu,adoptionsCumulative\n");
    }

    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("year,zip,milieu,adoptionsCumulative\n");
        for(Object[] entry: mapping.iterable()) {
            sb.append(StringUtil.concat(",", entry));
            sb.append("\n");
        }
        return sb.toString();
    }
}
