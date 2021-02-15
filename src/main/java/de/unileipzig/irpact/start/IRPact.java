package de.unileipzig.irpact.start;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.misc.graphviz.GraphvizConfiguration;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.LifeCycleControl;
import de.unileipzig.irpact.io.input.GraphvizInputParser;
import de.unileipzig.irpact.io.input.InRoot;
import de.unileipzig.irpact.io.input.JadexInputParser;
import de.unileipzig.irpact.jadex.agents.consumer.ConsumerAgentInitializationData;
import de.unileipzig.irpact.jadex.agents.consumer.PlaceholderConsumerAgent;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationAgentInitializationData;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
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

    public static final String DATA = "DATA";

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
    public static final String VERSION = "0_0_0";

    private final Start clParam;
    private final ObjectNode jsonRoot;
    @SuppressWarnings("FieldCanBeLocal")
    private AnnualEntry<InRoot> entry;
    private InRoot inRoot;
    private JadexSimulationEnvironment environment;
    private GraphvizConfiguration graphvizConfiguration;
    private IExternalAccess platform;

    public IRPact(Start clParam, ObjectNode jsonRoot) {
        this.clParam = clParam;
        this.jsonRoot = jsonRoot;
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

    public void start() throws Exception {
        parseInputFile();
        createSimulationEnvironment();
        if(clParam.hasImagePath()) {
            createGraphvizConfiguration();
        }

        initEnvironment();
        validateEnvironment();
        setupEnvironment();

        if(graphvizConfiguration != null) {
            LOGGER.info("create initial network image");
            graphvizConfiguration.printSocialGraph(
                    environment.getNetwork().getGraph(),
                    SocialGraph.Type.COMMUNICATION
            );
        }

        if(clParam.isNoSimulation()) {
            LOGGER.info("execution finished (noSimulation flag set)");
            return;
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
        startSimulation();
        waitForTermination();
        postSimulation();
    }

    private void parseInputFile() {
        entry = convert(jsonRoot);
        inRoot = entry.getData();
    }

    private void createSimulationEnvironment() throws Exception {
        JadexInputParser parser = new JadexInputParser();
        environment = parser.parse(inRoot, jsonRoot);
        int year = entry.getConfig().getYear();
        environment.getInitializationData().setStartYear(year);
        if(environment.getInitializationData().hasValidEndYear()) {
            LOGGER.info("valid custom end year found, simulation will run multiple years");
        }
    }

    private void createGraphvizConfiguration() throws Exception {
        GraphvizInputParser parser = new GraphvizInputParser();
        parser.setEnvironment(environment);
        parser.setImageOutputPath(clParam.getImagePath());
        graphvizConfiguration = parser.parse(inRoot, jsonRoot);
    }

    private void initEnvironment() {
        LOGGER.info("init environment");
        environment.initialize();
    }

    private void validateEnvironment() throws ValidationException {
        LOGGER.info("validate environment");
        environment.validate();
    }

    private void setupEnvironment() {
        LOGGER.info("setup environment");
        environment.setup();
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
        environment.getTimeModel().setup();
    }

    private void createAgents() {
        LOGGER.info("create agents");

        final int totalNumberOfAgents = 1 //SimulationAgent
                + environment.getAgents().getTotalNumberOfConsumerAgents();
        int agentCount = 0;

        LOGGER.debug(IRPSection.INITIALIZATION_PLATFORM, "total number of agents: {}", totalNumberOfAgents);
        environment.getLiveCycleControl().setTotalNumberOfAgents(totalNumberOfAgents);

        CreationInfo simulationAgentInfo = createSimulationAgentInfo(createSimulationAgentInitializationData());
        LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "create simulation agent '{}' ({}/{})", SIMULATION_AGENT_NAME, ++agentCount, totalNumberOfAgents);
        platform.createComponent(simulationAgentInfo).get();
        pulse();


        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            //die placeholder-Agenten werden direkt geaendert, damit duerfen wir nicht ueber die Agentenliste selber interieren
            Set<ConsumerAgent> setCopy = new HashSet<>(cag.getAgents());
            for(ConsumerAgent ca: setCopy) {
                LOGGER.trace(IRPSection.INITIALIZATION_PLATFORM, "create jadex agent '{}' ({}/{})", ca.getName(), ++agentCount, totalNumberOfAgents);
                ConsumerAgentInitializationData data = createConsumerAgentInitializationData(ca);
                platform.createComponent(createConsumerAgent(data)).get();
                pulse();
            }
        }
    }

    private SimulationAgentInitializationData createSimulationAgentInitializationData() {
        SimulationAgentInitializationData data = new SimulationAgentInitializationData();
        data.setName(SIMULATION_AGENT_NAME);
        data.setEnvironment(environment);
        return data;
    }

    private ConsumerAgentInitializationData createConsumerAgentInitializationData(ConsumerAgent agent) {
        return (PlaceholderConsumerAgent) agent;
    }

    private static CreationInfo createSimulationAgentInfo(SimulationAgentInitializationData data) {
        CreationInfo info = new CreationInfo();
        info.setName(data.getName());
        info.setFilename(SIMULATION_AGENT);
        info.addArgument(DATA, data);
        return info;
    }

    private static CreationInfo createConsumerAgent(ConsumerAgentInitializationData data) {
        CreationInfo info = new CreationInfo();
        info.setName(data.getName());
        info.setFilename(CONSUMER_AGENT);
        info.addArgument(DATA, data);
        return info;
    }

    private void waitForCreation() throws InterruptedException {
        LOGGER.info("wait until agent creation is finished...");
        environment.getLiveCycleControl().waitForCreationFinished();
        LOGGER.info("...  agent creation finished");
    }

    private void startSimulation() {
        LOGGER.info("start simulation");
        environment.getLiveCycleControl().start();
    }

    private void waitForTermination() {
        LOGGER.info("wait for termination");

        //Map<String, Object> result = environment.getLiveCycleControl().waitForTermination().get();
        ConcurrentUtil.sleepSilently(5000);
        environment.getLiveCycleControl().terminate().get();

        LOGGER.info("simulation terminated");
    }

    private void postSimulation() {
        LOGGER.info("simulation finished");
    }
}
