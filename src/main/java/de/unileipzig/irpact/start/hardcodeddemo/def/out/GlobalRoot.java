package de.unileipzig.irpact.start.hardcodeddemo.def.out;

import de.unileipzig.irpact.start.hardcodeddemo.def.in.AgentGroup;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.Util;

import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class GlobalRoot {

    public static final List<AnnotationParser.Input> CLASSES = Util.listOf(
            AnnotationParser.Input.newInstance(Type.INPUT, AgentGroup.class),
            AnnotationParser.Input.newInstance(Type.INPUT, Product.class),
            AnnotationParser.Input.newInstance(Type.OUTPUT, GlobalRoot.class),
            AnnotationParser.Input.newInstance(Type.OUTPUT, GlobalScalars.class)
    );

    @FieldDefinition
    public GlobalScalars scalars;

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public Product[] products;

    public GlobalRoot() {
    }
}
