package de.unileipzig.irpact.experimental.tests.syncStart;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class TestAgentBDI {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    private String name;

    @Belief
    protected Set<String> dataSet = new HashSet<>();

    public TestAgentBDI() {
    }

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    private boolean is(int id) {
        return name.endsWith("#" + id);
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        log("onInit");
    }

    @OnStart
    protected void onStart() {
        log("onStart");
        execFeature.waitForDelay(0, ia -> {
            if(is(0)) {
                log("TEST");
            }
//            if(is(0)) {
//                dataSet.addAll(CollectionUtil.arrayListOf("a0", "b0"));
//            }
//            if(is(1)) {
//                dataSet.add("c1");
//            }
//            if(is(3)) {
//                dataSet.add("d3");
//            }
            return IFuture.DONE;
        });
    }

    @OnEnd
    protected void onEnd() {
        log("onEnd");
    }

    //=========================
    //BDI
    //=========================

    @Goal
    protected class HandleDataSetGoal {

        protected String data;

        @GoalCreationCondition(factadded = "dataSet")
        public HandleDataSetGoal(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    @Plan(trigger = @Trigger(goals = HandleDataSetGoal.class))
    protected void handleDataSet(HandleDataSetGoal goal) {
        String data = goal.getData();
        log("Data: " + data);
    }
}
