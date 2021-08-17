package de.unileipzig.irpact.start.optact.out;

import de.unileipzig.irptools.defstructure.DefinitionType;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
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
@Definition(
        root = true
)
public class OutRoot implements RootClass {

    public static final List<ParserInput> CLASSES_WITHOUT_ROOT = Util.arrayListOf(
            ParserInput.newInstance(DefinitionType.OUTPUT, OutCustom.class)
    );

    public static final List<ParserInput> CLASSES = Util.arrayListOf(
            ParserInput.newInstance(DefinitionType.OUTPUT, OutCustom.class),
            ParserInput.newInstance(DefinitionType.OUTPUT, OutRoot.class)
    );

    @FieldDefinition
    public OutCustom[] outGrps;

    public OutRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES;
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
