package de.unileipzig.irpact.uitest.v1;

import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Agentengruppen",
                icon = "fa fa-map-marker",
                description = "Agentengruppen in der Simulation",
                useOwnSet = "1"
        )
)
public class AgentGroup {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    identifier = "Agentenanzahl",
                    description = "Anzahl der Agenten in der Gruppe",
                    domain = "[1,]",
                    defaultValue = "10"
            )
    )
    public int numberOfAgents;

    @FieldDefinition(
            gams = @GamsParameter(
                    identifier = "Adaptionsrate",
                    description = "Adaptionsrate für die Agenten dieser Gruppe",
                    domain = "[0,1]",
                    defaultValue = "0.5"
            ),
            edn = @EdnParameter(
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

    public static AgentGroup[] x() {
        return new AgentGroup[]{
                new AgentGroup("gx10x5", 10, 0.5),
                new AgentGroup("gx100x01", 100, 0.01),
                new AgentGroup("gx50x75", 50, 0.75)
        };
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
