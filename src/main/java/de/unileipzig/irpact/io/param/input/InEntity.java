package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irptools.util.Copyable;

/**
 * @author Daniel Abitz
 */
public interface InEntity<T extends InputParser> extends Copyable {

    String getName();

    default boolean requiresSetup() {
        return false;
    }

    default Object parse(T parser) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    default void setup(T parser, Object input) throws ParsingException {
        throw new UnsupportedOperationException();
    }
}
