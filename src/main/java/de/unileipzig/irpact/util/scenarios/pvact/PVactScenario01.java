package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InPVactUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class PVactScenario01 extends AbstractPVactScenario {

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);

    public PVactScenario01(String name, String creator, String description) {
        super(name, creator, description);
        setTotalAgents(100);
    }

    public PVactScenario01(String name, String creator, String description, Path logPath, Path outputDir, Path downloadDir) {
        super(name, creator, description, logPath, outputDir, downloadDir);
        setTotalAgents(100);
    }

    protected InPVactConsumerAgentGroup createAgentGroup(String name, InSpatialDistribution dist) {
        InPVactConsumerAgentGroup grp = new InPVactConsumerAgentGroup();
        grp.setName(name);

        //A1 in file
        grp.setNoveltySeeking(dirac0);                            //A2
        grp.setDependentJudgmentMaking(dirac0);                   //A3
        grp.setEnvironmentalConcern(dirac0);                      //A4
        //A5 in file
        //A6 in file
        grp.setConstructionRate(dirac0);                          //A7
        grp.setRenovationRate(dirac0);                            //A8

        grp.setRewire(dirac0);                                    //B6

        grp.setCommunication(dirac0);                             //C1
        grp.setRateOfConvergence(dirac0);                         //C3

        grp.setInitialProductAwareness(dirac0);                   //D1
        grp.setInterestThreshold(dirac0);                         //D2
        grp.setFinancialThreshold(dirac0);                       //D3
        grp.setAdoptionThreshold(dirac0);                        //D4
        grp.setInitialAdopter(dirac0);                            //D5
        grp.setInitialProductInterest(dirac0);                    //D6

        grp.setSpatialDistribution(dist);

        return grp;
    }

    @Override
    public List<InRoot> createInRoots() {
        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");

        InPVactConsumerAgentGroup[] cags = new InPVactConsumerAgentGroup[]{
                createAgentGroup("BUM", spatialDist),
                createAgentGroup("EPE", spatialDist),
                createAgentGroup("G", spatialDist),
                createAgentGroup("HED", spatialDist),
                createAgentGroup("KET", spatialDist),
                createAgentGroup("LIB", spatialDist),
                createAgentGroup("PER", spatialDist),
                createAgentGroup("PRA", spatialDist),
                createAgentGroup("PRE", spatialDist),
                createAgentGroup("SOK", spatialDist),
                createAgentGroup("TRA", spatialDist)
        };

        InAffinities affinities = createZeroAffinities("affinities", cags);

        InFileBasedPVactConsumerAgentPopulation population = createPopulation("Pop", getTotalAgents(), cags);

        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("Topo");

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");

        InPVactUncertaintyGroupAttribute uncertainty = createDefaultUnvertainty("uncert", dirac0, cags);

        InRAProcessModel processModel = createDefaultProcessModel("Process", uncertainty);

        InSpace2D space2D = createSpace2D("Space2D");

        //=====
        InGeneral general = createGeneralPart();
        general.setFirstSimulationYear(2015);
        general.lastSimulationYear = 2015;

        InRoot root = new InRoot();
        root.version = new InVersion[]{InVersion.currentVersion()};
        root.general = general;
        root.setAffinities(affinities);
        root.setConsumerAgentGroups(cags);
        root.setAgentPopulationSize(population);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return Collections.singletonList(root);
    }
}
