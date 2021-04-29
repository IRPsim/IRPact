package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.agent.population.InPopulationSize;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InPVactUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileSelectedSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class ToyModel76 {

    private static InRoot createRoot() {
        //files
        InPVFile pvFile = new InPVFile("Barwertrechner");
        InSpatialTableFile tableFile = new InSpatialTableFile("testdaten_Test_7_Y");

        //dist
        InConstantUnivariateDistribution diraq0 = new InConstantUnivariateDistribution("diraq0", 0);
        InConstantUnivariateDistribution diraq06 = new InConstantUnivariateDistribution("diraq06", 0.6);
        InConstantUnivariateDistribution diraq07 = new InConstantUnivariateDistribution("diraq07", 0.7);
        InConstantUnivariateDistribution diraq1 = new InConstantUnivariateDistribution("diraq1", 1);

        InFileSelectedSpatialDistribution2D spatialDist = new InFileSelectedSpatialDistribution2D(
                "SpatialDist",
                new InAttributeName(RAConstants.X_CENT),
                new InAttributeName(RAConstants.Y_CENT),
                tableFile,
                new InAttributeName(RAConstants.DOM_MILIEU)
        );

        //cag
        InPVactConsumerAgentGroup S = ToyModelUtil.createNullPVcag("S", diraq0);
        S.setSpatialDistribution(spatialDist);

        InPVactConsumerAgentGroup A = ToyModelUtil.createNullPVcag("A", diraq0);
        A.setSpatialDistribution(spatialDist);

        InPVactConsumerAgentGroup K = ToyModelUtil.createNullPVcag("K", diraq0);
        K.setSpatialDistribution(spatialDist);

        InPVactConsumerAgentGroup H = ToyModelUtil.createNullPVcag("H", diraq0);
        H.setSpatialDistribution(spatialDist);


        InComplexAffinityEntry[] affinities = new InComplexAffinityEntry[]{
                new InComplexAffinityEntry("Affinity_A_A", A, A, 0),
                new InComplexAffinityEntry("Affinity_A_S", A, S, 1),
                new InComplexAffinityEntry("Affinity_A_K", A, K, 0),
                new InComplexAffinityEntry("Affinity_A_H", A, H, 0),

                new InComplexAffinityEntry("Affinity_S_A", S, A, 0),
                new InComplexAffinityEntry("Affinity_S_S", S, S, 1),
                new InComplexAffinityEntry("Affinity_S_K", S, K, 0),
                new InComplexAffinityEntry("Affinity_S_H", S, H, 0),

                new InComplexAffinityEntry("Affinity_K_A", K, A, 0),
                new InComplexAffinityEntry("Affinity_K_S", K, S, 0),
                new InComplexAffinityEntry("Affinity_K_K", K, K, 1),
                new InComplexAffinityEntry("Affinity_K_H", K, H, 0),

                new InComplexAffinityEntry("Affinity_H_A", H, A, 0),
                new InComplexAffinityEntry("Affinity_H_S", H, S, 0),
                new InComplexAffinityEntry("Affinity_H_K", H, K, 0),
                new InComplexAffinityEntry("Affinity_H_H", H, H, 1)
        };


        //Population
        InFixConsumerAgentPopulationSize sizeS = new InFixConsumerAgentPopulationSize("size_S", S, 1);
        InFixConsumerAgentPopulationSize sizeA = new InFixConsumerAgentPopulationSize("size_A", A, 1);
        InFixConsumerAgentPopulationSize sizeK = new InFixConsumerAgentPopulationSize("size_K", K, 1);
        InFixConsumerAgentPopulationSize sizeH = new InFixConsumerAgentPopulationSize("size_H", H, 1);


        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("Unlinked_Topology");


        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel();
        timeModel.setName("Discrete_TimeModel");
        timeModel.setAmountOfTime(1);
        timeModel.setUnit(ChronoUnit.WEEKS);


        InPVactUncertaintyGroupAttribute uncertainty = new InPVactUncertaintyGroupAttribute();
        uncertainty.setName("uncert");
        uncertainty.setGroups(new InConsumerAgentGroup[]{A, H});
        uncertainty.setForAll(diraq0);

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA_ProcessModel");
        processModel.setABCD(0);
        processModel.setA(1);
        processModel.setDefaultPoints();
        processModel.setLogisticFactor(1.0 / 8.0);
        processModel.setUncertaintyGroupAttribute(uncertainty);
        processModel.setPvFile(pvFile);


        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.HAVERSINE_KM);


        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.logLevel = IRPLevel.ALL.getLevelId();
        general.logAllIRPact = true;
        general.enableAllDataLogging();
        general.enableAllResultLogging();
        general.firstSimulationYear = 2020;
        general.lastSimulationYear = 2020;

        //=====
        InRoot root = new InRoot();
        root.version = new InVersion[]{InVersion.currentVersion()};
        root.general = general;
        root.affinityEntries = affinities;
        root.consumerAgentGroups = new InConsumerAgentGroup[]{S, A, H, K};
        root.setAgentPopulationSizes(new InPopulationSize[]{sizeS, sizeA, sizeH, sizeK});
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return root;
    }

    @Test
    void runThisModel() {
        ToyModelUtil.run("test_7_6", createRoot());
    }
}
