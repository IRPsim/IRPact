package de.unileipzig.irpact.v2.io.input2.network;

import de.unileipzig.irpact.v2.commons.Util;
import de.unileipzig.irpact.v2.commons.graph.topology.GraphTopology;
import de.unileipzig.irpact.v2.commons.graph.topology.WattsStrogatzModel;
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
                label = {InputResource.IWATTSSTROGATZMODEL_LABEL},
                priorities = {InputResource.IWATTSSTROGATZMODEL_PRIORITIES},
                description = {InputResource.IWATTSSTROGATZMODEL_DESCRIPTION}
        )
)
public class IWattsStrogatzModel implements IGraphTopology {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.IWATTSSTROGATZMODEL_WSMK
            )
    )
    public int wsmK;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.IWATTSSTROGATZMODEL_WSMBETA
            )
    )
    public double wsmBeta;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.IWATTSSTROGATZMODEL_WSMSELFREFERENTIAL
            )
    )
    public boolean wsmSelfReferential;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = InputResource.IWATTSSTROGATZMODEL_WSMSEED
            )
    )
    public long wsmSeed;

    public IWattsStrogatzModel() {
    }

    @Override
    public <V, E> WattsStrogatzModel<V, E, ?> createInstance() {
        WattsStrogatzModel<V, E, ?> wsm = new WattsStrogatzModel<>();
        wsm.setSelfReferential(wsmSelfReferential);
        wsm.setBeta(wsmBeta);
        wsm.setK(wsmK);
        long seed = Util.getRandomSeedIf0(wsmSeed);
        wsm.setRandom(new Random(seed));
        return wsm;
    }
}
