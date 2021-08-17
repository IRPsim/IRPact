package de.unileipzig.irpact.util.scenarios.demo;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.util.pvact.Milieu;
import de.unileipzig.irpact.util.scenarios.pvact.AbstractPVactScenario;

import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class FullPopulationDemoWithSelfLinks2015 extends AbstractPVactScenario {

    public static final int REVISION = 0;

    public FullPopulationDemoWithSelfLinks2015(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    @Override
    public List<InRoot> createInRootsOLD() {
        final InUnivariateDoubleDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);

        final InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");

        InConsumerAgentGroup[] cags = Milieu.ALL.stream()
                .map(milieu -> {
                    InPVactConsumerAgentGroup pvCag = new InPVactConsumerAgentGroup();
                    pvCag.setName(milieu.print());
                    pvCag.setSpatialDistribution(spatialDist);
                    pvCag.setForAll(dirac0);
                    return pvCag;
                })
                .toArray(InConsumerAgentGroup[]::new);

        InAffinities affinities = createSelfLinkedAffinities("affinities", cags);

        InFileBasedPVactConsumerAgentPopulation population = createFullPopulation("population", cags);

        InFreeNetworkTopology topology = createFreeTopology("topology", affinities, cags, 5);

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("time");

        InPVactGlobalDeffuantUncertainty uncertainty = createGlobalUnvertainty("uncert", cags);

        InRAProcessModel processModel = createDefaultProcessModel("process", uncertainty, 0.0);

        InSpace2D space2D = createSpace2D("space2d");

        InGenericOutputImage[] defaultImages = createDefaultImages();

        //=====
        InRoot root = createRootWithInformations();
        root.getGeneral().setFirstSimulationYear(2015);
        root.getGeneral().setLastSimulationYear(2015);
        root.setAffinities(affinities);
        root.setConsumerAgentGroups(cags);
        root.setAgentPopulationSize(population);
        root.setGraphTopologyScheme(topology);
        root.setProcessModel(processModel);
        root.setSpatialModel(space2D);
        root.setTimeModel(timeModel);
        root.setImages(defaultImages);
        root.getGraphvizGeneral().setPositionBasedLayoutAlgorithm(true);
        root.getGraphvizGeneral().setPreferredImageSize(1000);

        setColors(root, cags);

        return Collections.singletonList(root);
    }
}
