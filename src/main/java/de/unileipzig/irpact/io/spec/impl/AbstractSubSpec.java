package de.unileipzig.irpact.io.spec.impl;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSubSpec<T> extends AbstractSpec<T> {

    public abstract boolean isType(String type);

    public abstract String getType();

    public abstract boolean isInstance(Object obj);

    @SuppressWarnings("unchecked")
    public void rawToSpec(Object input, SpecificationJob job) throws ParsingException {
        toSpec((T) input, job);
    }

    @SuppressWarnings("unchecked")
    public void rawCreate(Object input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        create((T) input, rootSpec, job);
    }
}
