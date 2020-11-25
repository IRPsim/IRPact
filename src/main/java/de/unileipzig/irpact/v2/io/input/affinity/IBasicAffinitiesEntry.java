package de.unileipzig.irpact.v2.io.input.affinity;

import de.unileipzig.irpact.v2.io.input.agent.consumer.IConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Agents", "Consumer", "Groups", "Affinity", "Entry"}
        )
)
public class IBasicAffinitiesEntry {

    public String _name;

    @FieldDefinition
    public IConsumerAgentGroup from;

    @FieldDefinition
    public IConsumerAgentGroup to;

    @FieldDefinition
    public double affinityValue;

    public IBasicAffinitiesEntry() {
    }

    public IBasicAffinitiesEntry(String name, IConsumerAgentGroup from, IConsumerAgentGroup to, double affinityValue) {
        this._name = name;
        this.from = from;
        this.to = to;
        this.affinityValue = affinityValue;
    }
}
