package de.unileipzig.irpact.io.spec;

import de.unileipzig.irpact.commons.exception.ParsingException;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public interface ToParamFunctionX<T> {

    T toParam(SpecificationManager manager, Map<String, Object> cache) throws ParsingException;
}
