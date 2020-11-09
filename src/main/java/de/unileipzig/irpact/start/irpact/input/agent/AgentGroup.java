package de.unileipzig.irpact.start.irpact.input.agent;

import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irptools.defstructure.annotation.*;

import java.util.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "AgentGroup"
        )
)
public class AgentGroup {

    public String _name;

    @FieldDefinition
    public int numberOfAgents;

    @FieldDefinition
    public double adoptionRate;

    @FieldDefinition(
            gams = @GamsParameter(
                    identifier = "seedAgentGroup"
            )
    )
    public long seed;

    @FieldDefinition(
            gams = @GamsParameter(
                    identifier = "needsAgentGroup"
            ),
            edn = @EdnParameter(
                    label = "Link/AgentGroup-Need"
            )
    )
    public Need[] needs;

    public AgentGroup() {
    }

    public AgentGroup(String name, int numberOfAgents, double adoptionRate, long seed, Need[] needs) {
        this._name = name;
        this.numberOfAgents = numberOfAgents;
        this.adoptionRate = adoptionRate;
        this.seed = seed;
        this.needs = needs;
    }

    //=========================
    //helper
    //=========================

    public Set<Need> getNeeds() {
        Set<Need> needSet = new LinkedHashSet<>();
        Collections.addAll(needSet, needs);
        return needSet;
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentGroup that = (AgentGroup) o;
        return numberOfAgents == that.numberOfAgents &&
                adoptionRate == that.adoptionRate &&
                seed == that.seed &&
                Objects.equals(_name, that._name) &&
                Arrays.equals(needs, that.needs);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name, numberOfAgents, adoptionRate, seed);
        result = 31 * result + Arrays.hashCode(needs);
        return result;
    }

    @Override
    public String toString() {
        return "AgentGroup{" +
                "_name='" + _name + '\'' +
                ", numberOfAgents=" + numberOfAgents +
                ", adoptionRate=" + adoptionRate +
                ", seed=" + seed +
                ", needs=" + Arrays.toString(needs) +
                '}';
    }
}
