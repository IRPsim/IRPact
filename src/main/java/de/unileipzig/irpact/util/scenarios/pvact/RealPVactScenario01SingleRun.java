package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InBernoulliDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InTruncatedNormalDistribution;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.util.pvact.Milieu;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class RealPVactScenario01SingleRun extends AbstractPVactScenario {

    public static final int REVISION = 0;

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);

    public Path xlsx;

    public RealPVactScenario01SingleRun(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    protected InPVactConsumerAgentGroup createAgentGroup(String name) {
        InPVactConsumerAgentGroup grp = new InPVactConsumerAgentGroup();
        grp.setName(name);

        //A1 in file
        grp.setNoveltySeeking(dirac0);                            //A2 !
        grp.setDependentJudgmentMaking(dirac0);                   //A3 !
        grp.setEnvironmentalConcern(dirac0);                      //A4 !
        //A5 in file
        //A6 in file
        grp.setConstructionRate(dirac0);                          //A7 !
        grp.setRenovationRate(dirac0);                            //A8 !

        grp.setRewire(dirac0);                                    //B6 !

        grp.setCommunication(dirac0);                             //C1 !
        grp.setRateOfConvergence(dirac0);                         //C3 !

        grp.setInitialProductAwareness(dirac0);                   //D1 -
        grp.setInterestThreshold(dirac0);                         //D2 !
        grp.setFinancialThreshold(dirac0);                        //D3 !
        grp.setAdoptionThreshold(dirac0);                         //D4 !
        grp.setInitialAdopter(dirac0);                            //D5 !
        grp.setInitialProductInterest(dirac0);                    //D6 -

        return grp;
    }

    @Override
    public List<InRoot> createInRoots() {
        RealData realData = new RealData(this::createAgentGroup);

        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");
        realData.CAGS.forEach(cag -> cag.setSpatialDistribution(spatialDist));

        InDiracUnivariateDistribution renoDist = new InDiracUnivariateDistribution("RENO", 0.00394339526076053);
        InDiracUnivariateDistribution constDist = new InDiracUnivariateDistribution("CONST", 0.00364015558626337);
        InDiracUnivariateDistribution interestDist = new InDiracUnivariateDistribution("INTEREST_THRESHOLD", 9);
        InDiracUnivariateDistribution financialThresholdDist = new InDiracUnivariateDistribution("FINANCIAL_THRESHOLD", 0);
        InDiracUnivariateDistribution adoptionThreshold = new InDiracUnivariateDistribution("ADOPTION_THRESHOLD", 0);
        realData.CAGS.forEach(cag -> cag.setRenovationRate(renoDist));
        realData.CAGS.forEach(cag -> cag.setConstructionRate(constDist));
        realData.CAGS.forEach(cag -> cag.setInterestThreshold(interestDist));
        realData.CAGS.forEach(cag -> cag.setFinancialThreshold(financialThresholdDist));
        realData.CAGS.forEach(cag -> cag.setAdoptionThreshold(adoptionThreshold));

        InAffinities affinities = realData.buildAffinities("affinities", 0.53);

        //NS
        Map<Milieu, InTruncatedNormalDistribution> ns = RealData.buildTruncNorm("NS", RealData.XLSX_ORDER_ARR, RealData.NS_MEANS, RealData.NS_SD);
        realData.CAGS.applyMilieus(ns, InPVactConsumerAgentGroup::setNoveltySeeking);
        //DEP
        Map<Milieu, InTruncatedNormalDistribution> dep = RealData.buildTruncNorm("DEP", RealData.XLSX_ORDER_ARR, RealData.DEP_MEANS, RealData.DEP_SD);
        realData.CAGS.applyMilieus(dep, InPVactConsumerAgentGroup::setDependentJudgmentMaking);
        //NS
        Map<Milieu, InTruncatedNormalDistribution> nep = RealData.buildTruncNorm("NEP", RealData.XLSX_ORDER_ARR, RealData.NEP_MEANS, RealData.NEP_SD);
        realData.CAGS.applyMilieus(nep, InPVactConsumerAgentGroup::setEnvironmentalConcern);
        //COMMU
        Map<Milieu, InBernoulliDistribution> commu = RealData.buildBernoulli("COMMU", RealData.XLSX_ORDER_ARR, RealData.COMMU);
        realData.CAGS.applyMilieus(commu, InPVactConsumerAgentGroup::setCommunication);
        //REWIRE
        Map<Milieu, InBernoulliDistribution> rewire = RealData.buildBernoulli("REWIRE", RealData.XLSX_ORDER_ARR, RealData.REWIRE);
        realData.CAGS.applyMilieus(rewire, InPVactConsumerAgentGroup::setRewire);
        //COMMU
        Map<Milieu, InBernoulliDistribution> initialAdopter = RealData.buildBernoulli("INITADOPT", RealData.XLSX_ORDER_ARR, RealData.INITIAL_ADOPTER);
        realData.CAGS.applyMilieus(initialAdopter, InPVactConsumerAgentGroup::setInitialAdopter);

        InFileBasedPVactConsumerAgentPopulation population = createFullPopulation("Pop", realData.CAGS.cags());
//        population.setUseAll(false);
//        population.setDesiredSize(100);

        Map<InPVactConsumerAgentGroup, Integer> edgeCount = realData.CAGS.map(RealData.calcEdgeCount(
                RealData.XLSX_ORDER_ARR,
                RealData.NSIZE,
                RealData.SCALE,
                RealData.MULTIPLIER
        ));

        InFreeNetworkTopology topology = createFreeTopology("Topo", affinities, edgeCount);

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");

        InPVactGlobalDeffuantUncertainty uncertainty = createGlobalUnvertainty("uncert", realData.CAGS.cags());

        InRAProcessModel processModel = createDefaultProcessModel("Process", uncertainty, RAConstants.DEFAULT_SPEED_OF_CONVERGENCE);
        processModel.setDefaultValues();
        processModel.setB(RealData.WEIGHT_NEP);
        processModel.setC(RealData.WEIGHT_NS);
        processModel.setWeightFT(RealData.WEIGHT_EK);
        processModel.setWeightNPV(RealData.WEIGHT_NPV);
        processModel.setWeightSocial(RealData.WEIGHT_SOCIAL);
        processModel.setWeightLocal(RealData.WEIGHT_LOCALE);

        InSpace2D space2D = createSpace2D("Space2D");

        InGenericOutputImage[] defaultImages = createDefaultImages();

        //=====
        InRoot root = createRootWithInformationsWithFullLogging();
        root.getGeneral().setFirstSimulationYear(2008);
        root.getGeneral().setLastSimulationYear(2008);
        root.getGeneral().useInfoLogging();
        root.getGeneral().setPersistDisable(true);
        root.getGeneral().logResultAdoptionsAll = true;
        root.setAffinities(affinities);
        root.setConsumerAgentGroups(realData.CAGS.cags());
        root.setAgentPopulationSize(population);
        root.setGraphTopologyScheme(topology);
        root.setProcessModel(processModel);
        root.setSpatialModel(space2D);
        root.setTimeModel(timeModel);
        root.setImages(defaultImages);
        root.getGraphvizGeneral().setPositionBasedLayoutAlgorithm(true);
        root.getGraphvizGeneral().setKeepAspectRatio(true);
        root.getGraphvizGeneral().setPreferredImageSize(1000);
        root.getSpecialPVactInput().setUseConstructionRates(true);
        root.getSpecialPVactInput().setConstructionRates(RealData.CONST_RATES);
        root.getSpecialPVactInput().setUseRenovationRates(true);
        root.getSpecialPVactInput().setRenovationRates(RealData.RENO_RATES);

        setColors(root, realData.CAGS.cags());

        setupGeneral(root.getGeneral());

        return Collections.singletonList(root);
    }
}
