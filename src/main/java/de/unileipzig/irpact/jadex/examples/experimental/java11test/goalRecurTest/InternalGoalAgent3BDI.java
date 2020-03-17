package de.unileipzig.irpact.jadex.examples.experimental.java11test.goalRecurTest;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//primitive parameter als belief
@Agent(type = BDIAgentFactory.TYPE)
public class InternalGoalAgent3BDI {

    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;

    @Belief
    protected Set<String> needs = new HashSet<>();
    @Belief
    protected int handleNeedRecurTrigger = 0;
    //kein belief!
    protected Map<String, String> db = new HashMap<>();

    @OnInit
    public void onInit() {
        db.put("a", "a0");
    }

    @OnStart
    public void onStart() {
        execFeature.waitForDelay(4000, ia -> {
            db.put("b", "b0");
            handleNeedRecurTrigger++;
            return IFuture.DONE;
        });

        execFeature.waitForDelay(7000, ia -> {
            db.put("c", "c0");
            handleNeedRecurTrigger++;
            return IFuture.DONE;
        });

        execFeature.waitForDelay(9000, ia -> {
            needs.remove("a");
            needs.add("a");
            return IFuture.DONE;
        });

        needs.add("a");
        needs.add("b");
        needs.add("c");
    }

    //retry false, da wir recur wollen - sonst spamm
    @Goal(recur = true, retry = false)
    public class InternalTestGoal {

        protected String data;
        protected boolean succeeded = false;

        @GoalCreationCondition(factadded = "needs")
        public InternalTestGoal(String data) {
            this.data = data;
        }

        //hilfsvariable, um das goal zu entfernen, wenn es erfolgreich war - wird entfernt,
        //wenn ich einen besseren weg finde
        public void setSucceeded() {
            succeeded = true;
        }

        //checkTarget entfernt recur goal, sonst wird es immer wieder aufgerufen
        @GoalTargetCondition(beliefs = "handleNeedRecurTrigger")
        public boolean checkTarget() {
            System.out.println(System.currentTimeMillis() + " TARGET " + data);
            return succeeded;
        }

        @GoalRecurCondition(beliefs = "handleNeedRecurTrigger")
        public boolean checkRecur() {
            System.out.println(System.currentTimeMillis() + " RECUR " + data);
            return !succeeded;
        }
    }

    @Plan(trigger = @Trigger(goals = InternalTestGoal.class))
    protected void handleTestGoal(InternalTestGoal goal) {
        Future<Void> ret = new Future<>();
        System.out.println(System.currentTimeMillis() + " PLAN " + goal.data + " " + goal.succeeded);
        if(!db.containsKey(goal.data)) {
            throw new PlanFailureException();
        } else {
            System.out.println("> '" + goal.data + "' '" + db.get(goal.data) + "'");
            goal.setSucceeded();
            ret.setResult(null);
        }
    }
}
