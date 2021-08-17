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
public class ToyModel_D_S1R extends AbstractToyModel {

    public static final int REVISION = 0;

    public static final int SIZE_P = 500;
    public static final int SIZE_M = 1500;
    public static final int SIZE_L = 500;

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected InDiracUnivariateDistribution dirac05 = new InDiracUnivariateDistribution("dirac05", 0.5);
    protected InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);
    protected InDiracUnivariateDistribution diracX = new InDiracUnivariateDistribution("diracX", 0.01);

    protected InBernoulliDistribution bernoulli0 = new InBernoulliDistribution("bernoulli0", 0);
    protected InBernoulliDistribution bernoulli001 = new InBernoulliDistribution("bernoulli001", 0.01);
    protected InBernoulliDistribution bernoulli03 = new InBernoulliDistribution("bernoulli03", 0.3);

    protected int sizeP = SIZE_P;
    protected int sizeM = SIZE_M;
    protected int sizeL = SIZE_L;

    public ToyModel_D_S1R(BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(resultConsumer);
        init();
    }

    public ToyModel_D_S1R(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        init();
    }

    protected void init() {
        setRevision(REVISION);
        setTotalAgents(sizeP + sizeM + sizeL);
    }

    @Override
    protected List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            Random random) {
        return createTestData(input, sizeP, sizeM, sizeL, random);
    }

    public List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            int sizeOfP, int sizeOfM, int sizeOfL,
            Random rnd) {
        List<List<SpatialAttribute>> output = SpatialUtil.drawRandom(input, sizeOfP + sizeOfM + sizeOfL, rnd);
        int from = 0;
        int to = 0;
        //P
        to += sizeOfP;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "P");
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //M
        from += sizeOfP;
        to += sizeOfM;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "M");
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //L
        from += sizeOfM;
        to += sizeOfL;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "L");
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

        grp.setB6(diracX);

        grp.setC1(dirac05);
        grp.setC3(dirac0);

        grp.setD1(dirac1);
        grp.setD2(dirac1);
        grp.setD3(dirac05);
        grp.setD4(dirac05);
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

        InPVactConsumerAgentGroup P = createAgentGroup("P", spatialDist);
        P.setD1(bernoulli03);
        P.setD5(bernoulli03);

        InPVactConsumerAgentGroup M = createAgentGroup("M", spatialDist);
        M.setD1(bernoulli001);
        M.setD1(bernoulli001);

        InPVactConsumerAgentGroup L = createAgentGroup("L", spatialDist);
        L.setD1(bernoulli0);
        L.setD1(bernoulli0);

        InConsumerAgentGroup[] cags = {P, M, L};

        InAffinities affinities = createAffinities("affinities",
                createEntries("affi", P, cags, new double[]{0.8, 0.17, 0.03}),
                createEntries("affi", M, cags, new double[]{0.2, 0.6, 0.2}),
                createEntries("affi", L, cags, new double[]{0.01, 0.74, 0.25})
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
        processModel.setA(0.2);
        processModel.setD(0.8);

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
    public List<InRoot> createInRootsOLD() {
        return Collections.singletonList(createInRoot());
    }
}
