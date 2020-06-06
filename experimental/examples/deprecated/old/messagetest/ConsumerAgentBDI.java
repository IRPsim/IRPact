package de.unileipzig.irpact.jadex.examples.deprecated.old.messagetest;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.IComponentIdentifier;
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
    @AgentFeature
    protected IMessageFeature msgFeature;

    //args
    protected String name;
    protected Logger logger;
    protected Set<IComponentIdentifier> ids;
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
    //Multigoal
    //=========================

    @Goal
    public class MultiGoal {

        @GoalParameter
        protected String para0;
        @GoalParameter
        protected double para1;
        @GoalParameter
        protected long para2;
        @GoalParameter
        protected boolean para3;
        @GoalParameter
        protected String para4;

        public MultiGoal(String para0, double para1, long para2, boolean para3, String para4) {
            this.para0 = para0;
            this.para1 = para1;
            this.para2 = para2;
            this.para3 = para3;
            this.para4 = para4;
        }
    }

    @Plan(trigger = @Trigger(goals = MultiGoal.class))
    protected void handleMultiGoal(String para0, double para1, long para2, boolean para3, String para4) {
        logger.debug("[{}] BIGBIG {} {} {} {} {}", name, para0, para1, para2, para3, para4);
    }

    public static class HelperContainer {

        protected String para0;
        protected double para1;
        protected long para2;
        protected boolean para3;
        protected String para4;

        public HelperContainer(String para0, double para1, long para2, boolean para3, String para4) {
            this.para0 = para0;
            this.para1 = para1;
            this.para2 = para2;
            this.para3 = para3;
            this.para4 = para4;
        }

        @Override
        public String toString() {
            return "HelperContainer{" +
                    "para0='" + para0 + '\'' +
                    ", para1=" + para1 +
                    ", para2=" + para2 +
                    ", para3=" + para3 +
                    ", para4='" + para4 + '\'' +
                    '}';
        }
    }

    @Goal
    public class MultiGoal2 {

        @GoalParameter
        protected HelperContainer container;

        public MultiGoal2(HelperContainer container) {
            this.container = container;
        }
    }

    @Plan(trigger = @Trigger(goals = MultiGoal2.class))
    protected void handleMultiGoal2(HelperContainer container) {
        logger.debug("[{}] BIGBIG2 {}", name, container);
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
        ids = (Set<IComponentIdentifier>) args.get("ids");
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("created: {}", name);
        logger.debug("[{}] INFO {} {}", name, getClass(), agent.getClass());
    }

    protected IComponentIdentifier getId(boolean my) {
        for(IComponentIdentifier id: ids) {
            if(my && id == agent.getId()) {
                return id;
            }
            if(!my && id != agent.getId()) {
                return id;
            }
        }
        return null;
    }

    @OnStart
    protected void body() {
        logger.debug("body: {}", name);

        String need = (String) resultsFeature.getArguments().get("need");
        needs.add(need);

        msgFeature.addMessageHandler(new IMessageHandler() {
            @Override
            public boolean isHandling(ISecurityInfo secinfos, IMsgHeader header, Object msg) {
                logger.debug("[{}] msg from '{}' to '{}'", name, header.getSender().getName(), header.getReceiver().getName());
                return true;
            }

            @Override
            public boolean isRemove() {
                return false;
            }

            @Override
            public void handleMessage(ISecurityInfo secinfos, IMsgHeader header, Object msg) {
                logger.debug("type: {}, msg: {}", msg.getClass(), msg);
            }
        });

        execFeature.waitForDelay(2000, ia -> {
            IComponentIdentifier myid = getId(true);
            IComponentIdentifier other = getId(false);
            logger.debug("[{}] {} {}", name, myid.getName(), other.getName());
            return IFuture.DONE;
        });

        if("agent0".equals(name)) {
            execFeature.waitForDelay(6000, ia -> {
                msgFeature.sendMessage("Hallo Welt", getId(true), getId(false));
                return IFuture.DONE;
            });
        }

        execFeature.waitForDelay(11000, is -> {
            bdiFeature.dispatchTopLevelGoal(new MultiGoal("hallo", 1.5, 234, false, "welt"));
            bdiFeature.dispatchTopLevelGoal(new MultiGoal2(new HelperContainer("hallo", 1.5, 234, false, "welt")));
            return IFuture.DONE;
        });
    }

    @OnEnd
    protected void killed() {
        logger.debug("killed: {}", name);
        logger.debug("needs: {}, products: {}", needs, products);
    }
}
