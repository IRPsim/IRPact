package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InEntity<T extends InputParser> extends Copyable {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

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

    default void update(T parser, Object input) throws ParsingException {
        throw new UnsupportedOperationException();
    }
}
