package de.unileipzig.irpact.start.hardcodeddemo.def.out;

import de.unileipzig.irpact.start.hardcodeddemo.def.in.AgentGroup;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import de.unileipzig.irptools.defstructure.GamsType;
import de.unileipzig.irptools.defstructure.ParserInput;
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
public class OutputRoot {

    public static final List<ParserInput> CLASSES = Util.listOf(
            ParserInput.newInstance(GamsType.INPUT, AgentGroup.class),
            ParserInput.newInstance(GamsType.INPUT, Product.class),
            ParserInput.newInstance(GamsType.OUTPUT, OutputRoot.class),
            ParserInput.newInstance(GamsType.OUTPUT, OutputScalars.class)
    );

    @FieldDefinition
    public OutputScalars scalars;

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public Product[] products;

    public OutputRoot() {
    }
}
