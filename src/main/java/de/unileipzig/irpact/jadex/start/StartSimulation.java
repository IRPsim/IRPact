package de.unileipzig.irpact.jadex.start;

import de.unileipzig.irpact.commons.LogUtil;
import de.unileipzig.irpact.commons.TimeUtil;
import de.unileipzig.irpact.commons.annotation.Idea;
import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.commons.annotation.ToImpl;
import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.company.CompanyAgentBase;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentBase;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import de.unileipzig.irpact.core.agent.policy.PolicyAgentBase;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgent;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgentBase;
import de.unileipzig.irpact.jadex.agent.simulation.JadexSimulationControlAgent;
import de.unileipzig.irpact.jadex.config.JadexConfiguration;
import de.unileipzig.irpact.jadex.config.JadexLogConfig;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.bridge.service.types.threadpool.IThreadPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.time.LocalDateTime;

/**
 * @author Daniel Abitz
 */
@ToDo("sinnvolle aber einfache schemes bauen zum testen")
public final class StartSimulation {

    //core
    private static final String IRPACT_LOGGER = "IRPact";
    private static final String CONSOLE_APPENDER = "CONSOLE";
    private static final String FILE_APPENDER = "FILE";
    private static final String FILE_ERROR_APPENDER = "FILE_ERROR";
    private static final String FILE_AGENT_APPENDER = "FILE_AGENT";
    private static final String RUN_PROPERTY = "irpact.run";
    public static final String AGENT_BASE = "agentBase";
    //simulation
    private static final String AGENT_NAME = "SimulationController";
    public static final String NAME = "name";
    public static final String ENVIRONMENT = "environment";

    private static CreationInfo createConsumerAgentInfo(ConsumerAgentBase agentBase) {
        CreationInfo info = new CreationInfo();
        info.setName(agentBase.getName());
        info.setFilename("de.unileipzig.irpact.jadex.agent.consumer.JadexConsumerAgent.class");
        info.addArgument(AGENT_BASE, agentBase);
        return info;
    }

    private static CreationInfo createCompanyAgentInfo(CompanyAgentBase agentBase) {
        CreationInfo info = new CreationInfo();
        info.setName(agentBase.getName());
        info.setFilename("de.unileipzig.irpact.jadex.agent.company.JadexCompanyAgent.class");
        info.addArgument(AGENT_BASE, agentBase);
        return info;
    }

    private static CreationInfo createPointOfSaleAgentInfo(PointOfSaleAgentBase agentBase) {
        CreationInfo info = new CreationInfo();
        info.setName(agentBase.getName());
        info.setFilename("de.unileipzig.irpact.jadex.agent.pos.JadexPointOfSaleAgent.class");
        info.addArgument(AGENT_BASE, agentBase);
        return info;
    }

    private static CreationInfo createPolicyAgentInfo(PolicyAgentBase agentBase) {
        CreationInfo info = new CreationInfo();
        info.setName(agentBase.getName());
        info.setFilename("de.unileipzig.irpact.jadex.agent.policy.JadexPolicyAgent.class");
        info.addArgument(AGENT_BASE, agentBase);
        return info;
    }

    private static CreationInfo createSimulationControlAgent(
            JadexSimulationEnvironment environment) {
        CreationInfo info = new CreationInfo();
        info.setName(AGENT_NAME);
        info.setFilename("de.unileipzig.irpact.jadex.agent.simulation.JadexSimulationControlAgent.class");
        info.addArgument(JadexSimulationControlAgent.ENVIRONMENT, environment);
        info.addArgument(JadexSimulationControlAgent.NAME, AGENT_NAME);
        return info;
    }

