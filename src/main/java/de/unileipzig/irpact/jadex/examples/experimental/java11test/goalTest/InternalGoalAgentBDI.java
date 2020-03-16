package de.unileipzig.irpact.jadex.examples.experimental.java11test.goalTest;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.util.HashMap;
import java.util.Map;

@Agent(type = BDIAgentFactory.TYPE)
public class InternalGoalAgentBDI {

    @AgentFeature
    protected IBDIAgentFeature bdiFeature;

    @Belief
    protected Map<String, String> db = new HashMap<>();

    @OnInit
    public void onInit() {
        db.put("a", "a0");
    }

    @OnStart
    public void onStart() {
        bdiFeature.dispatchTopLevelGoal(new InternalTestGoal("a"));
    }

    @Goal
    public class InternalTestGoal {

        private String data;

        public InternalTestGoal(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    @Plan(trigger = @Trigger(goals = InternalTestGoal.class))
    protected void handleTestGoal(InternalTestGoal goal) {
        System.out.println("> '" + goal.getData() + "' '" + db.get(goal.getData()) + "'");
    }
}
