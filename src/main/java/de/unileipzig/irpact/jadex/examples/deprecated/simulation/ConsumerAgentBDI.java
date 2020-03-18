package de.unileipzig.irpact.jadex.examples.deprecated.simulation;

import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.jadex.agent.JadexAgent;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import org.slf4j.Logger;

import java.util.Map;

@Agent(type = BDIAgentFactory.TYPE)
public class ConsumerAgentBDI implements JadexAgent {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;

    //args
    protected String name;
    protected Logger logger;
    protected JadexSimulationEnvironment env;

    //=========================
    //Goal
    //=========================

    //=========================
    //Plans
    //=========================

    //=========================
    //Agent
    //=========================

    public ConsumerAgentBDI() {
    }

    protected void initArgs(Map<String, Object> args) {
        name = (String) args.get("name");
        logger = (Logger) args.get("logger");
        env = (JadexSimulationEnvironment) args.get("env");
        env.getConfiguration().register(agent.getExternalAccess(), this);
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("created: {}", name);
    }

    @OnStart
    protected void body() {
        logger.debug("body: {}", name);

        /*
        //suspend + resume geht
        execFeature.waitForDelay(3000, ia -> {
            logger.debug("SUPSPEND COMPONENT");
            execFeature.suspendComponent().get();

            execFeature.scheduleStep(ia2 -> {
                logger.debug("TEST");
                return IFuture.DONE;
            });
            return IFuture.DONE;
        });

        ConcurrentUtil.start(7000, () -> {
            logger.debug("RESUME COMPONENT");
            execFeature.resumeComponent();
        });
        */
        /*
        execFeature.waitForDelay(7000, ia2 -> {
            logger.debug("RESUME COMPONENT");
            execFeature.resumeComponent();
            return IFuture.DONE;
        });
        */

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

    @Override
    public boolean isHandling(de.unileipzig.irpact.core.agent.Agent sender, Message content) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleMessage(de.unileipzig.irpact.core.agent.Agent sender, Message content) {
        throw new UnsupportedOperationException();
    }
}
