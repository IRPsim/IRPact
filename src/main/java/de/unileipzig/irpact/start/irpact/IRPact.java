package de.unileipzig.irpact.start.irpact;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.misc.InitializationStage;
import de.unileipzig.irpact.core.simulation.*;
import de.unileipzig.irpact.core.util.PVactResultLogging;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.io.param.output.OutAdoptionResult;
import de.unileipzig.irpact.jadex.agents.consumer.ProxyConsumerAgent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.misc.graphviz.GraphvizConfiguration;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.io.param.input.GraphvizInputParser;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.JadexInputParser;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.jadex.agents.simulation.ProxySimulationAgent;
import de.unileipzig.irpact.jadex.persistance.JadexPersistenceModul;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.util.JadexSystemOut;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class IRPact implements IRPActAccess {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IRPact.class);

    public static final String PROXY = "PROXY";

    private static final String SIMULATION_AGENT = "de.unileipzig.irpact.jadex.agents.simulation.JadexSimulationAgentBDI.class";
    private static final String CONSUMER_AGENT = "de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentBDI.class";
    private static final String SIMULATION_AGENT_NAME = "IRPact_Simulation_Agent";

    private static final String MAJOR_STRING = "0";
    private static final String MINOR_STRING = "0";
    private static final String BUILD_STRING = "0";
    public static final String VERSION_STRING = MAJOR_STRING + "_" + MINOR_STRING + "_" + BUILD_STRING;
    public static final Version VERSION = new BasicVersion(MAJOR_STRING, MINOR_STRING, BUILD_STRING);

    public static final String CL_NAME = "IRPact";
    public static final String CL_VERSION = "Version " + VERSION_STRING;
    public static final String CL_COPY = "(c) 2019-2021";

    private static final Map<MainCommandLineOptions, Converter> INPUT_CONVERTS = new WeakHashMap<>();
    private static final Map<MainCommandLineOptions, Converter> OUTPUT_CONVERTS = new WeakHashMap<>();

    private final List<IRPactCallback> CALLBACKS = new ArrayList<>();
    private final MainCommandLineOptions CL_OPTIONS;
    private final ResourceLoader RESOURCE_LOADER;

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
        this.RESOURCE_LOADER = resourceLoader;
    }

    private static DefinitionMapper createMapper(MainCommandLineOptions options, DefinitionCollection dcoll) {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "setup definition mapper: autoTrim={}, maxNameLength={}, minPartLength={}", options.isEnableGamsNameTrimming(), options.getMaxGamsNameLength(), options.getMinGamsPartLength());
        DefinitionMapper mapper = new DefinitionMapper(dcoll, false);
        mapper.setAutoTrimName(options.isEnableGamsNameTrimming());
        mapper.setMaxNameLength(options.getMaxGamsNameLength());
        mapper.setMinPartLength(options.getMinGamsPartLength());
        mapper.init();
        return mapper;
    }

    public static Converter getInputConverter(MainCommandLineOptions options) {
        if(INPUT_CONVERTS.containsKey(options)) {
            return INPUT_CONVERTS.get(options);
        } else {
            DefinitionCollection dcoll = AnnotationParser.parse(InRoot.CLASSES_WITH_GRAPHVIZ);
            DefinitionMapper dmap = createMapper(options, dcoll);
            Converter converter = new Converter(dmap);
            INPUT_CONVERTS.put(options, converter);
            return converter;
        }
    }

    public static Converter getOutputConverter(MainCommandLineOptions options) {
        if(OUTPUT_CONVERTS.containsKey(options)) {
            return OUTPUT_CONVERTS.get(options);
        } else {
            DefinitionCollection dcoll = AnnotationParser.parse(OutRoot.CLASSES);
            DefinitionMapper dmap = createMapper(options, dcoll);
            Converter converter = new Converter(dmap);
            OUTPUT_CONVERTS.put(options, converter);
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
        return convert(CL_OPTIONS, rootNode);
    }

    public static void clearConverterCache() {
        INPUT_CONVERTS.clear();
        OUTPUT_CONVERTS.clear();
    }

    private void pulse() {
        environment.getLiveCycleControl().pulse();
    }

    public void start(ObjectNode jsonRoot) throws Exception {
        parseInputFile(jsonRoot);
        start();
    }

    private void parseInputFile(ObjectNode jsonRoot) {
        inEntry = convert(jsonRoot);
        inRoot = inEntry.getData();
    }

    public void start(AnnualEntry<InRoot> entry) throws Exception {
        this.inEntry = entry;
        inRoot = entry.getData();
        start();
    }

    private void start() throws Exception {
        initialize();

        preAgentCreation();
        runPreAgentCreationTasks();
        preAgentCreationValidation();

        createAgents();

        postAgentCreation();
        runPostAgentCreationTasks();
        postAgentCreationValidation();

        runPrePlatformCreationTasks();

        if(CL_OPTIONS.isNoSimulation()) {
            if(CL_OPTIONS.hasImagePath()) {
                LOGGER.info(IRPSection.GENERAL, "create initial network image");
                printNetwork();
            }
            LOGGER.info(IRPSection.GENERAL, "no simulation");
            return;
        }

        createPlatform();
        preparePlatform();
        setupTimeModel();
        createJadexAgents();
        try {
            waitForCreation();
        } catch (InterruptedException e) {
            LOGGER.warn(IRPSection.GENERAL, "waiting interrupted", e);
            if(environment.getLiveCycleControl().getTerminationState() != LifeCycleControl.TerminationState.NOT) {
                environment.getLiveCycleControl().terminateWithError(e);
            }
            return;
        }
        setupPreSimulationStart();
        startSimulation();
        waitForTermination();
        postSimulation();
    }

    private void initialize() throws Exception {
        if(hasPreviousState()) {
            restorPreviousSimulationEnvironment();
        } else {
            initializeNewSimulationEnvironment();
        }

        createGraphvizConfiguration();
        logSimulationInformations();
    }

    private void initializeNewSimulationEnvironment() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "create new environment");
        createSimulationEnvironment();
        applyCommandLineToEnvironment();
    }

    private void createSimulationEnvironment() throws ParsingException {
        int year = inEntry.getConfig().getYear();
        JadexInputParser parser = new JadexInputParser();
        parser.setSimulationYear(year);
        parser.setResourceLoader(RESOURCE_LOADER);
        environment = parser.parseRoot(inRoot);
        environment.getSettings().setFirstSimulationYear(year);
    }

    private void applyCommandLineToEnvironment() {
        Settings settings = environment.getSettings();
        settings.apply(CL_OPTIONS);
    }

    private void restorPreviousSimulationEnvironment() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "restore previous environment");
        JadexPersistenceModul persistenceModul = new JadexPersistenceModul();
        SimulationEnvironment restoredEnvironment = persistenceModul.restore(CL_OPTIONS, inRoot);
        this.environment = (JadexSimulationEnvironment) restoredEnvironment;
    }

    private void createGraphvizConfiguration() throws Exception {
        if(!CL_OPTIONS.hasImagePath()) {
            return;
        }
        LOGGER.info(IRPSection.GENERAL, "valid image path, setup graphviz");
        GraphvizInputParser parser = new GraphvizInputParser();
        parser.setEnvironment(environment);
        parser.setImageOutputPath(CL_OPTIONS.getImagePath());
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

    private void preAgentCreation() throws MissingDataException {
        LOGGER.info(IRPSection.GENERAL, "run pre-agent creation");
        environment.preAgentCreation();
    }

    private void runPreAgentCreationTasks() {
        LOGGER.info(IRPSection.GENERAL, "run pre-agent creation tasks");
        environment.getTaskManager().runInitializationStageTasks(InitializationStage.PRE_AGENT_CREATION, environment);
    }

    private void preAgentCreationValidation() throws ValidationException {
        LOGGER.info(IRPSection.GENERAL, "run pre-agent creation validation");
        environment.preAgentCreationValidation();
    }

    private void createAgents() throws InitializationException {
        LOGGER.info(IRPSection.GENERAL, "run create agents");
        environment.createAgents();
    }

    private void postAgentCreation() throws MissingDataException, InitializationException {
        LOGGER.info(IRPSection.GENERAL, "run post agent creation");
        environment.postAgentCreation();
    }

    private void runPostAgentCreationTasks() {
        LOGGER.info(IRPSection.GENERAL, "run post-agent creation tasks");
        environment.getTaskManager().runInitializationStageTasks(InitializationStage.POST_AGENT_CREATION, environment);
    }

    private void postAgentCreationValidation() throws ValidationException {
        LOGGER.info(IRPSection.GENERAL, "run post-agent creation validation");
        environment.postAgentCreationValidation();
    }

    private void runPrePlatformCreationTasks() {
        LOGGER.info(IRPSection.GENERAL, "run pre-platforn creation tasks");
        environment.getTaskManager().runInitializationStageTasks(InitializationStage.PRE_PLATFORM_CREATION, environment);
    }

    private void printNetwork() throws Exception {
        graphvizConfiguration.printSocialGraph(
                environment.getNetwork().getGraph(),
                SocialGraph.Type.COMMUNICATION
        );
    }

    private void createPlatform() {
        LOGGER.info(IRPSection.GENERAL, "create platform");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        platform = Starter.createPlatform(config)
                .get();
        environment.getLiveCycleControl().setPlatform(platform);
        environment.getLiveCycleControl().startKillSwitch();

        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "get ISimulationService");
        ISimulationService simulationService = JadexUtil.getSimulationService(platform);
        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "get IClockService");
        IClockService clock = simulationService.getClockService();
        environment.getLiveCycleControl().setSimulationService(simulationService);
        environment.getLiveCycleControl().setClockService(clock);
    }

    private void preparePlatform() {
        LOGGER.info(IRPSection.GENERAL, "prepare platform start");
        environment.getLiveCycleControl().pause();
        pulse();
    }

    private void setupTimeModel() {
        LOGGER.info(IRPSection.GENERAL, "setup time model");
        environment.getTimeModel().setupTimeModel();
    }

    private void createJadexAgents() {
        LOGGER.info(IRPSection.GENERAL, "create agents");

        final int totalNumberOfAgents = 1 //SimulationAgent
                + environment.getAgents().getTotalNumberOfConsumerAgents();
        int agentCount = 0;

        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "total number of agents: {}", totalNumberOfAgents);
        environment.getLiveCycleControl().setTotalNumberOfAgents(totalNumberOfAgents);

        CreationInfo simulationAgentInfo = createSimulationAgentInfo(createProxySimulationAgent());
        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "create simulation agent '{}' ({}/{})", SIMULATION_AGENT_NAME, ++agentCount, totalNumberOfAgents);
        platform.createComponent(simulationAgentInfo).get();
        pulse();


        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "create jadex agent '{}' ({}/{})", ca.getName(), ++agentCount, totalNumberOfAgents);
                ProxyConsumerAgent data = createConsumerAgentInitializationData(ca);
                CreationInfo info = createConsumerAgentInfo(data);
                platform.createComponent(info).get();
                pulse();
            }
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

    private void waitForCreation() throws InterruptedException {
        LOGGER.info(IRPSection.GENERAL, "wait until agent creation is finished...");
        environment.getLiveCycleControl().waitForCreationFinished();
        LOGGER.info(IRPSection.GENERAL, "...  agent creation finished");
    }

    private void setupPreSimulationStart() throws MissingDataException {
        LOGGER.info(IRPSection.GENERAL, "pre simulation start environment");
        environment.preSimulationStart();
    }

    private void startSimulation() {
        LOGGER.info(IRPSection.GENERAL, "start simulation");
        environment.getLiveCycleControl().start();
    }

    private void waitForTermination() {
        LOGGER.info(IRPSection.GENERAL, "wait for termination");

        environment.getLiveCycleControl().waitForTermination().get();
        JadexSystemOut.reset();

        LOGGER.info(IRPSection.GENERAL, "simulation terminated");
    }

    private void postSimulation() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "simulation finished");
        if(CL_OPTIONS.hasImagePath()) {
            LOGGER.info(IRPSection.GENERAL, "create network image after simulation finished");
            printNetwork();
        }
        createOutput();
        callCallbacks();
        printLoggingOutput();
        finalTask();
    }

    private void createOutput() throws Exception {
        OutRoot outRoot = createOutRoot();
        outData = createOutputData(outRoot);
        storeOutputData(outData);
    }

    private OutRoot createOutRoot() throws Exception {
        LOGGER.info(IRPSection.GENERAL, "create output");
        OutRoot outRoot = new OutRoot();
        applySimulationResult(outRoot);
        applyPersistenceData(outRoot);
        return outRoot;
    }

    private void applySimulationResult(OutRoot outRoot) {
        List<OutCustom> outList = new ArrayList<>();
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            int size = cag.getNumberOfAgents();
            OutCustom oc = new OutCustom(cag.getName(), size);
            outList.add(oc);
        }
        outRoot.outGrps = outList.toArray(new OutCustom[0]);

        //=====
        Timestamp start = environment.getTimeModel().startTime();
        Timestamp end = environment.getTimeModel().endTime();

        List<OutAdoptionResult> outResults = new ArrayList<>();
        if(true) throw new TodoException();
