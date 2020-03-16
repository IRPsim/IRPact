package de.unileipzig.irpact.jadex.examples.old.sharedData;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
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
import java.util.Random;

@Agent(type = BDIAgentFactory.TYPE)
public class CompanyAgentBDI {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature feature;
    @AgentFeature
    protected IExecutionFeature execFeature;

    //args
    protected String name;
    protected Logger logger;
    protected Store store;

    //=========================
    //Goal
    //=========================

    @Goal
    public class AddProductGoal {

        @GoalParameter
        protected String product;

        public AddProductGoal(String product) {
            this.product = product;
        }
    }

    //=========================
    //Plans
    //=========================

    @Plan(trigger = @Trigger(goals = AddProductGoal.class))
    protected void addProduct(String product) {
        logger.debug("{} add {}", sayMyName(), product);
        store.addProduct(product);
    }

    //=========================
    //Agent
    //=========================

    public CompanyAgentBDI() {
    }

    private String sayMyName() {
        return "[" + name + "]";
    }

    protected void initArgs(Map<String, Object> args) {
        name = (String) args.get("name");
        logger = (Logger) args.get("logger");
        store = (Store) args.get("store");

        logger.debug("{} my store: {}", sayMyName(), System.identityHashCode(store));
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("created: {}", name);
    }

    @OnStart
    protected void body() {
        logger.debug("body: {}", name);

        Random rnd = new Random();
        long wait1 = rnd.nextInt(2000) + 1000;
        execFeature.waitForDelay(wait1, ia -> {
            feature.dispatchTopLevelGoal(new AddProductGoal(name + "#" + wait1));
            return IFuture.DONE;
        });
        long wait2 = rnd.nextInt(2000) + wait1;
        execFeature.waitForDelay(wait2, ia -> {
            feature.dispatchTopLevelGoal(new AddProductGoal(name + "#" + wait2));
            return IFuture.DONE;
        });
    }

    @OnEnd
    protected void killed() {
        logger.debug("killed: {}", name);

        resultsFeature.getResults().put("store", store);
        resultsFeature.getResults().put("storehash", System.identityHashCode(store));
    }
}