    private static void init(JadexConfiguration config, BasicJadexSimulationEnvironment jenv) {
        JadexLogConfig logConfig = config.getLogConfig();
        String logFileName = logConfig.getLogFileName();
        if(logFileName == null || logFileName.isEmpty()) {
            LocalDateTime ldt = LocalDateTime.now();
            String ldtStr = ldt.format(TimeUtil.FILE_DATE_TIME_FORMATTER);
            System.setProperty(RUN_PROPERTY, ldtStr);
        } else {
            System.setProperty(RUN_PROPERTY, logFileName);
        }

        Logger irpactLogger = LoggerFactory.getLogger(IRPACT_LOGGER);
        Logger agentRoot = LoggerFactory.getLogger("de.unileipzig.irpact.jadex.agent");
        jenv.setLogger(irpactLogger);

        if(!logConfig.isUseConsole()) {
            LogUtil.disableAppender(irpactLogger, CONSOLE_APPENDER);
            LogUtil.disableAppender(agentRoot, CONSOLE_APPENDER);
        }

        if(!logConfig.isUseFile()) {
            LogUtil.disableAppender(irpactLogger, FILE_APPENDER);
            LogUtil.disableAppender(irpactLogger, FILE_ERROR_APPENDER);
            LogUtil.disableAppender(agentRoot, FILE_AGENT_APPENDER);
            LogUtil.disableAppender(agentRoot, FILE_ERROR_APPENDER);
        }

        if(logConfig.isLevelTrace()) {
            LogUtil.setLevel(irpactLogger, Level.TRACE);
        }
        else if(logConfig.isLevelDebug()) {
            LogUtil.setLevel(irpactLogger, Level.DEBUG);
        }

        if(logConfig.isAgentLevelTrace()) {
            LogUtil.setLevel(agentRoot, Level.TRACE);
        }
        else if(logConfig.isAgentLevelDebug()) {
            LogUtil.setLevel(agentRoot, Level.DEBUG);
        }
    }

    public static void start(JadexConfiguration configuration) {
        BasicJadexSimulationEnvironment jenv = (BasicJadexSimulationEnvironment) configuration.getEnvironment();
        init(configuration, jenv);

        jenv.getLogger().info("Starting Platform...");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        PlatformConfigurationHandler.addIntranetComm(config);
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();
        jenv.setPlatform(platform);

        IClockService clockService = platform.searchService(new ServiceQuery<>(IClockService.class))
                .get();
        jenv.setClockService(clockService);

        ISimulationService simulationService = platform.searchService(new ServiceQuery<>(ISimulationService.class))
                .get();
        jenv.setSimulationService(simulationService);

        simulationService.pause();
        clockService.setClock(IClock.TYPE_CONTINUOUS, platform.searchService(new ServiceQuery<>(IThreadPoolService.class)).get());
        simulationService.start();

        @Idea("noch speichern?")
        IExternalAccess simulationAgent = platform.createComponent(createSimulationControlAgent(jenv))
                .get();

        for(PolicyAgent policyAgent: configuration.getPolicyAgents()) {
            IExternalAccess access = platform.createComponent(createPolicyAgentInfo((PolicyAgentBase) policyAgent))
                    .get();
        }
        for(PointOfSaleAgent pointOfSaleAgent: configuration.getPointOfSaleAgents()) {
            IExternalAccess access = platform.createComponent(createPointOfSaleAgentInfo((PointOfSaleAgentBase) pointOfSaleAgent))
                    .get();
        }
        for(CompanyAgent companyAgent: configuration.getCompanyAgents()) {
            IExternalAccess access = platform.createComponent(createCompanyAgentInfo((CompanyAgentBase) companyAgent))
                    .get();
        }
        for(ConsumerAgentGroup consumerAgentGroup: configuration.getConsumerAgentGroups()) {
            for(ConsumerAgent consumerAgent: consumerAgentGroup.getEntities()) {
                IExternalAccess access = platform.createComponent(createConsumerAgentInfo((ConsumerAgentBase) consumerAgent))
                        .get();
            }
        }
    }

    private StartSimulation() {
    }

    @ToImpl
    public static void main(String[] args) {
    }
}
