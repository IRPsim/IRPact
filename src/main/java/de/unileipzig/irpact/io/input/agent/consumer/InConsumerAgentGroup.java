package de.unileipzig.irpact.io.input.agent.consumer;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Agenten", "Konsumer", "Gruppen"},
                priorities = {"-7", "0", "0"}
        )
)
public class InConsumerAgentGroup {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Agenten", "Konsumer", "Gruppe-Attribut-Mapping"},
                    priorities = {"-7", "0", "2"}
            )
    )
    public InConsumerAgentGroupAttribute[] cagAttributes;

    @FieldDefinition
    public double informationAuthority;

    @FieldDefinition
    public int numberOfAgents;

    public InConsumerAgentGroup() {
    }

    public InConsumerAgentGroup(String name, double informationAuthority, int numberOfAgents, Collection<? extends InConsumerAgentGroupAttribute> attributes) {
        this._name = name;
        this.informationAuthority = informationAuthority;
        this.numberOfAgents = numberOfAgents;
        this.cagAttributes = attributes.toArray(new InConsumerAgentGroupAttribute[0]);
    }

    public String getName() {
        return _name;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public InConsumerAgentGroupAttribute[] getAttributes() {
        return cagAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InConsumerAgentGroup)) return false;
        InConsumerAgentGroup that = (InConsumerAgentGroup) o;
        return Double.compare(that.informationAuthority, informationAuthority) == 0 && numberOfAgents == that.numberOfAgents && Objects.equals(_name, that._name) && Arrays.equals(cagAttributes, that.cagAttributes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name, informationAuthority, numberOfAgents);
        result = 31 * result + Arrays.hashCode(cagAttributes);
        return result;
    }

    @Override
    public String toString() {
        return "InConsumerAgentGroup{" +
                "_name='" + _name + '\'' +
                ", cagAttributes=" + Arrays.toString(cagAttributes) +
                ", informationAuthority=" + informationAuthority +
                ", numberOfAgents=" + numberOfAgents +
                '}';
    }
}
