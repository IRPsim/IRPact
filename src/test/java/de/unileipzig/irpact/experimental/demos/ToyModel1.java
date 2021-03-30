package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.ra.InPVactUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import org.junit.jupiter.api.Disabled;

import java.time.temporal.ChronoUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class ToyModel1 {

    private static InRoot getRoot() {
        //files
        InPVFile pvFile = new InPVFile("Barwertrechner");
        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");

        //dist
        InConstantUnivariateDistribution diraq0 = new InConstantUnivariateDistribution("diraq0", 0);
        InConstantUnivariateDistribution diraq07 = new InConstantUnivariateDistribution("diraq07", 0.7);
        InConstantUnivariateDistribution diraq1 = new InConstantUnivariateDistribution("diraq1", 1);

        InFileSpatialDistribution2D spatialDist = new InFileSpatialDistribution2D(
                "",
                new InAttributeName(RAConstants.X_CENT),
                new InAttributeName(RAConstants.Y_CENT),
                tableFile
        );

        //cag
        InPVactConsumerAgentGroup A = new InPVactConsumerAgentGroup();
        A.setName("A");
        A.a1 = diraq1;                                          //A1
        A.setNoveltySeeking(diraq1);                            //A2
        A.setDependentJudgmentMaking(diraq1);                   //A3
        A.setEnvironmentalConcern(diraq1);                      //A4
        A.a5 = diraq1;                                          //A5
        A.a6 = diraq1;                                          //A6
        A.setConstructionRate(diraq0);                          //A7
        A.setRenovationRate(diraq0);                            //A8

        A.setInitialProductAwareness(diraq1);                   //D1
        A.setInterestThreshold(diraq1);                         //D2
        A.setFinancialThreshold(diraq07);                       //D3
        A.setAdoptionThreshold(diraq07);                        //D4
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
        K.setDependentJudgmentMaking(diraq1);                   //A3
        K.setEnvironmentalConcern(diraq1);                      //A4
        K.a5 = diraq1;                                          //A5    //!
        K.a6 = diraq1;                                          //A6    //!
        K.setConstructionRate(diraq0);                          //A7
        K.setRenovationRate(diraq0);                            //A8

        K.setInitialProductAwareness(diraq0);                   //D1    //!
        K.setInterestThreshold(diraq1);                         //D2
        K.setFinancialThreshold(diraq07);                       //D3
        K.setAdoptionThreshold(diraq07);                        //D4
        K.setInitialAdopter(diraq0);                            //D5

        K.setRewire(diraq0);                                    //B6
        K.setCommunication(diraq0);                             //C1
        K.setRateOfConvergence(diraq0);                         //C3
        K.setInitialProductInterest(diraq0);                    //AX

        K.setSpatialDistribution(spatialDist);


        InComplexAffinityEntry[] entries = new InComplexAffinityEntry[]{
                new InComplexAffinityEntry("Affinity_A_K", A, K, 0),
                new InComplexAffinityEntry("Affinity_K_A", K, A, 0)
        };


        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("Unlinked_Topology");


        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel();
        timeModel.setName("Discrete_TimeModel");
        timeModel.setAmountOfTime(1);
        timeModel.setUnit(ChronoUnit.DAYS);


        InPVactUncertaintyGroupAttribute uncertainty = new InPVactUncertaintyGroupAttribute();
        uncertainty.setName("uncert");
        uncertainty.setGroups(new InConsumerAgentGroup[]{A, K});
        uncertainty.setForAll(diraq0);

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA_ProcessModel");
        processModel.setABCD(0.25);
        processModel.setDefaultPoints();
        processModel.setUncertaintyGroupAttribute(uncertainty);


        InGeneral general = new InGeneral();

        return null;
    }
}
