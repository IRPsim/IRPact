package de.unileipzig.irpact.io.input.agent.consumer;

import de.unileipzig.irpact.io.input.awareness.IAwareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Agents/Consumer/Groups"}
        )
)
public class IConsumerAgentGroup {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Agents/Consumer/Group-Attribute-Mapping"}
            )
    )
    public IConsumerAgentGroupAttribute[] cagAttributes;

    @FieldDefinition
    public IAwareness productAwareness;

    @FieldDefinition
    public double informationAuthority;

    @FieldDefinition
    public int numberOfAgents;

    public IConsumerAgentGroup() {
    }

    public IConsumerAgentGroup(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }

    public IConsumerAgentGroupAttribute[] getCagAttributes() {
        return cagAttributes;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public IAwareness getProductAwareness() {
        return productAwareness;
    }
}
