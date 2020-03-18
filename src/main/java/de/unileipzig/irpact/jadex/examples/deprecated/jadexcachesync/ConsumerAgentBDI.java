package de.unileipzig.irpact.jadex.examples.deprecated.jadexcachesync;

import de.unileipzig.irpact.core.message.MessageContent;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.jadex.agent.JadexAgent;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.*;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.types.security.ISecurityInfo;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import org.slf4j.Logger;

import java.util.Map;

@Agent(type = BDIAgentFactory.TYPE)
public class ConsumerAgentBDI implements JadexAgent {

    @Agent
    protected IInternalAccess internal;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IMessageFeature msgFeature;
    protected IMessageHandler messageHandler = new ThisMessageHandler();
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
    //Message
    //=========================

    public class ThisMessageHandler implements IMessageHandler {

        @Override
        public boolean isHandling(ISecurityInfo secinfos, IMsgHeader header, Object msg) {
            logger.debug("[{}] isHandling", name);
            return true;
        }

        @Override
        public boolean isRemove() {
            return false;
        }

        @Override
        public void handleMessage(ISecurityInfo secinfos, IMsgHeader header, Object msg) {
            logger.debug("[{}] handleMessage: {}", name, msg);
            if(msg == AgentState.SUSPEND) {
                execFeature.suspendComponent();
            }
            if(msg == AgentState.RESUME) {
                execFeature.resumeComponent();
            }
        }
    }

    //=========================
    //Agent
    //=========================

    public ConsumerAgentBDI() {
    }

    protected void initArgs(Map<String, Object> args) {
        name = (String) args.get("name");
        logger = (Logger) args.get("logger");
        env = (JadexSimulationEnvironment) args.get("env");
        env.getConfiguration().register(internal.getExternalAccess(), this);
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("created: {}", name);
        msgFeature.addMessageHandler(messageHandler);
    }

    @OnStart
    protected void body() {
        logger.debug("body: {}", name);
        execFeature.suspendComponent();

        execFeature.waitForDelay(6000, is1 -> {
            execFeature.scheduleStep(is2 -> {
                msgFeature.sendMessage("HALLO", internal.getId()).get();
                return IFuture.DONE;
            });
            return IFuture.DONE;
        });

        /*
        msgFeature.sendMessage("HALLO", agent.getId()).get();
        execFeature.scheduleStep(ia -> {
            msgFeature.sendMessage("HALLO", agent.getId()).get();
            return IFuture.DONE;
        });
        */

        /*
        execFeature.suspendComponent();
        execFeature.scheduleStep(ia2 -> {
            logger.debug("[{}] TEST", name);
            return IFuture.DONE;
        });

        ConcurrentUtil.start(12000, () -> {
            logger.debug("RESUME COMPONENT");
            execFeature.resumeComponent();
        });
        */

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
    public boolean isHandling(de.unileipzig.irpact.core.agent.Agent sender, MessageContent content) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleMessage(de.unileipzig.irpact.core.agent.Agent sender, MessageContent content) {
        throw new UnsupportedOperationException();
    }
}
