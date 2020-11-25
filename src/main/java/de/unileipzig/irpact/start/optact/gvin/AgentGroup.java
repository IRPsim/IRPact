package de.unileipzig.irpact.start.optact.gvin;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Graphviz", "Agentengruppe"},
                description = {"Graphviz Einstellungen", "Agentengruppe, welche im Graphen dargestellt werden sollen."}
        )
)
public class AgentGroup {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = "Anzahl der Agenten"
            )
    )
    public int numberOfAgents;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = "Farbe, welche diese Gruppe im Graphen haben soll. Wichtig: es wird nur der erste Wert verwendet! Falls keine Farbe gewählt wird, ist die Farbe schwarz."
            )
    )
    public GraphvizColor agentColor;

    public AgentGroup() {
    }

    public AgentGroup(String name, int numberOfAgents, GraphvizColor agentColor) {
        this._name = name;
        this.numberOfAgents = numberOfAgents;
        this.agentColor = agentColor;
    }
}

