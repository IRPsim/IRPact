package de.unileipzig.irpact.jadex.examples.experimental.java11test.planTest;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

@Agent(type = BDIAgentFactory.TYPE)
@Plans(
        @Plan(body = @Body(TestPlan.class))
)
public class ExternalPlanAgentBDI {

    @AgentFeature
    protected IBDIAgentFeature bdiFeature;

    @OnStart
    public void onStart() {
        bdiFeature.adoptPlan(new TestPlan());
    }
}
