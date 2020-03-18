package de.unileipzig.irpact.jadex.start;

import de.unileipzig.irpact.commons.LogUtil;
import de.unileipzig.irpact.commons.TimeUtil;
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
import de.unileipzig.irpact.io.config.JadexConfiguration;
import de.unileipzig.irpact.io.config.JadexLogConfig;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexTimeModule;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Daniel Abitz
 */
public final class StartSimulation {

    private static final AtomicBoolean RUNNING = new AtomicBoolean(false);
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
        info.setFilename("de.unileipzig.irpact.jadex.agent.consumer.JadexConsumerAgentBDI.class");
        info.addArgument(AGENT_BASE, agentBase);
        return info;
    }

    private static CreationInfo createCompanyAgentInfo(CompanyAgentBase agentBase) {
        CreationInfo info = new CreationInfo();
        info.setName(agentBase.getName());
        info.setFilename("de.unileipzig.irpact.jadex.agent.company.JadexCompanyAgentBDI.class");
        info.addArgument(AGENT_BASE, agentBase);
        return info;
    }

    private static CreationInfo createPointOfSaleAgentInfo(PointOfSaleAgentBase agentBase) {
        CreationInfo info = new CreationInfo();
        info.setName(agentBase.getName());
        info.setFilename("de.unileipzig.irpact.jadex.agent.pos.JadexPointOfSaleAgentBDI.class");
        info.addArgument(AGENT_BASE, agentBase);
        return info;
    }

    private static CreationInfo createPolicyAgentInfo(PolicyAgentBase agentBase) {
        CreationInfo info = new CreationInfo();
        info.setName(agentBase.getName());
        info.setFilename("de.unileipzig.irpact.jadex.agent.policy.JadexPolicyAgentBDI.class");
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

    private static void initLogging(JadexLogConfig logConfig, BasicJadexSimulationEnvironment jenv) {
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
        if(!RUNNING.compareAndSet(false, true)) {
            throw new IllegalStateException("running");
        }

        BasicJadexSimulationEnvironment jenv = (BasicJadexSimulationEnvironment) configuration.getEnvironment();
        initLogging(configuration.getLogConfig(), jenv);

        jenv.getLogger().info("Starting Platform...");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        PlatformConfigurationHandler.addIntranetComm(config);
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        try {
            jenv.setPlatform(platform);

            IClockService clockService = platform.searchService(new ServiceQuery<>(IClockService.class))
                    .get();
            jenv.setClockService(clockService);

            ISimulationService simulationService = platform.searchService(new ServiceQuery<>(ISimulationService.class))
                    .get();
            jenv.setSimulationService(simulationService);

            jenv.setTimeModule(new JadexTimeModule(clockService));

            jenv.validateAll();
            jenv.prepare();
            //=====
            IExternalAccess simulationAgent = platform.createComponent(createSimulationControlAgent(jenv))
                    .get();
            jenv.setSimulationAgent(simulationAgent);

            Set<IExternalAccess> accessSet = new HashSet<>();
            //jeden agent einzeln starten, da batch zu overflow fuehrt ab ca 1k agenten
            for(PolicyAgent policyAgent: configuration.getPolicyAgents()) {
                System.out.println("create: " + policyAgent.getName());
                IExternalAccess access = platform.createComponent(createPolicyAgentInfo((PolicyAgentBase) policyAgent))
                        .get();
                accessSet.add(access);
            }
            for(PointOfSaleAgent pointOfSaleAgent: configuration.getPointOfSaleAgents()) {
                IExternalAccess access = platform.createComponent(createPointOfSaleAgentInfo((PointOfSaleAgentBase) pointOfSaleAgent))
                        .get();
                accessSet.add(access);
            }
            for(CompanyAgent companyAgent: configuration.getCompanyAgents()) {
                IExternalAccess access = platform.createComponent(createCompanyAgentInfo((CompanyAgentBase) companyAgent))
                        .get();
                accessSet.add(access);
            }
            for(ConsumerAgentGroup consumerAgentGroup: configuration.getConsumerAgentGroups()) {
                for(ConsumerAgent consumerAgent: consumerAgentGroup.getEntities()) {
                    IExternalAccess access = platform.createComponent(createConsumerAgentInfo((ConsumerAgentBase) consumerAgent))
                            .get();
                    accessSet.add(access);
                }
            }
            //=====
            jenv.waitForSimulation(accessSet);
            accessSet.clear();
            jenv.initialize();
            jenv.start();
        } catch (Throwable t) {
            platform.killComponent();
            throw t;
        }
    }

    private StartSimulation() {
    }
}
