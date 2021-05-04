package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
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
import de.unileipzig.irpact.io.param.input.network.*;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InPVactUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileSelectedSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class ToyModel71 {

    @Test
    void peekData() throws IOException {
        Table<SpatialAttribute> data = SpatialTableFileLoader.parseXlsx(Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data", "testdaten_Test_7_Y.xlsx"));
        for(List<SpatialAttribute> row: data.listTable()) {
            //System.out.println(row);
            SpatialAttribute sa = row.get(9);
            System.out.println(sa + " " + sa.asValueAttribute().getDataTypes());
        }
    }

    private static InRoot createRoot() {
        //files
        InPVFile pvFile = new InPVFile("Barwertrechner");
        InSpatialTableFile tableFile = new InSpatialTableFile("testdaten_Test_7_Y");

        //dist
        InConstantUnivariateDistribution diraq0 = new InConstantUnivariateDistribution("diraq0", 0);
        InConstantUnivariateDistribution diraq085 = new InConstantUnivariateDistribution("diraq085", 0.85);
        InConstantUnivariateDistribution diraq1 = new InConstantUnivariateDistribution("diraq1", 1);

        InFileSelectedSpatialDistribution2D spatialDist = new InFileSelectedSpatialDistribution2D(
                "SpatialDist",
                new InAttributeName(RAConstants.X_CENT),
                new InAttributeName(RAConstants.Y_CENT),
                tableFile,
                new InAttributeName(RAConstants.DOM_MILIEU)
        );

        //cag
        InPVactConsumerAgentGroup S = ToyModelUtil.createFeasiblePVcag("S", null, diraq0, diraq1);
        S.setSpatialDistribution(spatialDist);
        S.setDependentJudgmentMaking(diraq1);
        S.setInitialAdopter(diraq1);
        S.setAdoptionThreshold(diraq085);

        InPVactConsumerAgentGroup A = ToyModelUtil.createFeasiblePVcag("A", null, diraq0, diraq1);
        A.setSpatialDistribution(spatialDist);
        A.setDependentJudgmentMaking(diraq1);
        A.setInitialAdopter(diraq0);
        A.setAdoptionThreshold(diraq085);

        InPVactConsumerAgentGroup K = ToyModelUtil.createFeasiblePVcag("K", null, diraq0, diraq1);
        K.setSpatialDistribution(spatialDist);
        K.setDependentJudgmentMaking(diraq0);
        K.setInitialAdopter(diraq0);
        K.setAdoptionThreshold(diraq085);

        InPVactConsumerAgentGroup H = ToyModelUtil.createFeasiblePVcag("H", null, diraq0, diraq1);
        H.setSpatialDistribution(spatialDist);
        H.setDependentJudgmentMaking(diraq0);
        H.setInitialAdopter(diraq0);
        H.setAdoptionThreshold(diraq085);


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
        InFixConsumerAgentPopulationSize sizeS = new InFixConsumerAgentPopulationSize("size_S", S, 5);
        InFixConsumerAgentPopulationSize sizeA = new InFixConsumerAgentPopulationSize("size_A", A, 5);
        InFixConsumerAgentPopulationSize sizeK = new InFixConsumerAgentPopulationSize("size_K", K, 2);
        InFixConsumerAgentPopulationSize sizeH = new InFixConsumerAgentPopulationSize("size_H", H, 2);

        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName("freetopo");
        topology.setInitialWeight(1.0);
        topology.setDistanceEvaluator(new InNoDistance("No_dist"));
        topology.setNumberOfTies(
                new InNumberOfTies("tieS", S, 1),
                new InNumberOfTies("tieA", A, 5),
                new InNumberOfTies("tieK", K, 1),
                new InNumberOfTies("tieH", H, 1)
        );
        topology.setAllowLessEdges(false);


        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel();
        timeModel.setName("Discrete_TimeModel");
        timeModel.setAmountOfTime(1);
        timeModel.setUnit(ChronoUnit.WEEKS);


        InPVactUncertaintyGroupAttribute uncertainty = new InPVactUncertaintyGroupAttribute();
        uncertainty.setName("uncert");
        uncertainty.setGroups(new InConsumerAgentGroup[]{S, A, K, H});
        uncertainty.setForAll(diraq0);

        InRAProcessPlanNodeFilterScheme nodeFilterScheme = new InRAProcessPlanMaxDistanceFilterScheme(
                "RA_max_distance",
                100,
                false
        );

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA_ProcessModel");
        processModel.setABCD(0);
        processModel.setA(0.5);
        processModel.setD(0.5);
        processModel.setDefaultPoints();
        processModel.setLogisticFactor(RAConstants.DEFAULT_LOGISTIC_FACTOR);
        processModel.setUncertaintyGroupAttribute(uncertainty);
        processModel.setPvFile(pvFile);
        processModel.setNodeFilterScheme(nodeFilterScheme);


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
        ToyModelUtil.run("test_7_1", createRoot());
    }
}
