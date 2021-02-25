package de.unileipzig.irpact.start;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ProxyConsumerAgent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.misc.graphviz.GraphvizConfiguration;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.core.simulation.LifeCycleControl;
import de.unileipzig.irpact.core.simulation.Version;
import de.unileipzig.irpact.io.param.input.GraphvizInputParser;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.JadexInputParser;
import de.unileipzig.irpact.jadex.agents.simulation.ProxySimulationAgent;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.util.JadexSystemOut;
import de.unileipzig.irpact.jadex.util.JadexUtil2;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class IRPact {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IRPact.class);

    public static final String PROXY = "PROXY";

    private static final String SIMULATION_AGENT = "de.unileipzig.irpact.jadex.agents.simulation.JadexSimulationAgentBDI.class";
    private static final String CONSUMER_AGENT = "de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentBDI.class";
    private static final String SIMULATION_AGENT_NAME = "IRPact_Simulation_Agent";

    /**
     * Current version.
     * first digit: major version (large update)
     * second digit: minor version (small update)
     * third digit: patch version (fixes)
     *
     * Used to identify valid input data.
     */
    public static final String VERSION_STRING = "0_0_0";
    public static final Version VERSION = new BasicVersion(VERSION_STRING);

    private final Start clParam;
    private final ResourceLoader resourceLoader;
    private AnnualEntry<InRoot> entry;
    private InRoot inRoot;
    private JadexSimulationEnvironment environment;
    private GraphvizConfiguration graphvizConfiguration;
    private IExternalAccess platform;

    public IRPact(Start clParam, ResourceLoader resourceLoader) {
        this.clParam = clParam;
        this.resourceLoader = resourceLoader;
    }

    private void pulse() {
        environment.getLiveCycleControl().pulse();
    }

    private static Converter converter;
    public static Converter getConverter() {
        if(converter == null) {
            DefinitionCollection dcoll = AnnotationParser.parse(InRoot.CLASSES_WITH_GRAPHVIZ);
            DefinitionMapper dmap = new DefinitionMapper(dcoll);
            converter = new Converter(dmap);
        }
        return converter;
    }

    public static AnnualEntry<InRoot> convert(ObjectNode rootNode) {
        ContentType contentType = ContentTypeDetector.detect(rootNode);
        LOGGER.debug("content type: {}", contentType);
        return ContentTypeDetector.parseFirstEntry(
                rootNode,
                contentType,
                getConverter()
        );
    }

    public void start(ObjectNode jsonRoot) throws Exception {
        parseInputFile(jsonRoot);
        start();
    }

    private void parseInputFile(ObjectNode jsonRoot) {
        entry = convert(jsonRoot);
        inRoot = entry.getData();
    }

    public void start(AnnualEntry<InRoot> entry) throws Exception {
        this.entry = entry;
        inRoot = entry.getData();
        start();
    }

    private void start() throws Exception {
        createSimulationEnvironment();
        createGraphvizConfiguration();

        preAgentCreation();
        initEnvironment();
        validateEnvironment();
        postAgentCreation();

        printInitialNetwork();

        if(clParam.isNoSimulation()) {
            LOGGER.info("execution finished (noSimulation flag set)");
            return;
        }

        if(true) {
            throw new RuntimeException("NOCH NICHT");
        }

        createPlatform();
        preparePlatform();
        setupTimeModel();
        createAgents();
        try {
            waitForCreation();
        } catch (InterruptedException e) {
            LOGGER.warn("waiting interrupted", e);
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

    private void createSimulationEnvironment() throws ParsingException {
        JadexInputParser parser = new JadexInputParser();
        parser.setResourceLoader(resourceLoader);
        environment = parser.parseRoot(inRoot);
        int year = entry.getConfig().getYear();
        environment.getInitializationData().setStartYear(year);
        if(environment.getInitializationData().hasValidEndYear()) {
            LOGGER.info("valid custom end year found, simulation will run multiple years");
        }
    }

    private void createGraphvizConfiguration() throws Exception {
        if(!clParam.hasImagePath()) {
            return;
        }
        LOGGER.info("valid image path, setup graphviz");
        GraphvizInputParser parser = new GraphvizInputParser();
        parser.setEnvironment(environment);
        parser.setImageOutputPath(clParam.getImagePath());
        graphvizConfiguration = parser.parseRoot(inRoot);
    }

    private void preAgentCreation() throws MissingDataException {
        LOGGER.info("preAgentCreation");
        environment.preAgentCreation();
    }

    private void initEnvironment() {
        LOGGER.info("initialize");
        environment.initialize();
    }

    private void validateEnvironment() throws ValidationException {
        LOGGER.info("validate");
        environment.validate();
    }

    private void postAgentCreation() throws MissingDataException {
        LOGGER.info("postAgentCreation");
        environment.postAgentCreation();
    }

    private void printInitialNetwork() throws Exception {
        if(graphvizConfiguration != null) {
            LOGGER.info("create initial network image");
            graphvizConfiguration.printSocialGraph(
                    environment.getNetwork().getGraph(),
                    SocialGraph.Type.COMMUNICATION
            );
        }
    }

    private void createPlatform() {
        LOGGER.info("create Platform");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        platform = Starter.createPlatform(config)
                .get();
        environment.getLiveCycleControl().setPlatform(platform);
        environment.getLiveCycleControl().startKillSwitch();

        LOGGER.debug(IRPSection.INITIALIZATION_PLATFORM, "get ISimulationService");
        ISimulationService simulationService = JadexUtil2.getSimulationService(platform);
        LOGGER.debug(IRPSection.INITIALIZATION_PLATFORM, "get IClockService");
        IClockService clock = simulationService.getClockService();
        environment.getLiveCycleControl().setSimulationService(simulationService);
        environment.getLiveCycleControl().setClockService(clock);
    }

    private void preparePlatform() {
        LOGGER.info("prepare platform start");
        environment.getLiveCycleControl().pause();
        pulse();
    }

    private void setupTimeModel() {
        LOGGER.info("setup time model");
        environment.getTimeModel().setupTimeModel();
    }

    private void createAgents() {
        LOGGER.info("create agents");

        final int totalNumberOfAgents = 1 //SimulationAgent
                + environment.getAgents().getTotalNumberOfConsumerAgents();
        int agentCount = 0;

        LOGGER.debug(IRPSection.INITIALIZATION_PLATFORM, "total number of agents: {}", totalNumberOfAgents);
        environment.getLiveCycleControl().setTotalNumberOfAgents(totalNumberOfAgents);

        CreationInfo simulationAgentInfo = createSimulationAgentInfo(createProxySimulationAgent());
        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "create simulation agent '{}' ({}/{})", SIMULATION_AGENT_NAME, ++agentCount, totalNumberOfAgents);
        platform.createComponent(simulationAgentInfo).get();
        pulse();


        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            //die placeholder-Agenten werden direkt geaendert, damit duerfen wir nicht ueber die Agentenliste selber interieren
            Set<ConsumerAgent> agentsCopy = new HashSet<>(cag.getAgents());
            for(ConsumerAgent ca: agentsCopy) {
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
        LOGGER.info("wait until agent creation is finished...");
        environment.getLiveCycleControl().waitForCreationFinished();
        LOGGER.info("...  agent creation finished");
    }

    private void setupPreSimulationStart() throws MissingDataException {
        LOGGER.info("pre simulation start environment");
        environment.preSimulationStart();
    }

    private void startSimulation() {
        //ConcurrentUtil.sleepSilently(5000);
        LOGGER.info("start simulation");
        environment.getLiveCycleControl().start();
    }

    private void waitForTermination() {
        LOGGER.info("wait for termination");

        //Map<String, Object> result = environment.getLiveCycleControl().waitForTermination().get();
        //ConcurrentUtil.sleepSilently(5000);
        environment.getLiveCycleControl().terminate().get();
        //===
        JadexSystemOut.reset();

        LOGGER.info("simulation terminated");
    }

    private void postSimulation() {
        LOGGER.info("simulation finished");

        BinaryJsonPersistanceManager persistanceManager = new BinaryJsonPersistanceManager();
        persistanceManager.persist(environment);
        LOGGER.info("persistables: {}", persistanceManager.getPersistables().size());

        throw new RuntimeException("TEST");
    }
}
