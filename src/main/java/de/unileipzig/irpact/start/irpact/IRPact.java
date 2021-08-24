package de.unileipzig.irpact.start.irpact;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.ImageUtil;
import de.unileipzig.irpact.commons.util.ProgressCalculator;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.AtomicDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.misc.InitializationStage;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.misc.graphviz.GraphvizConfiguration;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.postprocessing.BasicPostprocessingManager;
import de.unileipzig.irpact.core.postprocessing.PostprocessingManager;
import de.unileipzig.irpact.core.simulation.*;
import de.unileipzig.irpact.core.util.BasicMetaData;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.core.postprocessing.data.adoptions.AdoptionResultInfo;
import de.unileipzig.irpact.core.postprocessing.data.adoptions.AnnualCumulativeAdoptionsForOutput;
import de.unileipzig.irpact.io.param.input.*;
import de.unileipzig.irpact.io.param.output.OutInformation;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.io.param.output.agent.OutConsumerAgentGroup;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentBDI;
import de.unileipzig.irpact.jadex.agents.consumer.ProxyConsumerAgent;
import de.unileipzig.irpact.jadex.agents.simulation.ProxyIRPactAgentManagerAgent;
import de.unileipzig.irpact.jadex.agents.simulation.ProxySimulationAgent;
import de.unileipzig.irpact.core.persistence.BasicPersistenceModul;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.util.JadexSystemOut;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.start.Preloader3;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.ContentTypeUtil;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.data.AnnualEntry;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Daniel Abitz
 */
