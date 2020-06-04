package de.unileipzig.irpact.start.hardcodeddemo;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.cms.CMSStatusEvent;
import jadex.bridge.service.types.cms.IComponentDescription;
import jadex.bridge.service.types.cms.SComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.IIntermediateResultListener;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class MasterAgentBDI {

    //Jadex parameter
    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;
    @AgentFeature
    protected IMessageFeature msgFeature;

    //=========================
    //lifecycle
    //=========================

    //Das hier in einen Service umwandeln.
    private static final Object timeLock = new Object();
    private static long lastAccess = System.currentTimeMillis();
    public static void updateAccess() {
        synchronized (timeLock) {
            lastAccess = System.currentTimeMillis();
        }
    }

    private final Object lock = new Object();
    private int createCount = 0;
    private int modifyCreateCount(int delta) {
        synchronized (lock) {
            createCount += delta;
            return createCount;
        }
    }

    private final Object killLock = new Object();
    private int killCount = 0;
    private int modifyKillCount(int delta) {
        synchronized (killLock) {
            killCount += delta;
            return killCount;
        }
    }

    protected void runCycle() {
        execFeature.waitForDelay(1000, _internalAccess -> {
            int currentKillCount = killCount;
            int currentCreateCount = createCount;

            System.out.println(currentCreateCount + " " + currentKillCount + " " + killCount);

            if(currentKillCount == totalNumberOfAgents && ((System.currentTimeMillis() - lastAccess) > 1000)) {
                IComponentIdentifier platformId = _internalAccess.getId().getRoot();
                IExternalAccess platform = _internalAccess.getExternalAccess(platformId);
                platform.killComponent();
                System.out.println("[MASTER] kill platform");
            } else {
                runCycle();
            }
            return IFuture.DONE;
        });
    }

    private boolean isValidAgent(IComponentDescription description) {
        //ModelName == full class name
        return description.getModelName().endsWith("AdaptionAgentBDI");
    }

    private int totalNumberOfAgents;

    @OnInit
    protected void onInit() {
        System.out.println("[MASTER] onInit");
        totalNumberOfAgents = (int) resultsFeature.getArguments().get("totalNumberOfAgents");

        SComponentManagementService.listenToAll(agent)
                .addResultListener(new IIntermediateResultListener<CMSStatusEvent>() {
                    @Override
                    public void intermediateResultAvailable(CMSStatusEvent result) {
                        if(isValidAgent(result.getComponentDescription())) {
                            int c = 0;
                            if("CMSCreatedEvent".equals(result.getType())) {
                                c = modifyCreateCount(1);
                            }
                            if("CMSTerminatedEvent".equals(result.getType())) {
                                c = modifyKillCount(1);
                            }
                            if(HardCodedAgentDemo.debug) System.out.println("[MASTER] count:" + c);
                        }
                    }

                    @Override
                    public void finished() {
                        System.out.println("+++ finished");
                    }

                    @Override
                    public void exceptionOccurred(Exception exception) {
                        System.out.println("--- exceptionOccurred");
                    }

                    @Override
                    public void resultAvailable(Collection<CMSStatusEvent> result) {
                        System.out.println("??? resultAvailable");
                    }
                });
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
