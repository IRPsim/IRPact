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
        if(requiresSetup()) {
            throw new IllegalStateException("requires setup");
        } else {
            throw new UnsupportedOperationException("name: '" + getName() + "' class: '" + getClass() + "'");
        }
    }

    default Object parse(T parser, Object input) throws ParsingException {
        return parse(parser);
    }

    default void setup(T parser, Object input) throws ParsingException {
        if(requiresSetup()) {
            throw new UnsupportedOperationException("name: '" + getName() + "' class: '" + getClass() + "'");
        } else {
            throw new IllegalStateException("requires parse");
        }
    }

    default void update(T parser, Object original, Object input) throws ParsingException {
        throw new UnsupportedOperationException("name: '" + getName() + "' class: '" + getClass() + "'");
    }
}
