package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InScenarioVersion;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InBernoulliDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
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
public class ToyModel_D_B1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public static final int SIZE_S = 100;
    public static final int SIZE_P = 300;
    public static final int SIZE_M = 1600;
    public static final int SIZE_L = 500;

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected InDiracUnivariateDistribution dirac049 = new InDiracUnivariateDistribution("dirac049", 0.49);
    protected InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);
    protected InDiracUnivariateDistribution dirac100 = new InDiracUnivariateDistribution("dirac100", 100);

    protected InBernoulliDistribution bernoulli1 = new InBernoulliDistribution("bernoulli1", 1);
    protected InBernoulliDistribution bernoulli01 = new InBernoulliDistribution("bernoulli01", 0.1);
    protected InBernoulliDistribution bernoulli03 = new InBernoulliDistribution("bernoulli03", 0.3);
    protected InBernoulliDistribution bernoulli07 = new InBernoulliDistribution("bernoulli07", 0.7);

    public ToyModel_D_B1(BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(resultConsumer);
        setRevision(REVISION);
        setTotalAgents(SIZE_S + SIZE_P + SIZE_M + SIZE_L);
    }

    public ToyModel_D_B1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
        setTotalAgents(SIZE_S + SIZE_P + SIZE_M + SIZE_L);
    }

    public List<List<SpatialAttribute>> buildData(
            List<List<SpatialAttribute>> input,
            int sizeOfS, int sizeOfP, int sizeOfM, int sizeOfL,
            Random rnd) {
        List<List<SpatialAttribute>> output = SpatialUtil.drawRandom(input, sizeOfS + sizeOfP + sizeOfM + sizeOfL, rnd);
        int from = 0;
        int to = 0;
        //A
        to += sizeOfS;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "S");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac100.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //K1
        from += sizeOfS;
        to += sizeOfP;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "P");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac100.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //K2
        from += sizeOfP;
        to += sizeOfM;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "M");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac100.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //K3
        from += sizeOfM;
        to += sizeOfL;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "L");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac100.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }

        return output;
    }

    public void buildData(
            Path xlsxInput, Path xlsxOutput,
            int sizeOfS, int sizeOfP, int sizeOfM, int sizeOfL,
            Random rnd) throws ParsingException, IOException, InvalidFormatException {
        Table<SpatialAttribute> inputData = SpatialTableFileLoader.parseXlsx(xlsxInput);
        List<List<SpatialAttribute>> outputList = buildData(inputData.listTable(), sizeOfS, sizeOfP, sizeOfM, sizeOfL, rnd);
        Table<SpatialAttribute> outputData = inputData.emptyCopyWithSameHeader();
        outputData.addRows(outputList);
        SpatialTableFileLoader.writeXlsx(xlsxOutput, "Data", "Daten fuer " + getName(), outputData);
    }

    protected InPVactConsumerAgentGroup createAgentGroup(String name) {
        InPVactConsumerAgentGroup grp = new InPVactConsumerAgentGroup();
        grp.setName(name);

        //A1 in file
        grp.setNoveltySeeking(dirac1);                            //A2
        grp.setDependentJudgmentMaking(dirac0);                   //A3
        grp.setEnvironmentalConcern(dirac0);                      //A4
        //A5 in file
        //A6 in file
        grp.setConstructionRate(dirac0);                          //A7
        grp.setRenovationRate(dirac0);                            //A8

        grp.setRewire(dirac0);                                    //B6

        grp.setCommunication(dirac0);                             //C1
        grp.setRateOfConvergence(dirac0);                         //C3

        grp.setInitialProductAwareness(dirac1);                   //D1
        grp.setInterestThreshold(dirac1);                         //D2
        grp.setFinancialThreshold(dirac049);                       //D3
        grp.setAdoptionThreshold(dirac049);                        //D4
        grp.setInitialAdopter(dirac0);                            //D5
        grp.setInitialProductInterest(dirac1);                    //D6

        return grp;
    }

    @Override
    public List<InRoot> createInRoots() {
        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");

        InPVactConsumerAgentGroup S = createAgentGroup("S");
        S.setSpatialDistribution(spatialDist);
        S.setInitialProductAwareness(bernoulli1);
        S.setInitialAdopter(bernoulli1);

        InPVactConsumerAgentGroup P = createAgentGroup("P");
        P.setSpatialDistribution(spatialDist);
        P.setInitialProductAwareness(bernoulli07);
        P.setInitialAdopter(bernoulli07);

        InPVactConsumerAgentGroup M = createAgentGroup("M");
        M.setSpatialDistribution(spatialDist);
        M.setInitialProductAwareness(bernoulli03);
        M.setInitialAdopter(bernoulli03);

        InPVactConsumerAgentGroup L = createAgentGroup("L");
        L.setSpatialDistribution(spatialDist);
        L.setInitialProductAwareness(bernoulli01);
        L.setInitialAdopter(bernoulli01);

        String prefix = "aff";
        InAffinities affinities = createAffinities("affinities",
                createAffinityEntry(prefix, S, S, 0.0),
                createAffinityEntry(prefix, S, P, 0.8),
                createAffinityEntry(prefix, S, M, 0.15),
                createAffinityEntry(prefix, S, L, 0.05),

                createAffinityEntry(prefix, P, S, 0.0),
                createAffinityEntry(prefix, P, P, 0.7),
                createAffinityEntry(prefix, P, M, 0.25),
                createAffinityEntry(prefix, P, L, 0.05),

                createAffinityEntry(prefix, M, S, 0.0),
                createAffinityEntry(prefix, M, P, 0.3),
                createAffinityEntry(prefix, M, M, 0.6),
                createAffinityEntry(prefix, M, L, 0.1),

                createAffinityEntry(prefix, L, S, 0.0),
                createAffinityEntry(prefix, L, P, 0.05),
                createAffinityEntry(prefix, L, M, 0.15),
                createAffinityEntry(prefix, L, L, 0.8)
        );

        InFileBasedPVactConsumerAgentPopulation population = createPopulation("Pop", getTotalAgents(), S, P, M, L);

        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("Topo");

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");

        InPVactGroupBasedDeffuantUncertainty uncertainty = createDefaultUnvertainty("uncert", S, P, M, L);

        InRAProcessModel processModel = createDefaultProcessModel("Process", uncertainty, 0.0);
        processModel.setABCD(0);
        processModel.setA(1);

        InSpace2D space2D = createSpace2D("Space2D");

        //=====
        InRoot root = createRootWithInformations();
        root.general.lastSimulationYear = DEFAULT_INITIAL_YEAR;
        root.setAffinities(affinities);
        root.setConsumerAgentGroups(new InConsumerAgentGroup[]{S, P, M, L});
        root.setAgentPopulationSize(population);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return Collections.singletonList(root);
    }
}
