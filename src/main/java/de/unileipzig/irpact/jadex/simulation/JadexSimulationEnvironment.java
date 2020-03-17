package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import jadex.bridge.IExternalAccess;

/**
 * @author Daniel Abitz
 */
public interface JadexSimulationEnvironment extends SimulationEnvironment {

    IExternalAccess getPlatform();

    @Override
    JadexMessageSystem getMessageSystem();

    @Override
    JadexSimulationCache getCache();
}
