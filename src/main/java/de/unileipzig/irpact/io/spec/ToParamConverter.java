package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public interface ToParamConverter<T> extends SpecFunction {

    default T[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    default T toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        throw new UnsupportedOperationException();
    }
}
