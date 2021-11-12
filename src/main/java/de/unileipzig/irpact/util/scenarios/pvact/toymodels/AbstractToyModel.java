package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InBernoulliDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.postdata.InPostDataAnalysis;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.AbstractPVactScenario;
import de.unileipzig.irpact.util.scenarios.pvact.RealData;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataCreator;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.PVactCagManager;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.PVactModularProcessModelManager;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractToyModel extends AbstractPVactScenario {

    protected static final String DEFAULT_PV_DATA_NAME = "Barwertrechner";
    protected static final String LEIPZIG_ADOPTION_DATA = "PV_Diffusion_Leipzig";

    public static final String ORIGINAL_ID = "Original-ID";

    public static final BiConsumer<InRoot, OutRoot> IGNORE = (in, out) -> {};

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected InDiracUnivariateDistribution dirac002 = new InDiracUnivariateDistribution("dirac002", 0.02);
    protected InDiracUnivariateDistribution dirac005 = new InDiracUnivariateDistribution("dirac005", 0.05);
    protected InDiracUnivariateDistribution dirac01 = new InDiracUnivariateDistribution("dirac01", 0.1);
    protected InDiracUnivariateDistribution dirac015 = new InDiracUnivariateDistribution("dirac015", 0.15);
    protected InDiracUnivariateDistribution dirac02 = new InDiracUnivariateDistribution("dirac02", 0.2);
    protected InDiracUnivariateDistribution dirac03 = new InDiracUnivariateDistribution("dirac03", 0.3);
    protected InDiracUnivariateDistribution dirac035 = new InDiracUnivariateDistribution("dirac035", 0.35);
    protected InDiracUnivariateDistribution dirac04 = new InDiracUnivariateDistribution("dirac04", 0.4);
    protected InDiracUnivariateDistribution dirac045 = new InDiracUnivariateDistribution("dirac045", 0.45);
    protected InDiracUnivariateDistribution dirac047 = new InDiracUnivariateDistribution("dirac047", 0.47);
    protected InDiracUnivariateDistribution dirac049 = new InDiracUnivariateDistribution("dirac049", 0.49);
    protected InDiracUnivariateDistribution dirac05 = new InDiracUnivariateDistribution("dirac05", 0.5);
    protected InDiracUnivariateDistribution dirac0501 = new InDiracUnivariateDistribution("dirac0501", 0.501);
    protected InDiracUnivariateDistribution dirac07 = new InDiracUnivariateDistribution("dirac07", 0.7);
    protected InDiracUnivariateDistribution dirac085 = new InDiracUnivariateDistribution("dirac085", 0.85);
    protected InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);
    protected InDiracUnivariateDistribution dirac2 = new InDiracUnivariateDistribution("dirac2", 2);
    protected InDiracUnivariateDistribution dirac100 = new InDiracUnivariateDistribution("dirac100", 100);

    protected InBernoulliDistribution bernoulli0 = new InBernoulliDistribution("bernoulli0", 0);
    protected InBernoulliDistribution bernoulli001 = new InBernoulliDistribution("bernoulli001", 0.01);
    protected InBernoulliDistribution bernoulli003 = new InBernoulliDistribution("bernoulli003", 0.03);
    protected InBernoulliDistribution bernoulli005 = new InBernoulliDistribution("bernoulli005", 0.05);
    protected InBernoulliDistribution bernoulli01 = new InBernoulliDistribution("bernoulli01", 0.1);
    protected InBernoulliDistribution bernoulli015 = new InBernoulliDistribution("bernoulli015", 0.15);
    protected InBernoulliDistribution bernoulli1 = new InBernoulliDistribution("bernoulli1", 1);

    protected final DataSetup testData = new DataSetup();
    protected final PVactCagManager cagManager = new PVactCagManager(dirac0);
    protected BiConsumer<InRoot, OutRoot> resultConsumer;

    public AbstractToyModel(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description);
        this.resultConsumer = Objects.requireNonNull(resultConsumer);
        init();
    }

    @SafeVarargs
    protected static <T> T[] arr(T... values) {
        return values;
    }

    protected static double[] darr(double... values) {
        return values;
    }

    protected static double nextDouble(Random rnd, double mean, double delta) {
        double temp = rnd.nextDouble() * delta * 2;
        return mean - delta + temp;
    }

    protected void init() {
        setInputFiles();
        initTestData();
        initCagManager();
    }

    protected void setInputFiles() {
        setToyModelInputFile();
        setPvDataName(DEFAULT_PV_DATA_NAME);
        setRealAdoptionDataName(LEIPZIG_ADOPTION_DATA);
    }

    protected abstract void setToyModelInputFile();

    protected abstract void initTestData();

    protected abstract void initCagManager();

    public void createTestData(
            Path input,
            Path target,
            Random rnd) throws ParsingException, IOException, InvalidFormatException {
        Table<SpatialAttribute> inputTable = SpatialTableFileLoader.parseXlsx(input, "Datensatz");

        List<List<SpatialAttribute>> outputData;
        Table<SpatialAttribute> outputTable = inputTable.emptyCopyWithSameHeader();

        if(testData.isOriginal()) {
            outputData = DataCreator.modify(testData, inputTable.listTable());
        } else {
            outputData = DataCreator.apply(testData, inputTable.listTable(), rnd);
            outputTable.addColumn(0, ORIGINAL_ID);
        }

        outputTable.addRows(outputData);
        SpatialTableFileLoader.writeXlsx(target, "Datensatz", null, outputTable);
    }

    protected void initCustomParts(InRoot root) {
        createToyModelAffinities(root, "affinities");
        createPopulation(root, "Pop");
        createTopology(root, "Topo");
        createProcessModel(root, "Process");
    }

    protected void createToyModelAffinities(InRoot root, String name) {
        InAffinities affinities = createSelfLinkedAffinities(name, cagManager.getCagsArray());
        root.setAffinities(affinities);
    }

    protected void createTopology(InRoot root, String name) {
        createUnlinkedTopology(root, name);
    }

    protected void createUnlinkedTopology(InRoot root, String name) {
        InGraphTopologyScheme topology = createUnlinkedTopology(name);
        root.setGraphTopologyScheme(topology);
    }

    protected void createFreeTopology(InRoot root, String name) {
        try {
            InFreeNetworkTopology topo = createFreeTopology(
                    name,
                    root.getAffinities(),
                    cagManager.getCagsArray(),
                    cagManager.getEdgeCountArray()
            );
            root.setGraphTopologyScheme(topo);
        } catch (ParsingException e) {
            throw new RuntimeException(e);
        }
    }

    protected void createPopulation(InRoot root, String name) {
        InFileBasedPVactConsumerAgentPopulation population = createFullPopulation(name, cagManager.getCagsArray());
        population.setUseAll(true);
        population.setDesiredSize(-1);
        root.setAgentPopulationSize(population);
    }

    protected void createProcessModel(InRoot root, String name) {
        InUncertaintySupplier uncertainty = createUncertainty("uncert");
        PVactModularProcessModelManager mpm = new PVactModularProcessModelManager();

        List<InOutputImage2> outputImages2 = new ArrayList<>();
        List<InPostDataAnalysis> postData = new ArrayList<>();
        InProcessModel processModel = createDefaultModularProcessModel(
                name,
                uncertainty,
                createNodeFilter(),
                outputImages2,
                postData,
                mpm
        );

        setupProcessModel(mpm);

        root.setProcessModel(processModel);
        root.setImages2(outputImages2);
        root.setPostData(postData);
    }

    protected InUncertaintySupplier createUncertainty(String name) {
        return createGlobalUnvertaintySupplier("uncert", RAConstants.DEFAULT_EXTREMIST_RATE, RAConstants.DEFAULT_EXTREMIST_UNCERTAINTY, RAConstants.DEFAULT_MODERATE_UNCERTAINTY);
    }

    protected InNodeDistanceFilterScheme createNodeFilter() {
        return createNodeFilterScheme(2);
    }

    protected void setupProcessModel(PVactModularProcessModelManager mpm) {
        mpm.setNpvWeightName(NPV_WEIGHT);
        mpm.setPpWeightName(PP_WEIGHT);
        mpm.setEnvWeightName(ENV_WEIGHT);
        mpm.setNovWeightName(NOV_WEIGHT);
        mpm.setLocalWeightName(LOCAL_WEIGHT);
        mpm.setSocialWeightName(SOCIAL_WEIGHT);
        mpm.setCommunicationName(COMMUNICATION);

        mpm.getNpvWeightModule().setScalar(0.25);
        mpm.getPpWeightModule().setScalar(0.25);
        mpm.getEnvWeightModule().setScalar(0.25);
        mpm.getNovWeightModule().setScalar(0.25);
        mpm.getLocalWeightModule().setScalar(0.25);
        mpm.getSocialWeightModule().setScalar(0.25);

        customProcessModelSetup(mpm);
    }

    protected void customProcessModelSetup(PVactModularProcessModelManager mpm) {
    }

    protected void setSimulationDuration(InRoot root) {
        root.getGeneral().setFirstSimulationYear(getSimulationStart());
        root.getGeneral().setLastSimulationYear(getSimulationStart() + getSimulationLength() - 1);
    }

    protected int getSimulationStart() {
        return 2008;
    }

    protected int getSimulationLength() {
        return 1;
    }

    protected InRoot createRoot() {
        InRoot root = createRootWithInformationsWithFullLogging();

        InSpace2D space2D = createSpace2D("Space2D");
        root.setSpatialModel(space2D);

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");
        timeModel.setAmountOfTime(1);
        root.setTimeModel(timeModel);

        createToyModelAffinities(root, "affinities");

        createTopology(root, "Topo");

        initCustomParts(root);

        //=====
        root.addFiles(getDefaultFiles());
        setSimulationDuration(root);
        root.getGeneral().useInfoLogging();
        root.getGeneral().enableAllResultLogging();
        root.getGeneral().setEvaluationBucketSize(0.1);
        root.getGeneral().setPersistDisabled(true);
        root.getGeneral().setCopyLogIfPossible(true);
        root.getGeneral().logResultAdoptionsAll = true;

        root.getGeneral().setOuterParallelism(1);
        root.getGeneral().setInnerParallelism(8);

        root.setConsumerAgentGroups(cagManager.getCagsArray());
        root.getGraphvizGeneral().setPositionBasedLayoutAlgorithm(true);
        root.getGraphvizGeneral().setKeepAspectRatio(true);
        root.getGraphvizGeneral().setPreferredImageSize(1000);
        root.getSpecialPVactInput().setUseConstructionRates(true);
        root.getSpecialPVactInput().setConstructionRates(RealData.CONST_RATES);
        root.getSpecialPVactInput().setUseRenovationRates(true);
        root.getSpecialPVactInput().setRenovationRates(RealData.RENO_RATES);

        setColors(root, cagManager.getCagsArray());

        setupGeneral(root.getGeneral());

        return root;
    }

    @Override
    public List<InRoot> createInRootsOLD() {
        return Collections.singletonList(createRoot());
    }
}
