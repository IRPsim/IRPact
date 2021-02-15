package de.unileipzig.irpact.io.output;

import de.unileipzig.irpact.io.IOResources;
import de.unileipzig.irpact.io.inout.binary.HiddenBinaryData;
import de.unileipzig.irpact.start.optact.in.Ii;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irptools.defstructure.AnnotationResource;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.uiedn.Section;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.util.UiEdn;
import de.unileipzig.irptools.util.Util;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(root = true)
public class OutRoot implements RootClass {

    public static final List<ParserInput> LIST = ParserInput.listOf(Type.OUTPUT,
            HiddenBinaryData.class
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
    public Collection<? extends ParserInput> getInput() {
        return CLASSES;
    }

    @Override
    public AnnotationResource getResources() {
        return IOResources.getInstance();
    }

    @Override
    public void peekEdn(Sections sections, UiEdn ednType) {
        if(ednType == UiEdn.OUTPUT) {
            Section imageSection = new Section();
            imageSection.setPriority(-1);
            imageSection.setLabel("Agentennetzwerk");
            imageSection.setImage("agentGraph");
            imageSection.setDescription("Agentennetzwerk in IRPact");
            imageSection.setIcon("fa fa-spinner");
            sections.add(imageSection);
        }
    }
}
