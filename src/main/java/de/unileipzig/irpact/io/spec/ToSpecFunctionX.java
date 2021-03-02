package de.unileipzig.irpact.io.spec;

import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public interface ToSpecFunctionX<T> {

    Class<T> getParamType();

    void toSpec(T instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException;
}
