package de.unileipzig.irpact.jadex.examples.deprecated.simulation;

import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.jadex.agent.JadexAgent;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.persistence.IPersistenceService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;

@RequiredServices(
        @RequiredService(type = IClockService.class)
)
@Agent(type = BDIAgentFactory.TYPE)
public class SimuAgentBDI implements JadexAgent {

    @Agent
    protected IInternalAccess internal;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    //args
    protected String name;
    protected Logger logger;
    protected JadexSimulationEnvironment env;

    protected IClockService clock;
    protected ISimulationService simu;
    protected IPersistenceService platformPersi;

    //=========================
    //Goal
    //=========================

    //=========================
    //Plans
    //=========================

    //=========================
    //Agent
    //=========================

    public SimuAgentBDI() {
    }

    protected void initArgs(Map<String, Object> args) {
        name = (String) args.get("name");
        logger = (Logger) args.get("logger");
        env = (JadexSimulationEnvironment) args.get("env");
        env.getCache().register(name, internal.getExternalAccess(), this);
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("created: {}", name);

        clock = internal.getFeature(IRequiredServicesFeature.class)
                .getLocalService(IClockService.class);
        logger.debug("clock? {}", clock != null);

        simu = internal.getFeature(IRequiredServicesFeature.class)
                .getLocalService(ISimulationService.class);
        logger.debug("simu? {}", simu != null);

        /*
        logger.debug("paused? {}", simu.isExecuting().get());
        simu.pause().get();
        logger.debug("paused? {}", simu.isExecuting().get());
        */
        logger.debug("??? {}", simu.getClockService().getClockType());
    }

    @OnStart
    protected void body() {
        logger.debug("body: {}", name);


        Collection<IPersistenceService> persis = reqFeature.searchServices(JadexUtil.searchPlatform(IPersistenceService.class))
                .get();
        System.out.println(persis);
    }

    @OnEnd
    protected void killed() {
        logger.debug("killed: {}", name);
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return env;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean is(EntityType type) {
        throw new UnsupportedOperationException();
    }
}
