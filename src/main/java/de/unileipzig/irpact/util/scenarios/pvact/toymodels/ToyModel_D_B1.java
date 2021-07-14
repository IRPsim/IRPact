package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

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
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.output.OutRoot;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel_D_B1 extends AbstractToyModel {

    public static final int REVISION = 1;

    public static final int SIZE_S = 100;
    public static final int SIZE_P = 300;
    public static final int SIZE_M = 1600;
    public static final int SIZE_L = 500;
    public static final int SIZE_K = 100;

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected InDiracUnivariateDistribution dirac049 = new InDiracUnivariateDistribution("dirac049", 0.49);
    protected InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);
    protected InDiracUnivariateDistribution dirac100 = new InDiracUnivariateDistribution("dirac100", 100);

    protected InBernoulliDistribution bernoulli0 = new InBernoulliDistribution("bernoulli0", 0);
    protected InBernoulliDistribution bernoulli001 = new InBernoulliDistribution("bernoulli001", 0.01);
    protected InBernoulliDistribution bernoulli003 = new InBernoulliDistribution("bernoulli003", 0.03);
    protected InBernoulliDistribution bernoulli005 = new InBernoulliDistribution("bernoulli005", 0.05);
    protected InBernoulliDistribution bernoulli01 = new InBernoulliDistribution("bernoulli01", 0.1);
    protected InBernoulliDistribution bernoulli015 = new InBernoulliDistribution("bernoulli015", 0.15);
    protected InBernoulliDistribution bernoulli1 = new InBernoulliDistribution("bernoulli1", 1);

    protected int sizeS = SIZE_S;
    protected int sizeP = SIZE_P;
    protected int sizeM = SIZE_M;
    protected int sizeL = SIZE_L;
    protected int sizeK = SIZE_K;

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
        setTotalAgents(sizeS + sizeP + sizeM + sizeL + sizeK);
    }

    @Override
    protected List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            Random random) {
        return createTestData(input, sizeS, sizeP, sizeM, sizeL, sizeK, random);
    }

    public List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            int sizeOfS, int sizeOfP, int sizeOfM, int sizeOfL, int sizeOfK,
            Random rnd) {
        List<List<SpatialAttribute>> output = SpatialUtil.drawRandom(input, sizeOfS + sizeOfP + sizeOfM + sizeOfL + sizeOfK, rnd);
        int from = 0;
        int to = 0;
        //S
        to += sizeOfS;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "S");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac100.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //P
        from += sizeOfS;
        to += sizeOfP;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "P");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac100.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //M
        from += sizeOfP;
        to += sizeOfM;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "M");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac100.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //L
        from += sizeOfM;
        to += sizeOfL;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "L");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac100.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //K
        from += sizeOfL;
        to += sizeOfK;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "K");
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
        grp.setA2(dirac1);
        grp.setA3(dirac0);
        grp.setA4(dirac0);
        //A5 in file
        //A6 in file
        grp.setA7(dirac0);
        grp.setA8(dirac0);

        grp.setB6(dirac0);

        grp.setC1(dirac0);
        grp.setC3(dirac0);

        grp.setD1(dirac1);
        grp.setD2(dirac1);
        grp.setD3(dirac049);
        grp.setD4(dirac049);
        grp.setD5(dirac0);
        grp.setD6(dirac1);

        if(distribution != null) {
            grp.setSpatialDistribution(distribution);
        }

        return grp;
    }

    public InRoot createInRoot() {
        return createInRoot(DEFAULT_INITIAL_YEAR);
    }

    public InRoot createInRoot(int year) {
        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");

        InPVactConsumerAgentGroup S = createAgentGroup("S", spatialDist);
        S.setD1(bernoulli1);
        S.setD5(bernoulli0);

        InPVactConsumerAgentGroup P = createAgentGroup("P", spatialDist);
        P.setD1(bernoulli015);
        P.setD5(bernoulli01);

        InPVactConsumerAgentGroup M = createAgentGroup("M", spatialDist);
        M.setD1(bernoulli005);
        M.setD5(bernoulli003);

        InPVactConsumerAgentGroup L = createAgentGroup("L", spatialDist);
        L.setD1(bernoulli001);
        L.setD5(bernoulli001);

        InPVactConsumerAgentGroup K = createAgentGroup("K", spatialDist);
        K.setD1(bernoulli0);
        K.setD5(bernoulli0);

        InConsumerAgentGroup[] cags = {S, P, M, L, K};

        InAffinities affinities = createAffinities("affinities",
                createEntries("affi", S, cags, new double[]{0, 0.8, 0.15, 0.05, 0}),
                createEntries("affi", P, cags, new double[]{0, 0.7, 0.25, 0.05, 0}),
                createEntries("affi", M, cags, new double[]{0, 0.3, 0.6, 0.1, 0}),
                createEntries("affi", L, cags, new double[]{0, 0.05, 0.15, 0.8, 0}),
                createEntries("affi", K, cags, new double[]{0, 0.0, 0.0, 0.0, 1})
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
        root.getGeneral().setFirstSimulationYear(year);
        root.setAffinities(affinities);
        root.setConsumerAgentGroups(cags);
        root.setAgentPopulationSize(population);
        root.setGraphTopologyScheme(topology);
        root.setProcessModel(processModel);
        root.setSpatialModel(space2D);
        root.setTimeModel(timeModel);
        root.setImages(defaultImages);

        return root;
    }

    @Override
    public List<InRoot> createInRoots() {
        return Collections.singletonList(createInRoot());
    }
}
