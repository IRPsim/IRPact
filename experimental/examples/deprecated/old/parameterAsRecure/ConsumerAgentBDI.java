package de.unileipzig.irpact.jadex.examples.deprecated.old.parameterAsRecure;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.impl.PlanFailureException;
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
    protected Set<String> needs = new HashSet<>();
    protected Set<String> products;
    @Belief
    protected int fireDummyParameter = 0; //hmm, mehr hack als schoen

    //=========================
    //Goal
    //=========================

    @Goal(recur = true)
    public class AdoptProductGoal {

        @GoalParameter
        protected String product;

        @GoalCreationCondition(factadded = "needs")
        public AdoptProductGoal(String product) {
            this.product = product;
        }

        @GoalRecurCondition(beliefs = "fireDummyParameter")
        public boolean checkRecur() {
            return true;
        }
    }

    //=========================
    //Plans
    //=========================

    @Plan(trigger = @Trigger(goals = AdoptProductGoal.class))
    protected void adopt(String product) {
        boolean adopted = products.contains(product);
        logger.debug("ADOPTED: {} {}", product, adopted);
        if(adopted) {
            needs.remove(product);
            logger.debug("removed: {}", product);
        } else {
            throw new PlanFailureException();
        }
    }

    public void fireRecure(Object caller) {
        if(this == caller) {
            fireDummyParameter = fireDummyParameter + 1;
        } else {
            agent.scheduleStep(cs -> {
                fireRecure(this);
                return IFuture.DONE;
            });
        }
    }

    //=========================
    //Agent
    //=========================

    public ConsumerAgentBDI() {
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

        String need = (String) resultsFeature.getArguments().get("need");
        needs.add(need);


        //demo d2 + d3
        //if("agent0".equals(name)) {
            //IFuture<Boolean> future = bdiFeature.dispatchTopLevelGoal(new AdoptProductGoal(need));
            //boolean adopted = future.get();
            //logger.debug("product '{}' adopted? {}", need, adopted);
        //}
        execFeature.waitForDelay(7000, cs -> {
            logger.debug("add haus");
            products.add("haus");
            logger.debug("haus added");
            return IFuture.DONE;
        });

        /*
        execFeature.waitForDelay(14000, cs -> {
            logger.debug("fire...");
            fireRecure();
            logger.debug("... done!");
            return IFuture.DONE;
        });
         */
        //externer thread
        execFeature.waitForDelay(14000, cs -> {
            ConcurrentUtil.start(0, "EXTERN_THREAD", () -> {
                logger.debug("fire...");
                fireRecure("asd");
                logger.debug("... done!");
            });
            return IFuture.DONE;
        });
    }

    @OnEnd
    protected void killed() {
        logger.debug("killed: {}", name);
        logger.debug("needs: {}, products: {}", needs, products);
    }
}
