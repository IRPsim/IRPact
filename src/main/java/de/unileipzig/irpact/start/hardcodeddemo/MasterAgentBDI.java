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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class MasterAgentBDI {

    private static final Logger logger = LoggerFactory.getLogger(MasterAgentBDI.class);

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
            logger.debug("[MASTER] {} {} {}", createCount, currentKillCount, totalNumberOfAgents);
            if(currentKillCount == totalNumberOfAgents && ((System.currentTimeMillis() - lastAccess) > 1000)) {
                logger.debug("[MASTER] kill platform");
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
        logger.debug("[MASTER] onInit");
    }

    @OnStart
    protected void onStart() {
        logger.debug("[MASTER] onStart");
        runCycle();
    }

    @OnEnd
    protected void onEnd() {
        logger.debug("[MASTER] onEnd");
    }
}
