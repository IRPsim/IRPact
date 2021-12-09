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
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.AbstractPVactScenario;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataCreator;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.PVactCagManager;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractToyModel extends AbstractPVactScenario {

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
    protected InDiracUnivariateDistribution dirac06 = new InDiracUnivariateDistribution("dirac06", 0.6);
    protected InDiracUnivariateDistribution dirac07 = new InDiracUnivariateDistribution("dirac07", 0.7);
    protected InDiracUnivariateDistribution dirac072 = new InDiracUnivariateDistribution("dirac072", 0.72);
    protected InDiracUnivariateDistribution dirac08 = new InDiracUnivariateDistribution("dirac08", 0.8);
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

    protected int simulationStartYear = 2008;
    protected int simulationLength = 1;

    public AbstractToyModel(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description);
        this.resultConsumer = Objects.requireNonNull(resultConsumer);
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

    public void init() {
        simulationStartYear = 2008;
        simulationLength = 1;
        initThisCustom();

        initTestData();
        applySpatialDist();
        initCagManager();

        cagManager.apply();
    }

    public AbstractToyModel callInit() {
        init();
        return this;
    }

    protected void applySpatialDist() {
        InFileBasedPVactMilieuSupplier spatialDist = createSpatialDistribution("SpatialDist");
        cagManager.registerForAll(_cag -> {
            _cag.setSpatialDistribution(spatialDist);
        });
    }

    protected void initThisCustom() {
    }

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
        ToyModeltModularProcessModelTemplate mpm = new ToyModeltModularProcessModelTemplate(name);
        mpm.setUncertaintySupplierInstance(createUncertainty("uncert"));
        mpm.setDistanceFilterSupplierInstance(createNodeFilter());
        mpm.setPvFileSupplier(this::getPVFile);
        mpm.setRealAdoptionFileSupplier(this::getRealAdoptionDataFile);
        mpm.createModel();

        setupProcessModel(mpm);

        root.setProcessModel(mpm.getModel());
        root.setImages2(mpm.getImages());
        root.setPostData(mpm.getPostData());
    }

    protected InUncertaintySupplier createUncertainty(String name) {
        return createInPVactUpdatableGlobalModerateExtremistUncertainty("uncert", RAConstants.DEFAULT_EXTREMIST_RATE, RAConstants.DEFAULT_EXTREMIST_UNCERTAINTY, RAConstants.DEFAULT_MODERATE_UNCERTAINTY);
    }

    protected InNodeDistanceFilterScheme createNodeFilter() {
        return createNodeFilterScheme(2);
    }

    protected void setupProcessModel(ToyModeltModularProcessModelTemplate mpm) {
        setDefaultWeights(mpm);

        customProcessModelSetup(mpm);
    }

    protected void setDefaultWeights(ToyModeltModularProcessModelTemplate mpm) {
        mpm.getNpvWeightModule().setScalar(0.125);
        mpm.getPpWeightModule().setScalar(0.125);
        mpm.getLocalWeightModule().setScalar(0.125);
        mpm.getSocialWeightModule().setScalar(0.125);
        mpm.getEnvWeightModule().setScalar(0.25);
        mpm.getNovWeightModule().setScalar(0.25);
    }

    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
    }

    protected void setSimulationDuration(InRoot root) {
        root.getGeneral().setFirstSimulationYear(getSimulationStart());
        root.getGeneral().setLastSimulationYear(getSimulationStart() + getSimulationLength() - 1);
    }

    protected int getSimulationStart() {
        return simulationStartYear;
    }

    protected int getSimulationLength() {
        return simulationLength;
    }

    protected InRoot createRoot() {
        InRoot root = createRootWithInformationsWithFullLogging();

        InSpace2D space2D = createSpace2D("Space2D");
        root.setSpatialModel(space2D);

        InUnitStepDiscreteTimeModel timeModel = createOneWeekTimeModel("Time");
        timeModel.setAmountOfTime(1);
        root.setTimeModel(timeModel);

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
        root.getSpecialPVactInput().setUseConstructionRates(false);
        root.getSpecialPVactInput().setUseRenovationRates(false);

        setColors(root, cagManager.getCagsArray());

        setupGeneral(root.getGeneral());

        return root;
    }

    @Override
    public List<InRoot> createInRootsOLD() {
        return Collections.singletonList(createRoot());
    }
}
