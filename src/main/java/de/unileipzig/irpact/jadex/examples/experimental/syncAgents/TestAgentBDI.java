package de.unileipzig.irpact.jadex.examples.experimental.syncAgents;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices(
        @ProvidedService(type = TestService.class)
)
@Agent(type = BDIAgentFactory.TYPE)
public class TestAgentBDI implements TestService {

    @Agent
    protected IInternalAccess agent;

    @Belief
    protected Set<String> needs = new HashSet<>();

    @OnStart
    public void onStart() {
    }

    @Override
    public void prepare() {
        /*
        System.out.println("??? " + Thread.currentThread());
        agent.getFeature(IExecutionFeature.class)
                .suspendComponent();
        */
    }

    @Override
    public void setup() {
        /*
        agent.scheduleStep(ia -> {
            needs.add("NeedNummer42");
            needs.add("NeedNummer1337");
            return IFuture.DONE;
        });
         */
        agent.getFeature(IExecutionFeature.class).waitForTick(ia -> {
            needs.add("NeedNummer42");
            needs.add("NeedNummer1337");
            return IFuture.DONE;
        });
    }

    @Override
    public void start() {
        /*
        agent.getFeature(IExecutionFeature.class)
                .resumeComponent();
        */
    }

    @Goal
    public class TestGoal {

        protected String data;

        @GoalCreationCondition(factadded = "needs")
        public TestGoal(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    @Plan(trigger = @Trigger(goals = TestGoal.class))
    protected void handleTestGoal(TestGoal goal) {
        System.out.println(agent.getId().getLocalName() + " | handleTestGoal: " + goal.getData());
    }
}
