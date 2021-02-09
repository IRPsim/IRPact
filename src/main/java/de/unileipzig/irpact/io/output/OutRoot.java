package de.unileipzig.irpact.io.output;

import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;

import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(root = true)
public class OutRoot implements RootClass {

    public static final List<ParserInput> LIST = ParserInput.listOf(Type.OUTPUT,
            OutRoot.class
    );

    public OutRoot() {
    }

    @Override
    public List<ParserInput> getInput() {
        return LIST;
    }
}
