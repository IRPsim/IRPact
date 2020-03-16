package de.unileipzig.irpact.jadex.examples.old.mapAccess;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    //args
    protected String name;
    protected Logger logger;
    @Belief
    //protected Map<String, Double> myMap = new HashMap<>();
    protected Set<String> myMap = new HashSet<>();

    //=========================
    //Goal
    //=========================

    @Goal
    public class AdoptProductGoal {

        @GoalParameter
        protected String product;

        @GoalCreationCondition(factadded = "myMap")
        public AdoptProductGoal(String product) {
            this.product = product;
        }

        public String getProduct() {
            return product;
        }
    }

    //=========================
    //Plans
    //=========================

    @Plan(trigger = @Trigger(goals = AdoptProductGoal.class))
    protected void adopt(String product) {
        logger.debug("> {} {}", product, "myMap.get(product)");
    }

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
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("created: {}", name);
    }

    @OnStart
    protected void body() {
        logger.debug("body: {}", name);

        //myMap.put("Hallo", 1.0);
        //myMap.put("Welt", 1.2345);
        myMap.add("Hal");
        myMap.add("wel");

        //demo d2 + d3
        //if("agent0".equals(name)) {
            //IFuture<Boolean> future = bdiFeature.dispatchTopLevelGoal(new AdoptProductGoal(need));
            //boolean adopted = future.get();
            //logger.debug("product '{}' adopted? {}", need, adopted);
        //}
    }

    @OnEnd
    protected void killed() {
        logger.debug("killed: {}", name);
    }
}
