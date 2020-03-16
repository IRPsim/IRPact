package de.unileipzig.irpact.jadex.examples.old.consumerGoal;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Set;

@Agent(type = BDIAgentFactory.TYPE)
public class CompanyAgentBDI {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeatore;
    @AgentFeature
    protected IExecutionFeature execFeature;

    //args
    protected String name;
    protected Logger logger;
    @Belief
    protected Set<String> products;

    public CompanyAgentBDI() {
    }

    @SuppressWarnings("unchecked")
    protected void initArgs(Map<String, Object> args) {
        name = (String) args.get("name");
        logger = (Logger) args.get("logger");
        products = (Set<String>) args.get("products");
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("created: {}", name);
    }

    @OnStart
    protected void body() {
        logger.debug("body: {}", name);

        execFeature.waitForDelay(3000, ia -> {
            String product = "haus";
            logger.debug("add product '{}'", product);
            products.add(product);
            return IFuture.DONE;
        });
    }

    @OnEnd
    protected void killed() {
        logger.debug("killed: {}", name);
    }
}
