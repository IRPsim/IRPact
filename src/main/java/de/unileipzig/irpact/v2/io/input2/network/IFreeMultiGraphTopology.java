package de.unileipzig.irpact.v2.io.input2.network;

import de.unileipzig.irpact.v2.commons.Util;
import de.unileipzig.irpact.v2.commons.graph.topology.FreeMultiGraphTopology;
import de.unileipzig.irpact.v2.io.input2.InputResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {InputResource.IFREEMULTIGRAPHTOPOLOGY_LABEL},
                priorities = {InputResource.IFREEMULTIGRAPHTOPOLOGY_PRIORITIES},
                description = {InputResource.IFREEMULTIGRAPHTOPOLOGY_DESCRIPTION}
        )
)
public class IFreeMultiGraphTopology implements IGraphTopology {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.IFREEMULTIGRAPHTOPOLOGY_FTEDGECOUNT
            )
    )
    public int ftEdgeCount;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.IFREEMULTIGRAPHTOPOLOGY_FTSELFREFERENTIAL
            )
    )
    public boolean ftSelfReferential;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.IFREEMULTIGRAPHTOPOLOGY_FTSEED
            )
    )
    public long ftSeed;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = "Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!"
            )
    )
    public boolean ftUseThis;

    public IFreeMultiGraphTopology() {
    }

    @Override
    public boolean use() {
        return ftUseThis;
    }

    @Override
    public <V, E> FreeMultiGraphTopology<V, E, ?> createInstance() {
        FreeMultiGraphTopology<V, E, ?> ft = new FreeMultiGraphTopology<>();
        ft.setEdgeCount(ftEdgeCount);
        ft.setSelfReferential(ftSelfReferential);
        long seed = Util.getRandomSeedIf0(ftSeed);
        ft.setRandom(new Random(seed));
        return ft;
    }
}
