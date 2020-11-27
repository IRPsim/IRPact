package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irpact.io.input.agent.consumer.IConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Network/HeterogeneousSmallWorld/Entry"}
        )
)
public class IFastHeterogeneousSmallWorldTopologyEntry {

    public String _name;

    @FieldDefinition
    public IConsumerAgentGroup topoGroup;

    @FieldDefinition
    public double beta;

    @FieldDefinition
    public int z;

    public IFastHeterogeneousSmallWorldTopologyEntry() {
    }

    public IFastHeterogeneousSmallWorldTopologyEntry(String name, IConsumerAgentGroup topoGroup, double beta, int z) {
        this._name = name;
        this.topoGroup = topoGroup;
        this.beta = beta;
        this.z = z;
    }
}
