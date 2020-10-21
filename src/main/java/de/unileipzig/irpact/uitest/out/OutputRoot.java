package de.unileipzig.irpact.uitest.out;

import de.unileipzig.irpact.uitest.in1.AgentGroup;
import de.unileipzig.irpact.uitest.in1.Product;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.Table;
import de.unileipzig.irptools.util.Util;

import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class OutputRoot {

    public static final List<ParserInput> CLASSES = Util.arrayListOf(
            ParserInput.newInstance(Type.REFERENCE, AgentGroup.class),
            ParserInput.newInstance(Type.REFERENCE, Product.class),
            ParserInput.newInstance(Type.OUTPUT, OutputRoot.class),
            ParserInput.newInstance(Type.OUTPUT, OutputScalars.class)
    );

    @FieldDefinition
    public OutputScalars scalars;

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public Product[] products;

    public OutputRoot() {
    }

    public static OutputRoot x() {
        AgentGroup[] agentGroup = AgentGroup.x();
        Product[] products = Product.x();
        Table<AgentGroup, Product, Double> t = Table.newLinked();
        t.put(agentGroup[0], products[0], 5.0);
        t.put(agentGroup[0], products[1], 6.0);
        t.put(agentGroup[1], products[0], 3.0);
        t.put(agentGroup[1], products[1], 2.0);
        t.put(agentGroup[2], products[0], 36.0);
        t.put(agentGroup[2], products[1], 39.0);
        OutputScalars scalars = new OutputScalars(t);

        OutputRoot x = new OutputRoot();
        x.agentGroups = agentGroup;
        x.products = products;
        x.scalars = scalars;
        return x;
    }
}
