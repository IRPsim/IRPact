package de.unileipzig.irpact.util.scenarios.pvact.toymodels.v2;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.PVactDataBuilder;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.AbstractToyModel;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel_S_1 extends AbstractToyModel {

    public static final int REVISION = 2;

    protected InDiracUnivariateDistribution dirac07 = new InDiracUnivariateDistribution("dirac07", 0.7);

    public ToyModel_S_1(BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(resultConsumer);
        init();
    }

    public ToyModel_S_1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        init();
    }

    protected void init() {
        setRevision(REVISION);
        String[] cags = {"A", "K"};
        groupBuilder.add(cags);
        dataBuilder.init(
                cags,
                new int[]{10, 10},
                new PVactDataBuilder.RowHandler[]{
                        row -> {
                            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac1.getValue());  //A1
                            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
                            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
                        },
                        row -> {
                            SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER, dirac0.getValue());  //A1
                            SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, dirac1.getValue()); //A5
                            SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, dirac1.getValue());     //A6
                        }
                }
        );
    }

    @Override
    protected InPVactConsumerAgentGroup createAgentGroup(String name, InSpatialDistribution distribution) {
        InPVactConsumerAgentGroup grp = createNullAgent(name, distribution);

        //A1 in file
        grp.setNoveltySeeking(dirac1);                            //A2
        grp.setDependentJudgmentMaking(dirac1);                   //A3
        grp.setEnvironmentalConcern(dirac1);                      //A4
        //A5 in file
        //A6 in file

        grp.setInterestThreshold(dirac1);                         //D2
        grp.setFinancialThreshold(dirac07);                       //D3
        grp.setAdoptionThreshold(dirac07);                        //D4
        grp.setInitialProductInterest(dirac1);                    //D6

        return grp;
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public InRoot createInRoot(int year, int delta) {
        groupBuilder.apply("A", A -> {
            A.setInitialProductAwareness(dirac1);
        });
        groupBuilder.apply("K", K -> {
            K.setInitialProductAwareness(dirac0);
        });

        InFileBasedPVactMilieuSupplier spatialDist = computeCachedIfAbsent("SpatialDistribution", this::createSpatialDistribution);
        groupBuilder.setSpatialDistribution(spatialDist);

        InAffinities affinities = computeCachedIfAbsent("Affinities", _name -> createZeroAffinities(_name, groupBuilder.cags()));

        InFileBasedPVactConsumerAgentPopulation population = computeCachedIfAbsent("Population", _name -> createPopulation(_name, dataBuilder.getTotalSize(), groupBuilder.cags()));

        InUnlinkedGraphTopology topology = computeCachedIfAbsent("Topology", InUnlinkedGraphTopology::new);

        InUnitStepDiscreteTimeModel timeModel = computeCachedIfAbsent("TimeModel", this::createOneWeekTimeModel);

        InPVactGroupBasedDeffuantUncertainty uncertainty = createDefaultUnvertainty("Uncertainty", groupBuilder.cags());

        InRAProcessModel processModel = createDefaultProcessModel("ProcessModel", uncertainty, 0.0);

        InSpace2D space2D = createSpace2D("SpatialModel");

        //=====

        InRoot root = createRootWithInformationsWithFullLogging(year, delta);
        root.getGeneral().setFirstSimulationYearAsLast();
        root.setAffinities(affinities);
        root.setConsumerAgentGroups(groupBuilder.cags());
        root.setAgentPopulationSize(population);
        root.setGraphTopologyScheme(topology);
        root.setProcessModel(processModel);
        root.setSpatialModel(space2D);
        root.setTimeModel(timeModel);

        return root;
    }
}