public final class IRPact implements IRPActAccess {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IRPact.class);

    public static final String PROXY = "PROXY";

    private static final String SIMULATION_AGENT = "de.unileipzig.irpact.jadex.agents.simulation.JadexSimulationAgentBDI.class";
    private static final String AGENTMANAGER_AGENT = "de.unileipzig.irpact.jadex.agents.simulation.JadexIRPactAgentManagerAgentBDI.class";
    private static final String CONSUMER_AGENT = "de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentBDI.class";
    private static final String SIMULATION_AGENT_NAME = "IRPact_Simulation_Agent";

    //dran denken die Version auch in der loc.yaml zu aktualisieren
    private static final String MAJOR_STRING = "1";
    private static final String MINOR_STRING = "4";
    private static final String BUILD_STRING = "1";
    public static final String VERSION_STRING = MAJOR_STRING + "_" + MINOR_STRING + "_" + BUILD_STRING;
    public static final Version VERSION = new BasicVersion(MAJOR_STRING, MINOR_STRING, BUILD_STRING);

    public static final String CL_NAME = "IRPact";
    public static final String CL_VERSION = "Version " + VERSION_STRING;
    public static final String CL_COPY = "(c) 2019-2021";

    //https://www.w3schools.com/icons/fontawesome5_icons_alert.asp
    public static final String ICON_IMAGES = "fa fa-plus-square";
    public static final String ICON_IMAGE = "fa fa-file-image-o";
    public static final String ICON_WARNING = "fa fa-warning";

    public static final String IMAGE_AGENTGRAPH_INPUT = "agentGraph";

    public static final String IMAGE_AGENTGRAPH_OUTPUT = "Agentennetzwerk";
    public static final String IMAGE_AGENTGRAPH_OUTPUT_PNG = IMAGE_AGENTGRAPH_OUTPUT + ".png";
    public static final String IMAGE_AGENTGRAPH_OUTPUT_DOT = IMAGE_AGENTGRAPH_OUTPUT + ".dot";

    public static final String IMAGE_STACKTRACE = "Stacktrace";
    public static final String IMAGE_STACKTRACE_PNG = IMAGE_STACKTRACE + ".png";

    public static final String IMAGE_ANNUAL_ADOPTIONS = "JaehrlicheAdoptionenPLZ";
    public static final String IMAGE_ANNUAL_ADOPTIONS_PNG = IMAGE_ANNUAL_ADOPTIONS + ".png";

    public static final String IMAGE_COMPARED_ANNUAL_ADOPTIONS = "JaehrlicheAdoptionenPLZVergleich";
    public static final String IMAGE_COMPARED_ANNUAL_ADOPTIONS_PNG = IMAGE_COMPARED_ANNUAL_ADOPTIONS + ".png";

    public static final String IMAGE_ANNUAL_CUMULATIVE_ADOPTIONS = "JaehrlicheAdoptionenPhase";
    public static final String IMAGE_ANNUAL_CUMULATIVE_ADOPTIONS_PNG = IMAGE_ANNUAL_CUMULATIVE_ADOPTIONS + ".png";

    public static final String DOWNLOAD_DIR_NAME = "images";

    public static final int MODELDEFINITION = 3;

    public static final double MINIMAL_PROGRESS = 0.05;
    public static final int NUMBER_OF_PROGRESS_PHASES = 3;
    public static final int PROGRESS_PHASE_AGENT_CREATION = 1;
    public static final int PROGRESS_PHASE_SIMULATION = 2;
    public static final int PROGRESS_PHASE_AGENT_KILL = 3;

    public static boolean printStacktraceImage = true;

    private static final Map<MainCommandLineOptions, Converter> INPUT_CONVERTS = new WeakHashMap<>();
    private static final Map<MainCommandLineOptions, Converter> OUTPUT_CONVERTS = new WeakHashMap<>();

    private final List<IRPactCallback> CALLBACKS = new ArrayList<>();
    private final MainCommandLineOptions CL_OPTIONS;
    private final BasicMetaData META_DATA = new BasicMetaData();

    private ObjectNode inRootNode;
    private AnnualEntry<InRoot> inEntry;
    private InRoot inRoot;
    private AnnualData<OutRoot> outData;
    private JadexSimulationEnvironment environment;
    private GraphvizConfiguration graphvizConfiguration;
    private IExternalAccess platform;

    public IRPact(
            MainCommandLineOptions clOptions,
            Collection<? extends IRPactCallback> callbacks,
            ResourceLoader resourceLoader) {
        this.CL_OPTIONS = clOptions;
        this.CALLBACKS.addAll(callbacks);

        META_DATA.setLocale(CL_OPTIONS.getLocale(Locale.GERMAN));
        META_DATA.setLoader(resourceLoader);

        LOGGER.trace(IRPSection.GENERAL, "using locale: '{}'" , META_DATA.getLocale().toLanguageTag());
    }

    public void notifyStart() {
        META_DATA.getCurrentRunInfo().setStartTime();
        LOGGER.trace(IRPSection.GENERAL, "set start time: {}", META_DATA.getCurrentRunInfo().getStartTime());
    }

    public void notifyEnd() {
        META_DATA.getCurrentRunInfo().setEndTime();
        LOGGER.trace(IRPSection.GENERAL, "set end time: {}", META_DATA.getCurrentRunInfo().getEndTime());
    }

    public MainCommandLineOptions getOptions() {
        return CL_OPTIONS;
    }

    public ObjectNode getInRootNode() {
        return inRootNode;
    }

    public AnnualEntry<InRoot> getInEntry() {
        return inEntry;
    }

    public InRoot getInRoot() {
        return inRoot;
    }

    public MetaData getMetaData() {
        return META_DATA;
    }

    private static DefinitionMapper createMapper(MainCommandLineOptions options, DefinitionCollection dcoll) {
        if(options != null) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "setup definition mapper: autoTrim={}, maxNameLength={}, minPartLength={}", options.isEnableGamsNameTrimming(), options.getMaxGamsNameLength(), options.getMinGamsPartLength());
        }

        DefinitionMapper mapper = new DefinitionMapper(dcoll, false);
        mapper.setAutoTrimName(options != null && options.isEnableGamsNameTrimming());
        mapper.setMaxNameLength(options == null ? -1 : options.getMaxGamsNameLength());
        mapper.setMinPartLength(options == null ? 1 : options.getMinGamsPartLength());
        mapper.init();
        return mapper;
    }

    public static Converter getInputConverter() {
        return getInputConverter(null);
    }

    public static Converter getInputConverter(MainCommandLineOptions options) {
        return getInputConverter(options, options != null);
    }

    public static Converter getInputConverter(MainCommandLineOptions options, boolean cache) {
        if(INPUT_CONVERTS.containsKey(options)) {
            return INPUT_CONVERTS.get(options);
        } else {
            DefinitionCollection dcoll = AnnotationParser.parse(InRoot.INPUT_WITH_GRAPHVIZ);
            DefinitionMapper dmap = createMapper(options, dcoll);
            Converter converter = new Converter(dmap);
            converter.setAddOnlyRemainingSetsToRoot(false);
            if(cache) {
                INPUT_CONVERTS.put(options, converter);
            }
            return converter;
        }
    }

    public static Converter getOutputConverter() {
        return getOutputConverter(null);
    }

    public static Converter getOutputConverter(MainCommandLineOptions options) {
        return getOutputConverter(options, options != null);
    }

    public static Converter getOutputConverter(MainCommandLineOptions options, boolean cache) {
        if(OUTPUT_CONVERTS.containsKey(options)) {
            return OUTPUT_CONVERTS.get(options);
        } else {
            DefinitionCollection dcoll = AnnotationParser.parse(OutRoot.ALL_CLASSES);
            DefinitionMapper dmap = createMapper(options, dcoll);
            Converter converter = new Converter(dmap);
            converter.setSortNames(false);
            if(cache) {
                OUTPUT_CONVERTS.put(options, converter);
            }
            return converter;
        }
    }

    public static AnnualEntry<OutRoot> convertOutput(MainCommandLineOptions options, ObjectNode rootNode) {
        ContentType contentType = ContentTypeDetector.detect(rootNode);
        LOGGER.trace(IRPSection.GENERAL, "content type: {}", contentType);
        return ContentTypeDetector.parseFirstEntry(
                rootNode,
                contentType,
                getOutputConverter(options)
        );
    }

    public static AnnualEntry<InRoot> convert(MainCommandLineOptions options, ObjectNode rootNode) {
        ContentType contentType = ContentTypeDetector.detect(rootNode);
        LOGGER.trace(IRPSection.GENERAL, "content type: {}", contentType);
        return ContentTypeDetector.parseFirstEntry(
                rootNode,
                contentType,
                getInputConverter(options)
        );
    }

    private AnnualEntry<InRoot> convert(ObjectNode rootNode) {
        peekLoggingOptionsForIRPtools(rootNode);
        return convert(CL_OPTIONS, rootNode);
    }

    protected void peekLoggingOptionsForIRPtools(ObjectNode rootNode) {
        JsonPointer ptr = Preloader3.scalarsPointer(ContentTypeDetector.detect(rootNode), InGeneral.SCA_INGENERAL_LOGALLTOOLS);
        JsonNode logToolsNode = rootNode.at(ptr);
        peekLoggingOptionsForIRPtools(
                logToolsNode != null
                && logToolsNode.isNumber()
                && logToolsNode.intValue() == 1
        );
    }

    protected void peekLoggingOptionsForIRPtools(AnnualEntry<InRoot> entry) {
        peekLoggingOptionsForIRPtools(entry.getData().general.logAllTools);
    }

    protected void peekLoggingOptionsForIRPtools(boolean enableToolsLogging) {
        LOGGER.trace(IRPSection.GENERAL, "enable IRPtools logging: {}", enableToolsLogging);
        if(enableToolsLogging) {
            IRPSection.addAllToolsTo(IRPLogging.getFilter());
        } else {
            IRPSection.removeAllToolsFrom(IRPLogging.getFilter());
        }
    }

    public static void clearConverterCache() {
        INPUT_CONVERTS.clear();
        OUTPUT_CONVERTS.clear();
    }

    private void pulse() {
        environment.getLifeCycleControl().pulse();
    }

    @Deprecated
    public void init(ObjectNode jsonRoot) throws Exception {
        parseInputFile(jsonRoot);
    }

    public void init(ObjectNode jsonRoot, AnnualEntry<InRoot> entry) throws Exception {
        this.inRootNode = jsonRoot;
        this.inEntry = entry;
        this.inRoot = entry.getData();
        peekLoggingOptionsForIRPtools(entry);
    }

    private void parseInputFile(ObjectNode jsonRoot) {
        inRootNode = jsonRoot;
        inEntry = convert(jsonRoot);
        inRoot = inEntry.getData();
    }

    public void init(AnnualEntry<InRoot> entry) throws Exception {
        this.inEntry = entry;
        peekLoggingOptionsForIRPtools(entry);
        inRoot = entry.getData();
    }

    public void initialize() throws Exception {
        initalizeLogging();
        LOGGER.info(IRPSection.GENERAL, "initialize");

        if(hasPreviousState()) {
            restorPreviousSimulationEnvironment();
        } else {
            initializeNewSimulationEnvironment();
        }

        META_DATA.apply(environment.getSettings());

        createGraphvizConfiguration();
        logSimulationInformations();
    }

    private void initalizeLogging() throws ParsingException {
        JadexInputParser parser = new JadexInputParser();
        parser.initLoggingOnly(inRoot);
    }

    private void initializeNewSimulationEnvironment() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "create new environment");
        META_DATA.init();
        createSimulationEnvironment();
        applyCommandLineToEnvironment();
    }

    private void applyCommandLineToEnvironment() {
        Settings settings = environment.getSettings();
        settings.apply(CL_OPTIONS);
    }

    private void createSimulationEnvironment() throws ParsingException {
        int year = inEntry.getConfig().getYear();
        JadexInputParser parser = new JadexInputParser();
        parser.setSimulationYear(year);
        parser.setResourceLoader(META_DATA.getLoader());
        environment = parser.parseRoot(inRoot);
        environment.getSettings().setFirstSimulationYear(year);
    }

    private void restorPreviousSimulationEnvironment() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "restore previous environment");
        int year = inEntry.getConfig().getYear();
        JadexRestoreUpdater updater = new JadexRestoreUpdater();
        updater.setSimulationYear(year);
        updater.setResourceLoader(META_DATA.getLoader());
        BasicPersistenceModul persistenceModul = new BasicPersistenceModul();
        environment = (JadexSimulationEnvironment) persistenceModul.restore(
                META_DATA,
                CL_OPTIONS,
                year,
                updater,
                inRoot
        );
    }

    private void createGraphvizConfiguration() throws Exception {
        GraphvizInputParser parser = new GraphvizInputParser();
        parser.setEnvironment(environment);
        graphvizConfiguration = parser.parseRoot(inRoot);
    }

    private void logSimulationInformations() {
        Settings settings = environment.getSettings();

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "run: {} (first run: {})", settings.getCurrentRun(), settings.isFirstRun());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "last simulation year of previous run: {}", settings.getLastSimulationYearOfPreviousRun());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "first simulation year: {} (actual: {})", settings.getFirstSimulationYear(), settings.getActualFirstSimulationYear());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "last simulation year: {}", settings.getLastSimulationYear());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "number of simulation years: {} (actuel: {})", settings.getNumberOfSimulationYears(), settings.getActualNumberOfSimulationYears());

        if(settings.hasActualMultipleSimulationYears()) {
            LOGGER.info(
                    IRPSection.GENERAL,
                    "simulation will run from {} to {} ({} years)",
                    settings.getActualFirstSimulationYear(),
                    settings.getLastSimulationYear(),
                    settings.getActualNumberOfSimulationYears()
            );
        } else {
            LOGGER.info(IRPSection.GENERAL, "simulation will run for year {}", settings.getFirstSimulationYear());
        }
    }

    private boolean hasPreviousState() {
        return inRoot.hasBinaryPersistData();
    }

    public void preAgentCreation() throws MissingDataException {
        LOGGER.info(IRPSection.GENERAL, "run pre-agent creation");
        environment.preAgentCreation();
    }

    public void runPreAgentCreationTasks() {
        LOGGER.info(IRPSection.GENERAL, "run pre-agent creation tasks");
        environment.getTaskManager().runInitializationStageTasks(InitializationStage.PRE_AGENT_CREATION, environment);
    }

    public void preAgentCreationValidation() throws ValidationException {
        LOGGER.info(IRPSection.GENERAL, "run pre-agent creation validation");
        environment.preAgentCreationValidation();
    }

    public void createAgents() throws InitializationException {
        LOGGER.info(IRPSection.GENERAL, "run create agents");
        environment.createAgents();
    }

    public void postAgentCreation() throws MissingDataException, InitializationException {
        LOGGER.info(IRPSection.GENERAL, "run post agent creation");
        environment.postAgentCreation();
    }

    public void runPostAgentCreationTasks() {
        LOGGER.info(IRPSection.GENERAL, "run post-agent creation tasks");
        environment.getTaskManager().runInitializationStageTasks(InitializationStage.POST_AGENT_CREATION, environment);
    }

    public void postAgentCreationValidation() throws ValidationException {
        LOGGER.info(IRPSection.GENERAL, "run post-agent creation validation");
        environment.postAgentCreationValidation();
    }

    public void runPrePlatformCreationTasks() {
        LOGGER.info(IRPSection.GENERAL, "run pre-platforn creation tasks");
        environment.getTaskManager().runInitializationStageTasks(InitializationStage.PRE_PLATFORM_CREATION, environment);
    }

    public void printInitialNetwork() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "create initial network image: {}", CL_OPTIONS.hasImagePath());
        if(CL_OPTIONS.hasImagePath()) {
            LOGGER.trace(IRPSection.GENERAL, "initial image path: {}", CL_OPTIONS.getImagePath());
            try {
                graphvizConfiguration.printSocialGraph(
                        environment.getNetwork().getGraph(),
                        SocialGraph.Type.COMMUNICATION,
                        CL_OPTIONS.getImagePath(),
                        null
                );
            } catch (TimeoutException e) {
                createStackTraceImage(e, CL_OPTIONS.getImagePath());
                throw e;
            }
        }
    }

    public boolean checkNoSimulationFlag() {
        return CL_OPTIONS.isNoSimulation();
    }

    public void createPlatform() {
        LOGGER.info(IRPSection.GENERAL, "create platform");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        platform = Starter.createPlatform(config)
                .get();
        environment.getLifeCycleControl().setPlatform(platform);
        environment.getLifeCycleControl().startKillSwitch();

        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "get ISimulationService");
        ISimulationService simulationService = JadexUtil.getSimulationService(platform);
        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "get IClockService");
        IClockService clock = simulationService.getClockService();
        environment.getLifeCycleControl().setSimulationService(simulationService);
        environment.getLifeCycleControl().setClockService(clock);
    }

    public void preparePlatform() {
        LOGGER.info(IRPSection.GENERAL, "prepare platform start");
        environment.getLifeCycleControl().pause();
        environment.getLifeCycleControl().setOnHardKill(this::handleHardKill);
        pulse();
    }

    public void setupTimeModel() {
        LOGGER.info(IRPSection.GENERAL, "setup time model");
        environment.getTimeModel().setupTimeModel();
    }

    public void createJadexAgents() {
        createJadexAgents(false);
    }

    public void createOnlyControlJadexAgents() {
        createJadexAgents(true);
    }

    private void createJadexAgents(boolean controlAgentsOnly) {
        LOGGER.info(IRPSection.GENERAL, "create agents (controlAgentsOnly={})", controlAgentsOnly);

        if(controlAgentsOnly) {
            createJadexAgentsWtihDefaultParallelism(true);
        } else {
            int numberOfThreads = inRoot.getGeneral().getNumberOfThreads();
            int totalNumberOfActors = environment.getAgents().getTotalNumberOfConsumerAgents();
            LOGGER.info(IRPSection.GENERAL, "create agents (threads={}, actors={})", numberOfThreads, totalNumberOfActors);
            if(numberOfThreads == 0 || numberOfThreads >= totalNumberOfActors) {
                createJadexAgentsWtihDefaultParallelism(false);
            } else {
                createJadexAgentsWithCustomParallelism();
            }
        }
    }

    private void createJadexAgentsWtihDefaultParallelism(boolean controlAgentsOnly) {
        final int totalNumberOfAgents = controlAgentsOnly
                ? 1
                : 1 + environment.getAgents().getTotalNumberOfConsumerAgents();
        int agentCount = 0;

        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "total number of agents: {}", totalNumberOfAgents);
        environment.getLifeCycleControl().setTotalNumberOfAgents(totalNumberOfAgents);

        CreationInfo simulationAgentInfo = createSimulationAgentInfo(createProxySimulationAgent());
        platform.createComponent(simulationAgentInfo).get();
        pulse();
        broadcastAgentCreationProgress(
                "simulation control agent",
                1, 1,
                ++agentCount, totalNumberOfAgents,
                0
        );

        if(controlAgentsOnly) {
            return;
        }

        final long totalConsumerAgentCount = environment.getAgents().getTotalNumberOfConsumerAgents();
        long consumerAgentCount = 0;
        double lastBroadcastedConsumerAgentProgress = 0;

        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                ProxyConsumerAgent data = createConsumerAgentInitializationData(ca);
                CreationInfo info = createConsumerAgentInfo(data);
                platform.createComponent(info).get();
                pulse();

                lastBroadcastedConsumerAgentProgress = broadcastAgentCreationProgress(
                        "consumer agents",
                        ++consumerAgentCount, totalConsumerAgentCount,
                        ++agentCount, totalNumberOfAgents,
                        lastBroadcastedConsumerAgentProgress
                );
            }
        }
    }

    private double broadcastAgentCreationProgress(
            String agentType,
            long created, long total,
            long createdAll, long totalAll,
            double lastBroadcastedProgress) {
        double progressAll = (double) createdAll / (double) totalAll;
        if(progressAll - lastBroadcastedProgress >= MINIMAL_PROGRESS || createdAll == totalAll || created == total) {
            ProgressCalculator calc = environment.getProgressCalculator();
            calc.setProgress(PROGRESS_PHASE_AGENT_CREATION, progressAll);
            double irpactProgress = calc.getProgress();
            double progress = (double) created / (double) total;

            long maxMem = Runtime.getRuntime().maxMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory();
            long usedMem = totalMem - freeMem;

            LOGGER.info(
                    IRPSection.INITIALIZATION_PLATFORM,
                    "created {}: {}% ({}/{}), total: {}% ({}/{}), IRPact: {}% (mem: max={}, total={}, free={}, used={})",
                    agentType,
                    StringUtil.DF2_POINT.format(progress * 100.0),
                    created, total,
                    StringUtil.DF2_POINT.format(progressAll * 100.0),
                    createdAll, totalAll,
                    StringUtil.DF2_POINT.format(irpactProgress * 100.0),
                    maxMem, totalMem, freeMem, usedMem
            );
            return progressAll;
        } else {
            return lastBroadcastedProgress;
        }
    }

    private void createJadexAgentsWithCustomParallelism() {
        int outerParallelism = inRoot.getGeneral().getOuterParallelism();
        int innerParallelism = inRoot.getGeneral().getInnerParallelism();
        LOGGER.info(IRPSection.GENERAL, "create agents (outerParallelism={}, innerParallelism={})", outerParallelism, innerParallelism);


        AtomicInteger agentCount = new AtomicInteger(0);
        final int totalNumberOfAgents = 1 + //system
                + outerParallelism //manager
                + environment.getAgents().getTotalNumberOfConsumerAgents();

        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "total number of agents: {}", totalNumberOfAgents);
        environment.getLifeCycleControl().setTotalNumberOfAgents(totalNumberOfAgents);

        CreationInfo simulationAgentInfo = createSimulationAgentInfo(createProxySimulationAgent());
        platform.createComponent(simulationAgentInfo).get();
        pulse();
        broadcastAgentCreationProgress(
                "simulation control agent",
                1, 1,
                agentCount.incrementAndGet(), totalNumberOfAgents,
                0
        );

        //prepare IRPact agents
        Map<IRPactAgentFactory, List<Map<String, Object>>> creationData = new LinkedHashMap<>();

        IRPactAgentFactory consumerAgentFactory = JadexConsumerAgentBDI::new;
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                pulse();
                ProxyConsumerAgent data = createConsumerAgentInitializationData(ca);
                Map<String, Object> param = new HashMap<>();
                param.put(PROXY, data);
                creationData.computeIfAbsent(consumerAgentFactory, _fac -> new ArrayList<>()).add(param);
            }
        }

        List<Map<IRPactAgentFactory, List<Map<String, Object>>>> splittedCreationData = CollectionUtil.split(
                creationData,
                outerParallelism
        );

        //create manager
        AtomicDouble agentProgress = new AtomicDouble(0);
        double lastBroadcastedManagerProgress = 0;
        for(int i = 0; i < outerParallelism; i++) {
            ProxyIRPactAgentManagerAgent proxy = new ProxyIRPactAgentManagerAgent();
            proxy.setName("Manager#" + i);
            proxy.setEnvironment(environment);
            proxy.setRnd(environment.getSimulationRandom().deriveInstance());
            proxy.setParallelism(innerParallelism);
            proxy.setCreatedAgentsCounter(agentCount);
            proxy.setTotalNumberOfAgentsToCreate(totalNumberOfAgents);
            proxy.setLastBroadcastedProgress(agentProgress);
            proxy.setExecMode(ExecMode.FIXED);

            Map<IRPactAgentFactory, List<Map<String, Object>>> thisCreationData = splittedCreationData.get(i);
            int iTotal = 0;
            for(Map.Entry<IRPactAgentFactory, List<Map<String, Object>>> entry: thisCreationData.entrySet()) {
                proxy.addAll(entry.getKey(), entry.getValue());
                iTotal += entry.getValue().size();
            }
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "agentCount: i={}, agents={}", i, iTotal);

            CreationInfo info = createAgentManagerInfo(proxy);
            platform.createComponent(info).get();
            pulse();

            lastBroadcastedManagerProgress = broadcastAgentCreationProgress(
                    "manager agents",
                    i+1, outerParallelism,
                    agentCount.incrementAndGet(), totalNumberOfAgents,
                    lastBroadcastedManagerProgress
            );
        }
    }

    private ProxySimulationAgent createProxySimulationAgent() {
        ProxySimulationAgent agent = new ProxySimulationAgent();
        agent.setName(SIMULATION_AGENT_NAME);
        agent.setEnvironment(environment);
        return agent;
    }

    private ProxyConsumerAgent createConsumerAgentInitializationData(ConsumerAgent agent) {
        if(agent instanceof ProxyConsumerAgent) {
            return (ProxyConsumerAgent) agent;
        } else {
            throw new IllegalArgumentException("requires ProxyConsumerAgent, found: " + agent.getClass().getName());
        }
    }

    private static CreationInfo createSimulationAgentInfo(ProxySimulationAgent proxy) {
        CreationInfo info = new CreationInfo();
        info.setName(proxy.getName());
        info.setFilename(SIMULATION_AGENT);
        info.addArgument(PROXY, proxy);
        return info;
    }

    private static CreationInfo createConsumerAgentInfo(ProxyConsumerAgent proxy) {
        CreationInfo info = new CreationInfo();
        info.setName(proxy.getName());
        info.setFilename(CONSUMER_AGENT);
        info.addArgument(PROXY, proxy);
        return info;
    }

    private static CreationInfo createAgentManagerInfo(ProxyIRPactAgentManagerAgent proxy) {
        CreationInfo info = new CreationInfo();
        info.setName(proxy.getName());
        info.setFilename(AGENTMANAGER_AGENT);
        info.addArgument(PROXY, proxy);
        return info;
    }

    public void waitForCreation() throws InterruptedException {
        LOGGER.info(IRPSection.GENERAL, "wait until agent creation is finished...");
        environment.getLifeCycleControl().waitForCreationFinished();
        LOGGER.info(IRPSection.GENERAL, "...  agent creation finished");
    }

    public boolean secureWaitForCreationFailed() {
        try {
            waitForCreation();
            return false;
        } catch (InterruptedException e) {
            LOGGER.warn(IRPSection.GENERAL, "waiting interrupted", e);
            if(environment.getLifeCycleControl().getTerminationState() != LifeCycleControl.TerminationState.RUNNING) {
                environment.getLifeCycleControl().terminateWithError(e);
            }
            return true;
        }
    }

    public void setupPreSimulationStart() throws MissingDataException {
        LOGGER.info(IRPSection.GENERAL, "pre simulation start environment");
        environment.preSimulationStart();
    }

    public void startSimulation() {
        LOGGER.info(IRPSection.GENERAL, "start simulation");
        environment.getLifeCycleControl().start();
    }

    public void waitForTermination() {
        LOGGER.info(IRPSection.GENERAL, "wait for termination");

        environment.getLifeCycleControl().waitForTermination().get();
        environment.getLifeCycleControl().terminationFinished();
        JadexSystemOut.reset();

        LOGGER.info(IRPSection.GENERAL, "simulation terminated");
    }

    public boolean shouldCreateDummyOutputWithErrorMessage() {
        return inRoot != null && inRoot.getGeneral().shouldPassErrorMessageToOutput();
    }

    public void postSimulation() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "start post-simulation");
        createNetworkAfterSimulation();
        createOutput();
        callCallbacks();
        runPostprocessing();
        finalTask();
        createNoErrorImageIfDesired();
    }

    public void postSimulationWithDummyOutput() {
        LOGGER.info(IRPSection.GENERAL, "start dummy post-simulation");
        outData = createDummyOutputData("DUMMY_OUTPUT", null);
        storeOutputData(outData);
        callCallbacks();
        finalTask();
    }

    public void postSimulationWithDummyOutputAndErrorMessageIfDesired(Throwable cause) {
        if(shouldCreateDummyOutputWithErrorMessage()) {
            postSimulationWithDummyOutputAndErrorMessage(cause);
        }
    }

    private void handleHardKill(Throwable cause) {
        LOGGER.info(IRPSection.GENERAL, "handle hard kill, cause: '{}'", cause.getMessage());

        try {
            createStackTraceImage(cause, getStackTraceImagePath());
        } catch (Throwable t) {
            LOGGER.error("failed to create no-error image", t);
        }

        try {
            createAndStoreDummyOutputAndErrorMessage(cause);
        } catch (Throwable t) {
            LOGGER.error("failed to create no-error image", t);
        }

        LOGGER.warn("hard kill");
        System.exit(1);
    }

    public void postSimulationWithDummyOutputAndErrorMessage(Throwable cause) {
        LOGGER.info(IRPSection.GENERAL, "start post-simulation with error ({})", cause.getMessage());

        createAndStoreDummyOutputAndErrorMessage(cause);
        callCallbacks();
        finalTask();
    }

    private void createAndStoreDummyOutputAndErrorMessage(Throwable cause) {
        String errorClass = cause.getClass().getSimpleName();
        //String errorMsg = StringUtil.replaceSpace(cause.getMessage(), "_");
        //String fullMsg = errorClass + "__" + errorMsg;
        OutInformation[] errorInfo = { new OutInformation(errorClass) };

        outData = createDummyOutputData("ERROR_OUTPUT", errorInfo);
        storeOutputData(outData);
    }

    private AnnualData<OutRoot> createDummyOutputData(String dummyName, OutInformation[] informations) {
        OutRoot outRoot = new OutRoot();
        OutConsumerAgentGroup outCag = new OutConsumerAgentGroup(dummyName);
        outCag.setAdoptionsThisPeriod(-1);
        outCag.setAdoptionsCumulative(-1);
        outCag.setInitialAdoptionsThisPeriod(-1);
        outCag.setInitialAdoptionsCumulative(-1);
        outRoot.outConsumerAgentGroups = new OutConsumerAgentGroup[]{outCag};
        outRoot.addInformations(informations);
        return createOutputData(outRoot);
    }

    private void createOutput() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "create output");
        OutRoot outRoot = createOutRoot();
        outData = createOutputData(outRoot);
        storeOutputData(outData);
    }

    private Path getStackTraceImagePath() throws IOException {
        return CL_OPTIONS.getCreatedDownloadDir().resolve(IMAGE_STACKTRACE_PNG);
    }

    private void createNoErrorImageIfDesired() {
        if(inRoot.getGeneral().shouldPrintNoErrorImage()) {
            try {
                createNoErrorImage(getStackTraceImagePath());
            } catch (Throwable t) {
                LOGGER.error("failed to create no-error image", t);
            }
        }
    }

    public void createStackTraceImageIfDesired(Throwable cause) {
        if(cause == null) {
            return;
        }

        boolean createImage = IRPact.printStacktraceImage;

        if(inRoot == null) {
            if(inRootNode != null) {
                try {
                    JsonPointer ptr = ContentTypeUtil.scalars(inRootNode, InGeneral.SCA_INGENERAL_PRINTSTACKTRACEIMAGE);
                    JsonNode node = inRootNode.at(ptr);
                    if(node.isBoolean()) {
                        createImage = node.booleanValue();
                    }
                } catch (Throwable t) {
                    LOGGER.error("JsonPointer failed", t);
                }
            }
        } else {
            createImage = inRoot.getGeneral().shouldPrintStacktraceImage();
        }

        LOGGER.info("create stacktrace image: {}", createImage);
        if(createImage) {
            try {
                createStackTraceImage(cause, getStackTraceImagePath());
            } catch (Throwable t) {
                LOGGER.error("failed to create no-error image", t);
            }
        }
    }

    private void createNetworkAfterSimulation() {
        boolean create = inRoot.getGraphvizGeneral().isStoreEndImage();
        LOGGER.trace(IRPSection.GENERAL, "create network image after finished simulation: {}", create);
        if(create) {
            boolean storeDot = inRoot.getGraphvizGeneral().isStoreDotFile();
            LOGGER.trace(IRPSection.GENERAL, "store dot file: {}", storeDot);
            try {
                Path networkImagePath = CL_OPTIONS.getCreatedDownloadDir().resolve(IMAGE_AGENTGRAPH_OUTPUT_PNG);
                Path networkDotPath = storeDot
                        ? networkImagePath.resolveSibling(IMAGE_AGENTGRAPH_OUTPUT_DOT)
                        : null;

                LOGGER.trace(IRPSection.GENERAL, "store network image: {}", networkImagePath);
                if(storeDot) {
                    LOGGER.trace(IRPSection.GENERAL, "store dot file: {}", networkDotPath);
                }

                graphvizConfiguration.printSocialGraph(
                        environment.getNetwork().getGraph(),
                        SocialGraph.Type.COMMUNICATION,
                        networkImagePath,
                        networkDotPath
                );
            } catch (Throwable t) {
                LOGGER.error("creating network image after finished simulation failed, continue post simulation process", t);
            }
        }
    }

    private OutRoot createOutRoot() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "create OutRoot");
        OutRoot outRoot = new OutRoot();
        applySimulationResult(outRoot);
        applyPersistenceData(outRoot);
        return outRoot;
    }

    private void applySimulationResult(OutRoot outRoot) {
        AnnualCumulativeAdoptionsForOutput analyzer = new AnnualCumulativeAdoptionsForOutput();
        analyzer.setYears(environment.getSettings().listActualYears());
        analyzer.init(environment.getAgents().listAllConsumerAgentGroupNames(), Arrays.asList(true, false));
        analyzer.apply(environment);

        Map<String, OutConsumerAgentGroup> cache = new LinkedHashMap<>();
        for(Object[] entry: analyzer.getData().iterable()) {
            String name = entry[1] + "_" + entry[0];
            OutConsumerAgentGroup cag = cache.computeIfAbsent(name, OutConsumerAgentGroup::new);
            boolean initial = (Boolean) entry[2];
            AdoptionResultInfo resultInfo = (AdoptionResultInfo) entry[3];
            if(initial) {
                cag.setInitialAdoptionsThisPeriod(resultInfo.getValue());
                cag.setInitialAdoptionsCumulative(resultInfo.getCumulativeValue());
            } else {
                cag.setAdoptionsThisPeriod(resultInfo.getValue());
                cag.setAdoptionsCumulative(resultInfo.getCumulativeValue());
            }
        }

        outRoot.outConsumerAgentGroups = cache.values().toArray(new OutConsumerAgentGroup[0]);
        LOGGER.trace(IRPSection.RESULT, "added {} entries to 'outConsumerAgentGroups'", cache.size());
    }

    private void applyPersistenceData(OutRoot outRoot) throws Exception {
        boolean cmdSkipPersist = CL_OPTIONS.isSkipPersist();
        boolean paramSkipPersist = inRoot.getGeneral().isPersistDisabled();
        boolean skipPersist = cmdSkipPersist || paramSkipPersist;
        LOGGER.trace(IRPSection.GENERAL, "skip persistence: {} (cmd={}, param={}))", skipPersist, cmdSkipPersist, paramSkipPersist);
        if(!skipPersist) {
            environment.getPersistenceModul().store(META_DATA, environment, outRoot);
        }
    }

    private AnnualData<OutRoot> createOutputData(OutRoot outRoot) {
        AnnualData<OutRoot> outData = new AnnualData<>(outRoot);
        outData.getConfig().copyFrom(inEntry.getConfig());
        return outData;
    }

    private void storeOutputData(AnnualData<OutRoot> outData) {
        LOGGER.info(IRPSection.GENERAL, "store output: {}", getOutputConverter(CL_OPTIONS));
        try {
            AnnualFile outFile = outData.serialize(getOutputConverter(CL_OPTIONS));
            outFile.store(CL_OPTIONS.getOutputPath());
        } catch (Throwable t) {
            LOGGER.error("saving output failed", t);
        }
    }

    private void callCallbacks() {
        LOGGER.info(IRPSection.GENERAL, "call {} callbacks", CALLBACKS.size());
        for(IRPactCallback callback: CALLBACKS) {
            try {
                callback.onFinished(this);
            } catch (Exception e) {
                LOGGER.error("on running callback '" + callback.getName() + "'", e);
            }
        }
    }

    private void runPostprocessing() {
        PostprocessingManager postprocessor = new BasicPostprocessingManager(META_DATA, CL_OPTIONS, inRoot, environment);
        postprocessor.execute();
    }

    private void finalTask() {
        LOGGER.info(IRPSection.GENERAL, "simulation finished");
        copyLogIfPossible();;
        IRPLogging.terminate();
        clearConverterCache();
    }

    private void copyLogIfPossible() {
        try {
            if(CL_OPTIONS.logToFile() && inRoot.getGeneral().doCopyLogIfPossible()) {
                Path logFile = CL_OPTIONS.getLogPath();
                Path copyTarget = CL_OPTIONS.getCreatedDownloadDir().resolve(logFile.getFileName() + ".copy");
                Files.copy(logFile, copyTarget, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.error("copy log if possible failed", e);
        }
    }

    //=========================
    //util
    //=========================

    public static void createStackTraceImage(Throwable cause, Path target) {
        String msg = cause.getMessage();

        String text;
        if(msg == null || StringUtil.isBlank(msg)) {
            text = StringUtil.printStackTraceWithTitle("(Datum: " + TimeUtil.printNowWithoutMs() + ")\n\nEin Fehler ist aufgetreten!\n\nStacktrace:", cause);
        } else {
            text = StringUtil.printStackTraceWithTitle("(Datum: " + TimeUtil.printNowWithoutMs() + ")\n\nEin Fehler ist aufgetreten: " + msg + "\n\nStacktrace:", cause);
        }

        try {
            ImageUtil.writeText(text, target);
        } catch (Throwable t) {
            LOGGER.error("writing error image failed", t);
        }
    }

    public static void createNoErrorImage(Path target) {
        try {
            ImageUtil.writeText(
                    "(Datum: " + TimeUtil.printNowWithoutMs() + ")\n\nKein Fehler aufgetreten!",
                    target
            );
        } catch (Throwable t) {
            LOGGER.error("writing no-error image failed", t);
        }
    }

    //=========================
    //IRPactAccess
    //=========================

    @Override
    public MainCommandLineOptions getCommandLineOptions() {
        return CL_OPTIONS;
    }

    @Override
    public AnnualEntry<InRoot> getInput() {
        return inEntry;
    }

    @Override
    public AnnualData<OutRoot> getOutput() {
        return outData;
    }
}
