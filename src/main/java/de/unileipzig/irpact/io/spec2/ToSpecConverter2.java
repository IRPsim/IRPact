package de.unileipzig.irpact.io.spec2;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;

/**
 * @author Daniel Abitz
 */
public interface ToSpecConverter2<T> {

    Class<T> getParamType();

    default void toSpec(T[] inputArray, SpecificationJob2 job) throws ParsingException {
        for(T input: inputArray) {
            toSpec(input, job);
        }
    }

    void toSpec(T input, SpecificationJob2 job) throws ParsingException;

    void create(T input, ObjectNode root, SpecificationJob2 job) throws ParsingException;
}
