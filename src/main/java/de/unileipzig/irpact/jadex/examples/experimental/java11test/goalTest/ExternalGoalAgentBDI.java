package de.unileipzig.irpact.jadex.examples.experimental.java11test.goalTest;

import de.unileipzig.irpact.jadex.examples.experimental.java11test.goalTest.goal.TestGoal;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.util.HashMap;
import java.util.Map;

@Agent(type = BDIAgentFactory.TYPE)
public class ExternalGoalAgentBDI {

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
        bdiFeature.dispatchTopLevelGoal(new TestGoal("a"));
    }


    @Plan(trigger = @Trigger(goals = TestGoal.class))
    protected void handleTestGoal(TestGoal goal) {
        System.out.println("> '" + goal.getData() + "' '" + db.get(goal.getData()) + "'");
    }
}
