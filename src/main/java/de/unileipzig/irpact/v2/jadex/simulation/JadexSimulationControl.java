package de.unileipzig.irpact.v2.jadex.simulation;

import de.unileipzig.irpact.v2.core.simulation.SimulationControl;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Reference;
import jadex.commons.future.IFuture;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface JadexSimulationControl extends SimulationControl {

    void registerControlAgent(IInternalAccess agent);

    @Override
    IFuture<Map<String, Object>> terminate();

    @Override
    IFuture<Map<String, Object>> terminateWithError(Exception e);
}
