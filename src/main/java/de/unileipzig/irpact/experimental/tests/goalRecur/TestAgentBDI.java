package de.unileipzig.irpact.experimental.tests.goalRecur;

import de.unileipzig.irpact.jadex.util.JadexUtil;
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
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class TestAgentBDI {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TestAgentBDI.class);

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;

    protected IClockService clock;

    protected String name;
    protected CountDownLatch latch;
    protected int goalCounter = -1;

    //=========================
    //util
    //=========================

    protected void initData(Map<String, Object> input) {
        name = (String) input.get("NAME");
        latch = (CountDownLatch) input.get("LATCH");
        clock = JadexUtil.getClockService(agent);
    }

    //=========================
    //live
    //=========================

    public TestAgentBDI() {
    }

    @OnInit
    public void onInit() {
        initData(resultsFeature.getArguments());
        LOGGER.trace("[{}] init: {}", name, clock.getTick());
    }

    @OnStart
    public void onStart() {
        LOGGER.trace("[{}] start: {}", name, clock.getTick());
        if(name.equals("Agent#0")) {
            execFeature.waitForDelay(0, ia -> {
                bdiFeature.dispatchTopLevelGoal(new SimpleGoal(0));
                return IFuture.DONE;
            });
        } else {
            triggerRecursion();
        }
        latch.countDown();
    }

    @OnEnd
    public void onEnd() {
        LOGGER.trace("[{}] goalCounter: {} | end: {}", name, goalCounter, clock.getTick());
    }

    protected void triggerRecursion() {
        execFeature.waitForTick(ia -> {
            LOGGER.trace("[{}] recusion {}", name,clock.getTick());
            triggerRecursion();
            return IFuture.DONE;
        });
    }

    //=========================
    //stuff
    //=========================

    @Goal(excludemode = ExcludeMode.WhenSucceeded, retrydelay = 1)
    public class SimpleGoal {

        protected int counter;
        protected boolean notFinished = true;

        public SimpleGoal(int counter) {
            this.counter = counter;
        }

        public int getCounter() {
            return counter;
        }

        public void updateCounter() {
            counter++;
        }

//        @GoalRecurCondition(beliefs = "simpleGoalRecurTrigger")
        public boolean checkRecur() {
            return notFinished;
        }
    }

    @Plan(trigger = @Trigger(goals = SimpleGoal.class))
    protected void handleSimpleGoal(SimpleGoal goal) {
        LOGGER.trace("[{},{}] goal: {}", name, clock.getTick(), goal.getCounter());
        if(goal.getCounter() == 3) {
            goal.notFinished = false;
        } else {
            goal.updateCounter();
            throw new PlanFailureException();
        }
        goalCounter = goal.getCounter();
    }
}
