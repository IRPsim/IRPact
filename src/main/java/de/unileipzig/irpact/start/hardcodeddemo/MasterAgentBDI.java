package de.unileipzig.irpact.start.hardcodeddemo;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class MasterAgentBDI {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;

    //=========================
    //lifecycle
    //=========================

    private int totalNumberOfAgents;

    private static long lastAccess = System.currentTimeMillis();

    public static synchronized void updateAccess() {
        lastAccess = System.currentTimeMillis();
    }

    private static int createCount = 0;

    public static synchronized void incCreated() {
        updateAccess();
        createCount++;
    }

    private static int killCount = 0;

    public static synchronized void incKilled() {
        updateAccess();
        killCount++;
    }

    protected void runCycle() {
        execFeature.waitForDelay(1000, _internalAccess -> {
            int currentKillCount = killCount;
            System.out.println("[MASTER] " + createCount + " " + currentKillCount + " " + totalNumberOfAgents);
            if(currentKillCount == totalNumberOfAgents && ((System.currentTimeMillis() - lastAccess) > 1000)) {
                System.out.println("[MASTER] kill platform");
                IComponentIdentifier platformId = _internalAccess.getId().getRoot();
                IExternalAccess platform = _internalAccess.getExternalAccess(platformId);
                platform.killComponent();
            } else {
                runCycle();
            }
            return IFuture.DONE;
        });
    }

    @OnInit
    protected void onInit() {
        totalNumberOfAgents = (int) resultsFeature.getArguments().get("totalNumberOfAgents");
        System.out.println("[MASTER] onInit");
    }

    @OnStart
    protected void onStart() {
        System.out.println("[MASTER] onStart");
        runCycle();
    }

    @OnEnd
    protected void onEnd() {
        System.out.println("[MASTER] onEnd");
    }
}
