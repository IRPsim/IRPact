package de.unileipzig.irpact.start.irpact2;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.v2.commons.time.TimeMode;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.develop.ToDo;
import de.unileipzig.irpact.v2.io.input.IRoot;
import de.unileipzig.irpact.v2.jadex.JadexConstants;
import de.unileipzig.irpact.v2.jadex.agents.consumer.JadexConsumerAgentInitializationData;
import de.unileipzig.irpact.v2.jadex.agents.simulation.JadexSimulationAgentInitializationData;
import de.unileipzig.irpact.v2.jadex.simulation.BasicJadexSimulationControl;
import de.unileipzig.irpact.v2.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.v2.jadex.time.AbstractJadexTimeModel;
import de.unileipzig.irpact.v2.jadex.time.ContinuousTimeModel;
import de.unileipzig.irpact.v2.jadex.time.DiscreteTimeModel;
import de.unileipzig.irptools.io.base.AnnualEntry;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Abitz
 */
public class Platform {

    private static final Logger logger = LoggerFactory.getLogger(Platform.class);

    private AnnualEntry<IRoot> annualEntry;
    private JadexSimulationEnvironment environment;
    private IExternalAccess platform;

    public Platform() {
    }

    private int getYear() {
        return annualEntry.getConfig().getYear();
    }

    public void init(AnnualEntry<IRoot> annualEntry, JadexSimulationEnvironment environment) {
        this.annualEntry = annualEntry;
        this.environment = environment;
        createPlatform();
        setupTimeModel();
    }

    private void createPlatform() {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        platform = Starter.createPlatform(config).get();
    }

    private void setupTimeModel() {
        IClockService clock = JadexUtil.getClockService(platform);
        ISimulationService simulationService = JadexUtil.getSimulationService(platform);
        AbstractJadexTimeModel timeModel = (AbstractJadexTimeModel) environment.getTimeModel();
        timeModel.setClock(clock);
        timeModel.setSimulation(simulationService);

        simulationService.pause().get();
        if(timeModel.getMode() == TimeMode.CONTINUOUS) {
            setupContinuousTimeModel();
        } else {
            setupDiscreteTimeModel();
        }
    }

    private void setupDiscreteTimeModel() {
        DiscreteTimeModel timeModel = (DiscreteTimeModel) environment.getTimeModel();
        timeModel.getSimulationService().setClockType(IClock.TYPE_EVENT_DRIVEN).get();
        timeModel.getClockService().setDelta(timeModel.getStoredDelta());

        //testen ob das noetig ist (reconfig?)
        timeModel.getSimulationService().start().get();
        timeModel.getSimulationService().pause().get();
        timeModel.getClockService().stop();

        timeModel.setStartYear(getYear(), timeModel.getStoredTimePerTickInMs());
        timeModel.setStartTick(timeModel.getClockService().getTick());
        timeModel.setEnd(timeModel.plusYears(timeModel.getStart(), 1L));
        logger.info("DiscreteTimeModel delta: {}", timeModel.getClockService().getDelta());
    }

    private void setupContinuousTimeModel() {
        ContinuousTimeModel timeModel = (ContinuousTimeModel) environment.getTimeModel();
        timeModel.getSimulationService().setClockType(IClock.TYPE_CONTINUOUS).get();
        timeModel.getClockService().setDilation(timeModel.getStoredDilation());

        //testen ob das noetig ist (reconfig?)
        timeModel.getSimulationService().start().get();
        timeModel.getSimulationService().pause().get();
        timeModel.getClockService().stop();

        timeModel.setStartYear(getYear());
        timeModel.setStartTime(timeModel.getClockService().getStarttime());
        timeModel.setEnd(timeModel.plusYears(timeModel.getStart(), 1L));
        logger.info("ContinuousTimeModel dilation: {}", timeModel.getClockService().getDilation());
    }

    private int countAgents() {
        return 1 //SimulationAgeht
                + environment.getAgents().sumNumberOfConsumerAgents(); //ConsumerAgents
    }

    private void prepareControl() {
        BasicJadexSimulationControl control = new BasicJadexSimulationControl();
        int numberOfAgents = countAgents();
        logger.info("number of agents: {}", numberOfAgents);
        control.setNumberOfAgents(numberOfAgents);

        ((BasicJadexSimulationEnvironment) environment).setSimulationControl(control);
    }

    private static CreationInfo createSimulationAgent(JadexSimulationAgentInitializationData data) {
        CreationInfo info = new CreationInfo();
        info.setName(data.getName());
        info.setFilename("de.unileipzig.irpact.v2.jadex.agents.simulation.JadexSimulationAgentBDI.class");
        info.addArgument(JadexConstants.DATA, data);
        return info;
    }

    private void createSimulationAgent() {
        JadexSimulationAgentInitializationData data = new JadexSimulationAgentInitializationData();
        data.setName("SimulationAgent");
        data.setEnvironment(environment);
        CreationInfo info = createSimulationAgent(data);
        IExternalAccess agent = platform.createComponent(info).get();
    }

    private static CreationInfo createConsumerAgent(JadexConsumerAgentInitializationData data) {
        CreationInfo info = new CreationInfo();
        info.setName(data.getName());
        info.setFilename("de.unileipzig.irpact.v2.jadex.agents.consumer.JadexConsumerAgentBDI.class");
        info.addArgument(JadexConstants.DATA, data);
        return info;
    }

    private void createConsumerAgents() {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            int numberOfAgents = environment.getAgents().getNumberOfAgents(cag);
            for(int i = 0; i < numberOfAgents; i++) {
                JadexConsumerAgentInitializationData data = (JadexConsumerAgentInitializationData) cag.derive();
                CreationInfo info = createConsumerAgent(data);
                IExternalAccess agent = platform.createComponent(info).get();
            }
        }
    }

    private void createAgents() {
        createSimulationAgent();
        createConsumerAgents();
    }

    @ToDo("sleepkill entfernen")
    public void run() throws InterruptedException {
        createPlatform();
        setupTimeModel();
        prepareControl();
        createAgents();

        logger.info("wait for agent creation...");
        environment.getSimulationControl().waitForCreationFinished();
        logger.info("start clock");
        environment.getTimeModel().getSimulationService().start().get();
        environment.getTimeModel().getClockService().start();

        ConcurrentUtil.sleepSilently(10000);
        platform.killComponent().get();
    }
}
