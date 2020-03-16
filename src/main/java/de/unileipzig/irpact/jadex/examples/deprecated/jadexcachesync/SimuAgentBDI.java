package de.unileipzig.irpact.jadex.examples.deprecated.jadexcachesync;

import de.unileipzig.irpact.core.agent.AgentIdentifier;
import de.unileipzig.irpact.jadex.agent.JadexAgent;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.micro.annotation.*;
import org.slf4j.Logger;

import java.util.Map;

@Service
@ProvidedServices(
        @ProvidedService(type = ExtAccess.class)
)
@RequiredServices(
        @RequiredService(type = IClockService.class)
)
@Agent(type = BDIAgentFactory.TYPE)
public class SimuAgentBDI implements JadexAgent, ExtAccess {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IMessageFeature msgFeature;

    //args
    protected String name;
    protected Logger logger;
    protected JadexSimulationEnvironment env;

    protected IClockService clock;
    protected ISimulationService simu;

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
        env.registerInternal(name, agent.getId(), this);
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("created: {}", name);

        clock = agent.getFeature(IRequiredServicesFeature.class)
                .getLocalService(IClockService.class);
        logger.debug("clock? {}", clock != null);

        simu = agent.getFeature(IRequiredServicesFeature.class)
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
    public AgentIdentifier getIdentifier() {
        return null;
    }

    @Override
    public void doIt() {
        logger.debug("WO BIN ICH @Starter: {}", Thread.currentThread().getName());
        logger.debug("HAAAAAAAAAAAAAAAAAAAALO");
        IComponentIdentifier[] to = env.getComponentIdentifiers()
                .stream()
                .filter(id -> id != agent.getId())
                .toArray(IComponentIdentifier[]::new);
        msgFeature.sendMessage(AgentState.RESUME, to);
    }
}
