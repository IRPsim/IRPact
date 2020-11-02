package de.unileipzig.irpact.start.optact.out;

import de.unileipzig.irpact.start.optact.in.Ii;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.Util;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class OutRoot implements RootClass {

    public static final List<ParserInput> CLASSES = Util.arrayListOf(
            ParserInput.newInstance(Type.REFERENCE, Ii.class),
            //===
            ParserInput.newInstance(Type.OUTPUT, OutCustom.class),
            ParserInput.newInstance(Type.OUTPUT, OutRoot.class)
    );

    @FieldDefinition
    public OutCustom[] outGrps;

    public OutRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES;
    }
}
