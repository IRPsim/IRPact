package de.unileipzig.irpact.jadex.simulation;

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
public class BasicJadexSimulationControl implements JadexSimulationControl {

    protected IInternalAccess controlAgent;
    protected CountDownLatch startSynchronizer;

    public BasicJadexSimulationControl() {
    }

    public void setNumberOfAgents(int count) {
        startSynchronizer = new CountDownLatch(count);
    }

    @Override
    public void registerControlAgent(IInternalAccess controlAgent) {
        this.controlAgent = controlAgent;
    }

    @Override
    public void reportAgentCreation() {
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
