package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public interface ToSpecConverter<T> extends SpecFunction {

    Class<T> getParamType();

    default void toSpec(T[] inputArray, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        for(T input: inputArray) {
            toSpec(input,manager, converter, inline);
        }
    }

    void toSpec(T input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException;

    void create(T input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException;
}
