package de.unileipzig.irpact.io.output;

import de.unileipzig.irpact.start.optact.in.Ii;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.uiedn.Section;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.util.Util;

import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(root = true)
public class OutRoot implements RootClass {

    public static final List<ParserInput> LIST = ParserInput.listOf(Type.OUTPUT
            //TODO
    );

    public static final List<ParserInput> WITH_OPTACT = Util.mergedArrayListOf(
            LIST,
            Util.arrayListOf(
                    ParserInput.newInstance(Type.REFERENCE, Ii.class),
                    ParserInput.newInstance(Type.OUTPUT, OutCustom.class)
            )
    );

    public static final List<ParserInput> CLASSES = Util.mergedArrayListOf(
            WITH_OPTACT,
            Util.arrayListOf(
                    ParserInput.newInstance(Type.OUTPUT, OutRoot.class)
            )
    );

    //=========================
    //OptAct
    //=========================

    @FieldDefinition
    public OutCustom[] outGrps;

    public OutRoot() {
    }

    @Override
    public List<ParserInput> getInput() {
        return CLASSES;
    }

    @Override
    public void peekEdn(Sections sections, boolean input, boolean delta) {
        if(!input) {
            Section imageSection = new Section();
            imageSection.setPriority(-1);
            imageSection.setImage("agentImage");
            imageSection.setDescription("Bildanzeige");
        }
    }
}
