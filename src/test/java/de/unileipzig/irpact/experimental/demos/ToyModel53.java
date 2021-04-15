package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InNoDistance;
import de.unileipzig.irpact.io.param.input.network.InNumberOfTies;
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

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class ToyModel53 {

    private static InRoot createRoot() {
        //files
        InPVFile pvFile = new InPVFile("Barwertrechner");
        InSpatialTableFile tableFile = new InSpatialTableFile("testdaten_Test_5_3");

        //dist
        InConstantUnivariateDistribution diraq0 = new InConstantUnivariateDistribution("diraq0", 0);
        InConstantUnivariateDistribution diraq07 = new InConstantUnivariateDistribution("diraq07", 0.7);
        InConstantUnivariateDistribution diraq1 = new InConstantUnivariateDistribution("diraq1", 1);
        InConstantUnivariateDistribution diraqX = new InConstantUnivariateDistribution("diraqX", 0.3);

        InFileSelectedSpatialDistribution2D spatialDist = new InFileSelectedSpatialDistribution2D(
                "SpatialDist",
                new InAttributeName(RAConstants.X_CENT),
                new InAttributeName(RAConstants.Y_CENT),
                tableFile,
                new InAttributeName(RAConstants.DOM_MILIEU)
        );

        //cag
        InPVactConsumerAgentGroup S = new InPVactConsumerAgentGroup();
        S.setName("S");
        S.a1 = diraq1;                                          //A1
        S.setNoveltySeeking(diraq1);                            //A2
        S.setDependentJudgmentMaking(diraq1);                   //A3
        S.setEnvironmentalConcern(diraq1);                      //A4
        S.a5 = diraq1;                                          //A5
        S.a6 = diraq1;                                          //A6
        S.setConstructionRate(diraq0);                          //A7
        S.setRenovationRate(diraq0);                            //A8

        S.setInitialProductAwareness(diraq1);                   //D1    //!
        S.setInterestThreshold(diraq0);                         //D2
        S.setFinancialThreshold(diraq07);                       //D3
        S.setAdoptionThreshold(diraqX);                         //D4
        S.setInitialAdopter(diraq1);                            //D5

        S.setRewire(diraq0);                                    //B6
        S.setCommunication(diraq0);                             //C1
        S.setRateOfConvergence(diraq0);                         //C3
        S.setInitialProductInterest(diraq0);                    //AX       //!!!

        S.setSpatialDistribution(spatialDist);


        InPVactConsumerAgentGroup A = new InPVactConsumerAgentGroup();
        A.setName("A");
        A.a1 = diraq1;                                          //A1
        A.setNoveltySeeking(diraq1);                            //A2
        A.setDependentJudgmentMaking(diraq1);                   //A3
        A.setEnvironmentalConcern(diraq1);                      //A4
        A.a5 = diraq1;                                          //A5    //!
        A.a6 = diraq1;                                          //A6    //!
        A.setConstructionRate(diraq0);                          //A7
        A.setRenovationRate(diraq0);                            //A8

        A.setInitialProductAwareness(diraq1);                   //D1    //!
        A.setInterestThreshold(diraq0);                         //D2
        A.setFinancialThreshold(diraq07);                       //D3
        A.setAdoptionThreshold(diraqX);                         //D4
        A.setInitialAdopter(diraq0);                            //D5

        A.setRewire(diraq0);                                    //B6
        A.setCommunication(diraq0);                             //C1
        A.setRateOfConvergence(diraq0);                         //C3
        A.setInitialProductInterest(diraq0);                    //AX

        A.setSpatialDistribution(spatialDist);


        InPVactConsumerAgentGroup K = new InPVactConsumerAgentGroup();
        K.setName("K");
        K.a1 = diraq1;                                          //A1
        K.setNoveltySeeking(diraq1);                            //A2
        K.setDependentJudgmentMaking(diraq0);                   //A3
        K.setEnvironmentalConcern(diraq1);                      //A4
        K.a5 = diraq1;                                          //A5    //!
        K.a6 = diraq1;                                          //A6    //!
        K.setConstructionRate(diraq0);                          //A7
        K.setRenovationRate(diraq0);                            //A8

        K.setInitialProductAwareness(diraq1);                   //D1    //!
        K.setInterestThreshold(diraq0);                         //D2
        K.setFinancialThreshold(diraq07);                       //D3
        K.setAdoptionThreshold(diraqX);                         //D4
        K.setInitialAdopter(diraq0);                            //D5

        K.setRewire(diraq0);                                    //B6
        K.setCommunication(diraq0);                             //C1
        K.setRateOfConvergence(diraq0);                         //C3
        K.setInitialProductInterest(diraq0);                    //AX

        K.setSpatialDistribution(spatialDist);

        InConsumerAgentGroup[] cags = new InConsumerAgentGroup[]{S, A, K};

        InAffinityEntry[] affinities = new InAffinityEntry[]{
                new InNameSplitAffinityEntry(A, A, 0),
                new InNameSplitAffinityEntry(A, S, 1),
                new InNameSplitAffinityEntry(A, K, 0),

                new InNameSplitAffinityEntry(S, A, 0),
                new InNameSplitAffinityEntry(S, S, 1),
                new InNameSplitAffinityEntry(S, K, 0),

                new InNameSplitAffinityEntry(K, A, 0),
                new InNameSplitAffinityEntry(K, S, 1),
                new InNameSplitAffinityEntry(K, K, 0)
        };


        //Population
        InFixConsumerAgentPopulationSize populationSize = new InFixConsumerAgentPopulationSize();
        populationSize.setName("PopSize");
        populationSize.setSize(2);
        populationSize.setConsumerAgentGroups(cags);

        //Topologie
        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName("free_topo");
        topology.setDistanceEvaluator(new InNoDistance("No_dist"));
        topology.setInitialWeight(1.0);
        topology.setNumberOfTies(
                new InNumberOfTies("S_tie", S, 1),
                new InNumberOfTies("A_tie", A, 1),
                new InNumberOfTies("K_tie", K, 1)
        );


        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel();
        timeModel.setName("Discrete_TimeModel");
        timeModel.setAmountOfTime(1);
        timeModel.setUnit(ChronoUnit.WEEKS);


        InPVactUncertaintyGroupAttribute uncertainty = new InPVactUncertaintyGroupAttribute();
        uncertainty.setName("uncert");
        uncertainty.setGroups(cags);
        uncertainty.setForAll(diraq0);

        InRAProcessPlanNodeFilterScheme nodeFilterScheme = new InRAProcessPlanMaxDistanceFilterScheme(
                "RA_max_distance",
                2,
                false
        );

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA_ProcessModel");
        processModel.setABCD(0);
        processModel.setD(1);
        processModel.setDefaultPoints();
        processModel.setLogisticFactor(1.0 / 8.0);
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
        general.firstSimulationYear = 2015;
        general.lastSimulationYear = 2015;

        //=====
        InRoot root = new InRoot();
        root.version = new InVersion[]{InVersion.currentVersion()};
        root.general = general;
        root.affinityEntries = affinities;
        root.consumerAgentGroups = cags;
        root.setAgentPopulationSize(populationSize);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return root;
    }

    @Test
    void runThisModel() {
        ToyModelUtil.run("test_5_3", createRoot());
    }
}
