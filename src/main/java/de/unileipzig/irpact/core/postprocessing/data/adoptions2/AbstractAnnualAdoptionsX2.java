package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAnnualAdoptionsX2<A, B> extends AbstractAdoptionAnalyser2 {

    public AbstractAnnualAdoptionsX2() {
    }

    protected abstract Class<A> getFirstType();

    protected abstract Class<B> getSecondType();

    @Override
    protected VarCollection newCollection() {
        return new VarCollection(Integer.class, getFirstType(), getSecondType(), AdoptionResultInfo2.class);
    }
}
