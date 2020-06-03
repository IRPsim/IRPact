package de.unileipzig.irpact.start.hardcodeddemo.def.in;

import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Agentengruppen",
                icon = "",
                description = "Agentengruppen in der Simulation"
        )
)
public class AgentGroup {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    domain = "[1,]",
                    defaultValue = "10"
            ),
            edn = @EdnParameter(
                    description = "Anzahl der Agenten in der Gruppe"
            )
    )
    public int numberOfAgents;

    @FieldDefinition(
            gams = @GamsParameter(
                    domain = "[0,1]",
                    defaultValue = "0.5"
            ),
            edn = @EdnParameter(
                    description = "Adaptionsrate für die Agenten dieser Gruppe",
                    delta = "1"
            )
    )
    public double adaptionRate;

    public AgentGroup() {
    }

    public AgentGroup(String name, int numberOfAgents, double adaptionRate) {
        this._name = name;
        this.numberOfAgents = numberOfAgents;
        this.adaptionRate = adaptionRate;
    }

    public String getName() {
        return _name;
    }

    public double getAdaptionRate() {
        return adaptionRate;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    @Override
    public String toString() {
        return "AgentGroup{" +
                "_name='" + _name + '\'' +
                ", numberOfAgents=" + numberOfAgents +
                ", adaptionRate=" + adaptionRate +
                '}';
    }
}
