package de.unileipzig.irpact.core.util.result.adoptions;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractCumulativeAdoptionAnalyser extends AbstractAdoptionAnalyser {

    protected Collection<? extends Integer> years;

    protected AbstractCumulativeAdoptionAnalyser() {
        super();
    }

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
                add(year == info.getYear(), year, info);
            }
        }
    }

    protected abstract void add(boolean currentYear, int year, AdoptionInfo info);
}
