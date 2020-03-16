package de.unileipzig.irpact.jadex.examples.old.discrete;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import org.slf4j.Logger;

import java.util.Map;

@Agent(type = BDIAgentFactory.TYPE)
public class ConsumerAgentBDI {

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

    //=========================
    //Agent
    //=========================

    public ConsumerAgentBDI() {
    }

    protected void initArgs(Map<String, Object> args) {
        name = (String) args.get("name");
        logger = (Logger) args.get("logger");
    }

    @OnInit
    protected void onInit() {
        initArgs(resultsFeature.getArguments());
        logger.debug("[{}] onInit", name);
    }

    @OnStart
    protected void onStart() {
        logger.debug("[{}] onStart", name);
        nextStep(0);
    }

    private void nextStep(int i) {
        if(i < 10) {
            agent.waitForTick(is -> {
                logger.debug("[{}] step: {}", name, i);
                nextStep(i + 1);
                return IFuture.DONE;
            });
        }
    }

    @OnEnd
    protected void onEnd() {
        logger.debug("[{}] onEnd", name);
    }
}
