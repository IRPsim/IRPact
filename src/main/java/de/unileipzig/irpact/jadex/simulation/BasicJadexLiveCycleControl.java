package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Reference;
import jadex.commons.future.IFuture;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexLiveCycleControl implements JadexLiveCycleControl {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexLiveCycleControl.class);

    protected IInternalAccess controlAgent;
    protected CountDownLatch startSynchronizer;

    public BasicJadexLiveCycleControl() {
    }

    public void setTotalNumberOfAgents(int count) {
        startSynchronizer = new CountDownLatch(count);
    }

    @Override
    public void registerSimulationAgentAccess(IInternalAccess controlAgent) {
        this.controlAgent = controlAgent;
    }

    @Override
    public void reportAgentCreated(Agent agent) {
        startSynchronizer.countDown();
    }

    @Override
    public void waitForCreationFinished() throws InterruptedException {
        startSynchronizer.await();
    }

    protected IExternalAccess getPlatform() {
        IComponentIdentifier platformId = controlAgent.getId().getRoot();
        return controlAgent.getExternalAccess(platformId);
    }

    @Override
    public IFuture<Map<String, Object>> terminate() {
        return getPlatform().killComponent();
    }

    @Override
    public IFuture<Map<String, Object>> terminateWithError(Exception e) {
        return getPlatform().killComponent(e);
    }
}
