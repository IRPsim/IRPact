package de.unileipzig.irpact.util.scenarios.pvact.toymodels.old;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
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
public class ToyModel_S_7_3 extends AbstractToyModel {

    public static final int REVISION = 1;

    public static final int SIZE_S = 10;
    public static final int SIZE_A = 10;
    public static final int SIZE_K = 10;
    public static final int SIZE_H = 10;

    protected InDiracUnivariateDistribution dirac085 = new InDiracUnivariateDistribution("dirac085", 0.85);

    protected int sizeS = SIZE_S;
    protected int sizeA = SIZE_A;
    protected int sizeK = SIZE_K;
    protected int sizeH = SIZE_H;

    public ToyModel_S_7_3(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
        setTotalAgents(SIZE_S + SIZE_A + SIZE_K + SIZE_H);
    }

    @Override
    protected List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            Random random) {
        return createTestData(input, sizeS, sizeA, sizeK, sizeH, random);
    }

    public List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            int sizeOfS, int sizeOfA, int sizeOfK, int sizeOfH,
            Random rnd) {
        if(true) {
            throw new UnsupportedOperationException();
        }

        List<List<SpatialAttribute>> output = SpatialUtil.drawRandom(input, sizeOfS + sizeOfA + sizeOfK + sizeOfH, rnd);
        int from = 0;
        int to = 0;
        //A
        to += sizeOfS;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "S");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //K1
        from += sizeOfS;
        to += sizeOfA;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "A");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }
        //K2
        from += sizeOfA;
        to += sizeOfK;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "K");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac0.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //K3
        from += sizeOfK;
        to += sizeOfH;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "H");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac0.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac0.getValue());     //A6
        }

        return output;
    }

    protected InPVactConsumerAgentGroup createAgentGroup(String name, InSpatialDistribution distribution) {
        InPVactConsumerAgentGroup grp = createNullAgent(name, distribution);

        //A1 in file
        //A5 in file
        //A6 in file

        grp.setInterestThreshold(dirac1);                         //D2
        grp.setAdoptionThreshold(dirac085);                       //D4
        grp.setInitialProductInterest(dirac1);                    //D6

        return grp;
    }

    @Override
    public List<InRoot> createInRoots() {
        if(true) throw new RuntimeException("kaputt");

        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");

        InPVactConsumerAgentGroup S = createAgentGroup("S", spatialDist);
        S.setEnvironmentalConcern(dirac1);                  //A4
        S.setDependentJudgmentMaking(dirac1);               //A3
        S.setInitialAdopter(dirac1);                        //D5


        InPVactConsumerAgentGroup A = createAgentGroup("A", spatialDist);
        A.setEnvironmentalConcern(dirac1);                  //A4
        A.setDependentJudgmentMaking(dirac1);               //A3
        A.setInitialAdopter(dirac0);                        //D5

        InPVactConsumerAgentGroup K = createAgentGroup("K", spatialDist);
        K.setEnvironmentalConcern(dirac1);                  //A4
        K.setDependentJudgmentMaking(dirac0);               //A3
        K.setInitialAdopter(dirac0);                        //D5

        InPVactConsumerAgentGroup H = createAgentGroup("H", spatialDist);
        H.setEnvironmentalConcern(dirac1);                  //A4
        H.setDependentJudgmentMaking(dirac0);               //A3
        H.setInitialAdopter(dirac0);                        //D5

        InConsumerAgentGroup[] cags = {A, S, K, H};

        InAffinities affinities = createAffinities("affinities",
                createEntries("", S, cags, new double[]{0, 1, 0, 0}),
                createEntries("", A, cags, new double[]{0, 1, 0, 0}),
                createEntries("", K, cags, new double[]{0, 0, 1, 0}),
                createEntries("", H, cags, new double[]{0, 0, 0, 1})
        );

        InFileBasedPVactConsumerAgentPopulation population = createPopulation("Pop", getTotalAgents(), cags);

        InFreeNetworkTopology topology = createFreeTopology(
                "Topo",
                affinities,
                cags,
                5
        );

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");

        InPVactGroupBasedDeffuantUncertainty uncertainty = createDefaultUnvertainty("uncert", cags);

        InRAProcessModel processModel = createDefaultProcessModel("Process", uncertainty, 0.0);
        processModel.setA(0.5);
        processModel.setB(0);
        processModel.setC(0.5);
        processModel.setD(0);

        InSpace2D space2D = createSpace2D("Space2D");

        //=====
        InRoot root = createRootWithInformationsWithFullLogging();
        root.general.lastSimulationYear = DEFAULT_INITIAL_YEAR;
        root.setAffinities(affinities);
        root.setConsumerAgentGroups(cags);
        root.setAgentPopulationSize(population);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return Collections.singletonList(root);
    }
}
