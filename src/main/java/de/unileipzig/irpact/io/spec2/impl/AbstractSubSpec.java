package de.unileipzig.irpact.io.spec2.impl;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSubSpec<T> extends AbstractSpec<T> {

    public abstract boolean isType(String type);

    public abstract String getType();

    public abstract boolean isInstance(Object obj);

    @SuppressWarnings("unchecked")
    public void rawToSpec(Object input, SpecificationJob2 job) throws ParsingException {
        toSpec((T) input, job);
    }

    @SuppressWarnings("unchecked")
    public void rawCreate(Object input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        create((T) input, rootSpec, job);
    }
}
