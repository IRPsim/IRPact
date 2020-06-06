package de.unileipzig.irpact.jadex.examples.experimental.java11test.planTest;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.util.HashMap;
import java.util.Map;

@Agent(type = BDIAgentFactory.TYPE)
@Plans(
        @Plan(body = @Body(InternalPlanAgentBDI.InternalTestPlan.class))
)
public class InternalPlanAgentBDI {

    @AgentFeature
    protected IBDIAgentFeature bdiFeature;

    @OnStart
    public void onStart() {
        bdiFeature.adoptPlan(new InternalTestPlan());
    }

    @Plan
    public class InternalTestPlan {

        protected Map<String, String> db = new HashMap<>();

        public InternalTestPlan() {
            db.put("a", "a0");
        }

        @PlanBody
        public void doStuff() {
            System.out.println("InternalTestPlan 'a' '" + db.get("a") + "'");
        }
    }
}
