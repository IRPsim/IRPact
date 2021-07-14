package de.unileipzig.irpact.util.scenarios.pvact.toymodels.old;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
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
public class ToyModel_S_3 extends AbstractToyModel {

    public static final int REVISION = 0;

    public static final int SIZE_A = 10;
    public static final int SIZE_K = 10;

    protected int sizeA = SIZE_A;
    protected int sizeK = SIZE_K;

    public ToyModel_S_3(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
        setTotalAgents(SIZE_A + SIZE_K);
    }

    public void setSizeA(int sizeA) {
        this.sizeA = sizeA;
    }

    public int getSizeA() {
        return sizeA;
    }

    public void setSizeK(int sizeK) {
        this.sizeK = sizeK;
    }

    public int getSizeK() {
        return sizeK;
    }

    @Override
    protected List<List<SpatialAttribute>> createTestData(
            List<List<SpatialAttribute>> input,
            Random random) {
        return createTestData(input, sizeA, sizeK, random);
    }

    public List<List<SpatialAttribute>> createTestData(
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
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "A");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }
        //K
        from += sizeOfA;
        to += sizeOfK;
        for(int i = from; i < to; i++) {
            List<SpatialAttribute> row = output.get(i);
            SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, "K");
            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
        }

        return output;
    }

    protected InPVactConsumerAgentGroup createAgentGroup(String name, InSpatialDistribution distribution) {
        InPVactConsumerAgentGroup grp = createNullAgent(name, distribution);
        grp.setName(name);

        //A1 in file
        grp.setDependentJudgmentMaking(dirac1);                   //A3
        grp.setEnvironmentalConcern(dirac1);                      //A4
        //A5 in file
        //A6 in file

        grp.setInterestThreshold(dirac1);                         //D2
        grp.setInitialProductInterest(dirac1);                    //D6

        return grp;
    }

    @Override
    public List<InRoot> createInRoots() {
        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");

        InPVactConsumerAgentGroup A = createAgentGroup("A", spatialDist);
        A.setNoveltySeeking(dirac1);                     //A2

        InPVactConsumerAgentGroup K = createAgentGroup("K", spatialDist);
        K.setNoveltySeeking(dirac0);                     //A2

        InAffinities affinities = createZeroAffinities("affinities", A, K);

        InFileBasedPVactConsumerAgentPopulation population = createPopulation("Pop", getTotalAgents(), A, K);

        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("Topo");

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");

        InPVactGroupBasedDeffuantUncertainty uncertainty = createDefaultUnvertainty("uncert", A, K);

        InRAProcessModel processModel = createDefaultProcessModel("Process", uncertainty, 0.0);
        processModel.setABCD(0);
        processModel.setC(1);

        InSpace2D space2D = createSpace2D("Space2D");

        //=====

        InRoot root = createRootWithInformationsWithFullLogging();
        root.general.setFirstSimulationYearAsLast();
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
