package de.unileipzig.irpact.v2.io.input.agent.consumer;

import de.unileipzig.irpact.v2.io.input.awareness.IAwareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = {"Agents/Consumer/Groups"}
        )
)
public class IConsumerAgentGroup {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = {"Agents/Consumer/Group-Attribute-Mapping"}
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
