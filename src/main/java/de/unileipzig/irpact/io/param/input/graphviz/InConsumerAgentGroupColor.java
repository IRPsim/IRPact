package de.unileipzig.irpact.io.param.input.graphviz;

import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InRandomBoundedIntegerDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public class InConsumerAgentGroupColor {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InConsumerAgentGroupColor.class,
                res.getCachedElement("Graphviz"),
                res.getCachedElement("Agentengruppe-Farben-Mapping")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Konsumergruppe")
                .setGamsDescription("Legt die Gruppe fest")
                .store(InConsumerAgentGroupColor.class, "group");

        res.newEntryBuilder()
                .setGamsIdentifier("Farbe")
                .setGamsDescription("Legt die Farbe fest")
                .store(InConsumerAgentGroupColor.class, "color");
    }

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] group;

    @FieldDefinition
    public GraphvizColor[] color;

    public InConsumerAgentGroupColor() {
    }

    public InConsumerAgentGroupColor(String name, InConsumerAgentGroup group, GraphvizColor color) {
        this._name = name;
        this.group = new InConsumerAgentGroup[]{group};
        this.color = new GraphvizColor[]{color};
    }

    public String getName() {
        return _name;
    }

    public InConsumerAgentGroup getGroup() {
        return group[0];
    }

    public GraphvizColor getColor() {
        return color[0];
    }
}
