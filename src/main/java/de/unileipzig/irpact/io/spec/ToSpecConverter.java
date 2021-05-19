package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public interface ToSpecConverter<T> {

    Class<T> getParamType();

    default void toSpec(T[] inputArray, SpecificationJob job) throws ParsingException {
        for(T input: inputArray) {
            toSpec(input, job);
        }
    }

    void toSpec(T input, SpecificationJob job) throws ParsingException;

    void create(T input, ObjectNode root, SpecificationJob job) throws ParsingException;
}
