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
public class ToyModel_D_P1 extends AbstractToyModel {

    public static final int REVISION = 1;

    public static final int SIZE_H1 = 100;
    public static final int SIZE_P = 300;
    public static final int SIZE_M = 1200;
    public static final int SIZE_L = 300;
    public static final int SIZE_H2 = 100;

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected InDiracUnivariateDistribution dirac005 = new InDiracUnivariateDistribution("dirac005", 0.05);
    protected InDiracUnivariateDistribution dirac015 = new InDiracUnivariateDistribution("dirac015", 0.15);
    protected InDiracUnivariateDistribution dirac02 = new InDiracUnivariateDistribution("dirac02", 0.2);
    protected InDiracUnivariateDistribution dirac03 = new InDiracUnivariateDistribution("dirac03", 0.3);
    protected InDiracUnivariateDistribution dirac035 = new InDiracUnivariateDistribution("dirac035", 0.35);
    protected InDiracUnivariateDistribution dirac045 = new InDiracUnivariateDistribution("dirac045", 0.45);
    protected InDiracUnivariateDistribution dirac05 = new InDiracUnivariateDistribution("dirac05", 0.5);
    protected InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);
    protected InDiracUnivariateDistribution dirac9 = new InDiracUnivariateDistribution("dirac9", 9);

    protected int sizeH1 = SIZE_H1;
    protected int sizeP = SIZE_P;
    protected int sizeM = SIZE_M;
    protected int sizeL = SIZE_L;
    protected int sizeH2 = SIZE_H2;

    public ToyModel_D_P1(BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(resultConsumer);
        init();
    }

    public ToyModel_D_P1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        init();
    }

    protected void init() {
        setRevision(REVISION);
        setTotalAgents(sizeH1 + sizeP + sizeM + sizeL + sizeH2);
        setSimulationDelta(15);
    }

    @Override
    protected List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            Random random) {
        return createTestData(input, sizeH1, sizeP, sizeM, sizeL, sizeH2, random);
    }

    public List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            int sizeOfH1, int sizeOfP, int sizeOfM, int sizeOfL, int sizeOfH2,
            Random rnd) {
        List<List<SpatialAttribute>> output = SpatialUtil.drawRandom(input, sizeOfH1 + sizeOfP + sizeOfM + sizeOfL + sizeOfH2, rnd);
        int from = 0;
        int to = 0;
        //S
        to += sizeOfH1;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "H1");
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //P
        from += sizeOfH1;
        to += sizeOfP;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "P");
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //M
        from += sizeOfP;
        to += sizeOfM;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "M");
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //L
        from += sizeOfM;
        to += sizeOfL;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "L");
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //K
        from += sizeOfL;
        to += sizeOfH2;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "H2");
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
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
        grp.setD2(dirac9);
        grp.setD3(dirac0);
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

        InPVactConsumerAgentGroup S = createAgentGroup("S", spatialDist);
        S.setA2(dirac1);
        S.setA4(dirac1);
        S.setC1(dirac05);
        S.setD1(dirac1);
        S.setD5(dirac1);

        InPVactConsumerAgentGroup P = createAgentGroup("P", spatialDist);
        P.setA2(dirac045);
        P.setA4(dirac045);
        P.setC1(dirac035);
        P.setD1(dirac1);
        P.setD5(dirac0);

        InPVactConsumerAgentGroup M = createAgentGroup("M", spatialDist);
        M.setA2(dirac03);
        M.setA4(dirac03);
        M.setC1(dirac02);
        M.setD1(dirac1);
        M.setD5(dirac0);

        InPVactConsumerAgentGroup L = createAgentGroup("L", spatialDist);
        L.setA2(dirac015);
        L.setA4(dirac015);
        L.setC1(dirac005);
        L.setD1(dirac1);
        L.setD5(dirac0);

        InPVactConsumerAgentGroup K = createAgentGroup("K", spatialDist);
        K.setA2(dirac0);
        K.setA4(dirac0);
        K.setC1(dirac0);
        K.setD1(dirac1);
        K.setD5(dirac0);

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

        InPVactGlobalDeffuantUncertainty uncertainty = createGlobalUnvertainty("uncert", 0.1, 0.05, 0.2, cags);

        InRAProcessModel processModel = createDefaultProcessModel("Process", uncertainty, RAConstants.DEFAULT_SPEED_OF_CONVERGENCE);
        processModel.setABCD(0);
        processModel.setB(0.5);
        processModel.setC(0.5);

        InSpace2D space2D = createSpace2D("Space2D");

        InGenericOutputImage[] defaultImages = createDefaultImages();

        //=====
        InRoot root = createRootWithInformations();
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
