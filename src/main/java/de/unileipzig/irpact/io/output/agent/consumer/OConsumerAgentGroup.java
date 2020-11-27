package de.unileipzig.irpact.io.output.agent.consumer;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Output"}
        )
)
public class OConsumerAgentGroup {

    public String _name;

    @FieldDefinition
    public double adaptionRate;

    public OConsumerAgentGroup() {
    }

    public OConsumerAgentGroup(String name, double adaptionRate) {
        this._name = name;
        this.adaptionRate = adaptionRate;
    }

    public String getName() {
        return _name;
    }

    public double getAdaptionRate() {
        return adaptionRate;
    }
}
