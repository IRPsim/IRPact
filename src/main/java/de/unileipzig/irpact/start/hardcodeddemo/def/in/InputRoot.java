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
public class InputRoot {

    public static final List<AnnotationParser.Input> CLASSES = Util.listOf(
            AnnotationParser.Input.newInstance(Type.INPUT, InputRoot.class),
            AnnotationParser.Input.newInstance(Type.INPUT, InputScalars.class),
            AnnotationParser.Input.newInstance(Type.INPUT, AgentGroup.class),
            AnnotationParser.Input.newInstance(Type.INPUT, Product.class)
    );

    @FieldDefinition
    public InputScalars scalars;

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public Product[] products;

    public InputRoot() {
    }

    public InputRoot(InputScalars scalars, AgentGroup[] agentGroups, Product[] products) {
        this.scalars = scalars;
        this.agentGroups = agentGroups;
        this.products = products;
    }

    public InputScalars getScalars() {
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
