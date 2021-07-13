package de.unileipzig.irpact.core.postprocessing.data.adoptions;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAnnualCumulativeAdoptions1<X> extends AbstractCumulativeAdoptionAnalyser {

    public static final int INDEX_YEAR = 0;
    public static final int INDEX_FIRST_VALUE = 1;
    public static final int INDEX_ADOPTIONS = 2;
    public static final int INDEX_ADOPTIONS2 = 3;

    protected String[] csvHeader;

    public AbstractAnnualCumulativeAdoptions1() {
        super();
        this.data = newCollection();
    }

    protected abstract Class<X> getXClass();

    protected VarCollection newCollection() {
        return new VarCollection(Integer.class, getXClass(), AdoptionResultInfo.class);
    }

    public void setCsvHeader(String[] csvHeader) {
        this.csvHeader = csvHeader;
    }

    public void init(Collection<? extends X> firstValues) {
        for(Integer year: years) {
            for(X xValue: firstValues) {
                data.varSet(year, xValue, AdoptionResultInfo.ZERO.get());
            }
        }
    }
}
