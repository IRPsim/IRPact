package de.unileipzig.irpact.jadex.examples.experimental.schedule;

import de.unileipzig.irpact.jadex.util.JadexUtil;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class TestAgentBDI {

    protected IClockService clockService;
    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IExecutionFeature execFeature;

    @Belief
    protected Set<String> needs = new HashSet<>();

    @OnInit
    public void onInit() {
        clockService = agent.searchService(new ServiceQuery<>(IClockService.class)).get();
    }

    @OnStart
    public void onStart() {
        System.out.println(clockService == null);
        /*
        agent.getFeature(IExecutionFeature.class).waitForTick(ia -> {
            needs.add("NeedNummer42");
            needs.add("NeedNummer1337");
            return IFuture.DONE;
        });
        */
        System.out.println("Tick1: " + clockService.getTick());
        execFeature.waitForTick(ia -> {
            System.out.println("Tick2: " + clockService.getTick());
            return IFuture.DONE;
        });
        JadexUtil.waitForTick(execFeature, 1, ia -> {
            System.out.println("Tick3: " + clockService.getTick());
            return IFuture.DONE;
        });
        JadexUtil.waitForTick(execFeature, 10, ia -> {
            System.out.println("Tick4: " + clockService.getTick());
            return IFuture.DONE;
        });

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
