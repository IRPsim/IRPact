package de.unileipzig.irpact.io.input.graphviz;

import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Graphviz", "Agentengruppe-Farben-Mapping"},
                description = {"Graphviz Einstellungen", "Agentengruppe, welche im Graphen dargestellt werden sollen."}
        )
)
public class InConsumerAgentGroupColor {

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
