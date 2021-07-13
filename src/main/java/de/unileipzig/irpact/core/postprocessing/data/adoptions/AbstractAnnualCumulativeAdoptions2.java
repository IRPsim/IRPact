package de.unileipzig.irpact.core.postprocessing.data.adoptions;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAnnualCumulativeAdoptions2<X, Y> extends AbstractCumulativeAdoptionAnalyser {

    public static final int INDEX_YEAR = 0;
    public static final int INDEX_FIRST_VALUE = 1;
    public static final int INDEX_SECOND_VALUE = 2;
    public static final int INDEX_ADOPTIONS = 3;
    public static final int INDEX_ADOPTIONS2 = 4;

    protected String[] csvHeader;

    public AbstractAnnualCumulativeAdoptions2() {
        super();
        this.data = newCollection();
    }

    protected abstract Class<X> getXClass();

    protected abstract Class<Y> getYClass();

    protected VarCollection newCollection() {
        return new VarCollection(Integer.class, getXClass(), getYClass(), AdoptionResultInfo.class);
    }

    public void setCsvHeader(String[] csvHeader) {
        this.csvHeader = csvHeader;
    }

    public void init(
            Collection<? extends X> firstValues,
            Collection<? extends Y> secondValues) {
        for(Integer year: years) {
            for(X xValue: firstValues) {
                for(Y yValue: secondValues) {
                    data.varSet(year, xValue, yValue, AdoptionResultInfo.ZERO.get());
                }
            }
        }
    }
}
