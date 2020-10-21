package de.unileipzig.irpact.v2.io.input.agent.consumer;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = "Agents/Consumer/Groups"
        )
)
public class IConsumerAgentGroup {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "Agents/Consumer/Group-Attribute-Mapping"
            )
    )
    public IConsumerAgentGroupAttribute[] attributes;

    public IConsumerAgentGroup() {
    }

    public String getName() {
        return _name;
    }

    public IConsumerAgentGroupAttribute[] getAttributes() {
        return attributes;
    }
}
