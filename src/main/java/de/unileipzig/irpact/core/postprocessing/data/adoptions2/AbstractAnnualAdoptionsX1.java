package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAnnualAdoptionsX1<A> extends AbstractAdoptionAnalyser2 {

    public AbstractAnnualAdoptionsX1() {
    }

    protected abstract Class<A> getFirstType();

    @Override
    protected VarCollection newCollection() {
        return new VarCollection(Integer.class, getFirstType(), AdoptionResultInfo2.class);
    }

    public void init(Collection<? extends A> firstValues) {
        VarCollection data = getData();
        for(Integer year: years) {
            for(A value: firstValues) {
                data.varSet(year, value, AdoptionResultInfo2.ZERO.get());
            }
        }
    }
}
