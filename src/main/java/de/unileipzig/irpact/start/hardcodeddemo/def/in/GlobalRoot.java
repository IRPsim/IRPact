package de.unileipzig.irpact.start.hardcodeddemo.def.in;

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
            AnnotationParser.Input.newInstance(Type.INPUT, GlobalRoot.class),
            AnnotationParser.Input.newInstance(Type.INPUT, GlobalScalars.class),
            AnnotationParser.Input.newInstance(Type.INPUT, AgentGroup.class),
            AnnotationParser.Input.newInstance(Type.INPUT, Product.class)
    );

    @FieldDefinition
    public GlobalScalars scalars;

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public Product[] products;

    public GlobalRoot() {
    }

    public GlobalRoot(GlobalScalars scalars, AgentGroup[] agentGroups, Product[] products) {
        this.scalars = scalars;
        this.agentGroups = agentGroups;
        this.products = products;
    }

    public GlobalScalars getScalars() {
        return scalars;
    }

    public AgentGroup[] getAgentGroups() {
        return agentGroups;
    }

    public Product[] getProducts() {
        return products;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GlobalRoot{");
        sb.append("\n scalars=").append(scalars);
        sb.append("\n agentGroups=[");
        for(AgentGroup group: agentGroups) {
            sb.append("\n  ").append(group);
        }
        sb.append("\n ]");
        sb.append("\n products=[");
        for(Product product: products) {
            sb.append("\n  ").append(product);
        }
        sb.append("\n ]\n}");
        return sb.toString();
    }
}
