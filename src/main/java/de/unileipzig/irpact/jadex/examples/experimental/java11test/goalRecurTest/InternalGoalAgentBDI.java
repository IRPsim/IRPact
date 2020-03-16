package de.unileipzig.irpact.jadex.examples.experimental.java11test.goalRecurTest;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.model.MProcessableElement;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.util.HashMap;
import java.util.Map;

@Agent(type = BDIAgentFactory.TYPE)
public class InternalGoalAgentBDI {

    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;

    @Belief
    protected Map<String, String> db = new HashMap<>();

    @OnInit
    public void onInit() {
        db.put("a", "a0");
    }

    @OnStart
    public void onStart() {
        execFeature.waitForDelay(4000, ia -> {
            db.put("b", "b0");
            return IFuture.DONE;
        });

        execFeature.waitForDelay(7000, ia -> {
            db.put("c", "c0");
            return IFuture.DONE;
        });

        bdiFeature.dispatchTopLevelGoal(new InternalTestGoal("a"));
        bdiFeature.dispatchTopLevelGoal(new InternalTestGoal("b"));
        bdiFeature.dispatchTopLevelGoal(new InternalTestGoal("c"));

        /*
        System.out.println("before a");
        String ret = (String) bdiFeature.dispatchTopLevelGoal(new InternalTestGoal("a"))
                .get();
        System.out.println("after a: " + ret);
        */
        /*
        bdiFeature.dispatchTopLevelGoal(new InternalTestGoal("b")).get();
        System.out.println("after b");
        bdiFeature.dispatchTopLevelGoal(new InternalTestGoal("c")).get();
        System.out.println("after c");
        */
    }

    @Goal(recur = true, retry = false)
    public class InternalTestGoal {

        protected String data;
        protected String result;

        public InternalTestGoal(String data) {
            this.data = data;
        }

        @GoalTargetCondition(beliefs = "db")
        public boolean checkTarget() {
            System.out.println(System.currentTimeMillis() + " " + " XXX " + data);
            return result != null;
        }

        @GoalRecurCondition(rawevents = @RawEvent(value = ChangeEvent.FACTADDED, second = "db"))
        public boolean checkRecur() {
            System.out.println(System.currentTimeMillis() + " " + " YYY " + data);
            return result == null;
        }
    }

    @Plan(trigger = @Trigger(goals = InternalTestGoal.class))
    protected IFuture<Void> handleTestGoal(InternalTestGoal goal) {
        Future<Void> ret = new Future<>();
        if(!db.containsKey(goal.data)) {
            ret.setException(new PlanFailureException());;
        } else {
            System.out.println("> '" + goal.data + "' '" + db.get(goal.data) + "'");
            goal.result = db.get(goal.data);
            ret.setResult(null);
        }
        //System.out.println(System.currentTimeMillis() + " ZZZ " + " -> " + goal.data);
        return ret;
    }

    /*
    @Plan(trigger = @Trigger(goals = InternalTestGoal.class))
    protected String handleTestGoal(String input) {
        if(!db.containsKey(input)) {
            throw new PlanFailureException();
        } else {
            System.out.println("> '" + input + "' '" + db.get(input) + "'");
            return db.get(input);
        }
    }
    */
}
