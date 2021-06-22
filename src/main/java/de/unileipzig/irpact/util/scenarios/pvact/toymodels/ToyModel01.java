package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
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
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.output.OutRoot;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel01 extends AbstractToyModel {

    public static final int SIZE_A = 10;
    public static final int SIZE_K = 10;

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected InDiracUnivariateDistribution dirac07 = new InDiracUnivariateDistribution("dirac07", 0.7);
    protected InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);

    public ToyModel01(String name, BiConsumer<InRoot, OutRoot> resultConsumer) {
        this(name, null, null, resultConsumer);
    }

    public ToyModel01(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setTotalAgents(SIZE_A + SIZE_K);
    }

    public List<List<SpatialAttribute>> buildData(
            List<List<SpatialAttribute>> input,
            int sizeOfA, int sizeOfK,
            Random rnd) {
        List<List<SpatialAttribute>> output = SpatialUtil.drawRandom(input, sizeOfA + sizeOfK, rnd);
        int from = 0;
        int to = 0;
        //A
        to += sizeOfA;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //K
        from += sizeOfA;
        to += sizeOfK;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac0.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }

        return output;
    }

    public void buildData(
            Path xlsxInput, Path xlsxOutput,
            int sizeOfA, int sizeOfK,
            Random rnd) throws ParsingException, IOException, InvalidFormatException {
        Table<SpatialAttribute> inputData = SpatialTableFileLoader.parseXlsx(xlsxInput);
        List<List<SpatialAttribute>> outputList = buildData(inputData.listTable(), sizeOfA, sizeOfK, rnd);
        Table<SpatialAttribute> outputData = inputData.emptyCopyWithSameHeader();
        outputData.addRows(outputList);
        SpatialTableFileLoader.writeXlsx(xlsxOutput, "Data", "Daten fuer " + getName(), outputData);
    }

    protected InPVactConsumerAgentGroup createAgentGroup(String name) {
        InPVactConsumerAgentGroup grp = new InPVactConsumerAgentGroup();
        grp.setName(name);

        //A1 in file
        grp.setNoveltySeeking(dirac1);                            //A2
        grp.setDependentJudgmentMaking(dirac1);                   //A3
        grp.setEnvironmentalConcern(dirac1);                      //A4
        //A5 in file
        //A6 in file
        grp.setConstructionRate(dirac0);                          //A7
        grp.setRenovationRate(dirac0);                            //A8

        grp.setRewire(dirac0);                                    //B6

        grp.setCommunication(dirac0);                             //C1
        grp.setRateOfConvergence(dirac0);                         //C3

        grp.setInitialProductAwareness(null);                     //D1
        grp.setInterestThreshold(dirac1);                         //D2
        grp.setFinancialThreshold(dirac07);                       //D3
        grp.setAdoptionThreshold(dirac07);                        //D4
        grp.setInitialAdopter(dirac0);                            //D5
        grp.setInitialProductInterest(dirac1);                    //D6

        return grp;
    }

    @Override
    public List<InRoot> createInRoots() {
        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");

        InPVactConsumerAgentGroup A = createAgentGroup("A");
        A.setInitialProductAwareness(dirac1);                     //D1
        A.setSpatialDistribution(spatialDist);

        InPVactConsumerAgentGroup K = createAgentGroup("K");
        K.setInitialProductAwareness(dirac0);                     //D1
        K.setSpatialDistribution(spatialDist);

        InAffinities affinities = createZeroAffinities("affinities", A, K);

        InFileBasedPVactConsumerAgentPopulation population = createPopulation("Pop", getTotalAgents(), A, K);

        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("Topo");

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");

        InPVactUncertaintyGroupAttribute uncertainty = createDefaultUnvertainty("uncert", dirac0, A, K);

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
        root.setConsumerAgentGroups(new InConsumerAgentGroup[]{A, K});
        root.setAgentPopulationSize(population);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return Collections.singletonList(root);
    }
}
