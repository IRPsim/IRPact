package de.unileipzig.irpact.experimental.deprecated.input2.agent.consumer;

import de.unileipzig.irpact.experimental.deprecated.input2.InputResource;
import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {InputResource.ICONSUMERAGENTGROUP_LABEL},
                priorities = {InputResource.ICONSUMERAGENTGROUP_PRIORITIES},
                description = {InputResource.ICONSUMERAGENTGROUP_DESCRIPTION}
        )
)
public class IConsumerAgentGroup {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.ICONSUMERAGENTGROUP_CONSUMERAGENTINFORMATIONAUTHORITY
            )
    )
    public double consumerAgentInformationAuthority;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.ICONSUMERAGENTGROUP_NUMBEROFCONSUMERAGENTS
            )
    )
    public int numberOfConsumerAgents;

    public IConsumerAgentGroup() {
    }
}
