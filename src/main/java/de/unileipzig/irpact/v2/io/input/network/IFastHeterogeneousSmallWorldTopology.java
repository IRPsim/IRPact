package de.unileipzig.irpact.v2.io.input.network;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Network/HeterogeneousSmallWorld"}
        )
)
public class IFastHeterogeneousSmallWorldTopology {

    public String _name;

    @FieldDefinition
    public int edgeType;

    @FieldDefinition
    public boolean isSelfReferential;

    @FieldDefinition
    public double initialWeight;

    @FieldDefinition
    public long topoSeed;

    @FieldDefinition
    public IFastHeterogeneousSmallWorldTopologyEntry[] topoEntries;

    public IFastHeterogeneousSmallWorldTopology() {
    }
}
