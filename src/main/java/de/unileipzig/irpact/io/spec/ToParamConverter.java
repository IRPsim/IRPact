package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public interface ToParamConverter<T> {

    T[] toParamArray(SpecificationJob job) throws ParsingException;

    T toParam(ObjectNode root, SpecificationJob job) throws ParsingException;
}
