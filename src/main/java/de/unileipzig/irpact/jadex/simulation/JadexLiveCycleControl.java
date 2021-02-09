package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.LiveCycleControl;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Reference;
import jadex.commons.future.IFuture;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface JadexLiveCycleControl extends LiveCycleControl {

    void registerSimulationAgentAccess(IInternalAccess agent);

    @Override
    IFuture<Map<String, Object>> terminate();

    @Override
    IFuture<Map<String, Object>> terminateWithError(Exception e);
}
