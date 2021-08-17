package de.unileipzig.irpact.util.scenarios.pvact.toymodels.old;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InBernoulliDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.AbstractToyModel;

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
    protected InBernoulliDistribution bernoulli07 = new InBernoulliDistribution("bernoulli07", 0.7);
    protected InBernoulliDistribution bernoulli03 = new InBernoulliDistribution("bernoulli03", 0.3);
    protected InBernoulliDistribution bernoulli001 = new InBernoulliDistribution("bernoulli001", 0.01);

    protected int sizeS = SIZE_S;
    protected int sizeP = SIZE_P;
    protected int sizeM = SIZE_M;
    protected int sizeL = SIZE_L;

    public ToyModel_D_B1(BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(resultConsumer);
        init();
    }

    public ToyModel_D_B1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        init();
    }

    protected void init() {
        setRevision(REVISION);
        setTotalAgents(SIZE_S + SIZE_P + SIZE_M + SIZE_L);
    }

    @Override
    protected List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            Random random) {
        return createTestData(input, sizeS, sizeP, sizeM, sizeL, random);
    }

    public List<List<SpatialAttribute>> createTestData(
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

    protected InPVactConsumerAgentGroup createAgentGroup(String name, InSpatialDistribution distribution) {
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

        if(distribution != null) {
            grp.setSpatialDistribution(distribution);
        }

        return grp;
    }

    @Override
    public List<InRoot> createInRootsOLD() {
        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");

        InPVactConsumerAgentGroup S = createAgentGroup("S", spatialDist);
        S.setInitialProductAwareness(bernoulli1);
        S.setInitialAdopter(bernoulli1);

        InPVactConsumerAgentGroup P = createAgentGroup("P", spatialDist);
        P.setInitialProductAwareness(bernoulli07);
        P.setInitialAdopter(bernoulli07);

        InPVactConsumerAgentGroup M = createAgentGroup("M", spatialDist);
        M.setInitialProductAwareness(bernoulli03);
        M.setInitialAdopter(bernoulli03);

        InPVactConsumerAgentGroup L = createAgentGroup("L", spatialDist);
        L.setInitialProductAwareness(bernoulli001);
        L.setInitialAdopter(bernoulli001);

        InConsumerAgentGroup[] cags = {S, P, M, L};

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

        InFileBasedPVactConsumerAgentPopulation population = createPopulation("Pop", getTotalAgents(), cags);

        InFreeNetworkTopology topology = createFreeTopology(
                "Topo",
                affinities,
                cags,
                5
        );

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");

        InPVactGlobalDeffuantUncertainty uncertainty = createGlobalUnvertainty("uncert", cags);

        InRAProcessModel processModel = createDefaultProcessModel("Process", uncertainty, 0.0);
        processModel.setABCD(0);
        processModel.setA(1);

        InSpace2D space2D = createSpace2D("Space2D");

        InGenericOutputImage[] defaultImages = createDefaultImages();

        //=====
        InRoot root = createRootWithInformationsWithFullLogging();
        root.setAffinities(affinities);
        root.setConsumerAgentGroups(cags);
        root.setAgentPopulationSize(population);
        root.setGraphTopologyScheme(topology);
        root.setProcessModel(processModel);
        root.setSpatialModel(space2D);
        root.setTimeModel(timeModel);
        root.setImages(defaultImages);

        return Collections.singletonList(root);
    }
}
