package de.unileipzig.irpact.core.util.v2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractCumulativeAdoptionAnalyser extends AbstractAdoptionAnalyser {

    protected Collection<? extends Integer> years;

    public AbstractCumulativeAdoptionAnalyser(VarCollection data) {
        super(data);
    }

    public void setYears(Collection<? extends Integer> years) {
        this.years = years;
    }

    @Override
    public void add(AdoptionInfo info) {
        for(Integer year: years) {
            if(year >= info.getYear()) {
                add(year, info);
            }
        }
    }

    public abstract int getTotal(AdoptionInfo info);

    protected abstract void add(int year, AdoptionInfo info);
}
