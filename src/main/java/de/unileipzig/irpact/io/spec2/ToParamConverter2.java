package de.unileipzig.irpact.io.spec2;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public interface ToParamConverter2<T> {

    T[] toParamArray(SpecificationJob2 job) throws ParsingException;

    T toParam(ObjectNode root, SpecificationJob2 job) throws ParsingException;
}