//        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
//            OutAdoptionResult outResult = new OutAdoptionResult(cag.getName() + "_" + start.getYear());
//            int adoptions = 0;
//            for(ConsumerAgent ca: cag.getAgents()) {
//                for(AdoptedProduct ap: ca.getAdoptedProducts()) {
//                    if(ap.isInitial() || ap.getTimestamp().isBetween(start, end)) {
//                        adoptions++;
//                    }
//                }
//            }
//            outResult.setAdoptions(adoptions);
//            outResult.setShare((double) adoptions / (double) cag.getNumberOfAgents());
//            outResults.add(outResult);
//        }
        outRoot.adoptionResults = outResults.toArray(new OutAdoptionResult[0]);
    }

    private void applyPersistenceData(OutRoot outRoot) throws Exception {
        if(CL_OPTIONS.isSkipPersist()) {
            LOGGER.trace(IRPSection.GENERAL, "skip persistence");
        } else {
            environment.getPersistenceModul().store(environment, outRoot);
        }
    }

    private AnnualData<OutRoot> createOutputData(OutRoot outRoot) {
        AnnualData<OutRoot> outData = new AnnualData<>(outRoot);
        outData.getConfig().copyFrom(inEntry.getConfig());
        return outData;
    }

    private void storeOutputData(AnnualData<OutRoot> outData) {
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

    private void printLoggingOutput() {
        PVactResultLogging logging = new PVactResultLogging(CL_OPTIONS, environment, true);
        logging.execute();
    }

    private void finalTask() {
        IRPLogging.removeFilter();
        IRPtools.setLoggingFilter(null);
        clearConverterCache();
        LOGGER.info(IRPSection.GENERAL, "simulation finished");
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
