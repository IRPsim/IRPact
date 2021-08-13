package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.LifeCycleControl;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationAgent;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Reference;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.commons.future.IFuture;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface JadexLifeCycleControl extends LifeCycleControl {

    void registerSimulationAgentAccess(SimulationAgent agent, IInternalAccess access);

    void setPlatform(IExternalAccess platform);

    void setSimulationService(ISimulationService simulationService);

    ISimulationService getSimulationService();

    void setClockService(IClockService clockService);

    IClockService getClockService();

    @Override
    IFuture<Map<String, Object>> waitForTermination();

    void prepareTermination();

    @Override
    IFuture<Map<String, Object>> terminate();

    @Override
    IFuture<Map<String, Object>> terminateTimeout();

    @Override
    IFuture<Map<String, Object>> terminateWithError(Throwable t);
}
