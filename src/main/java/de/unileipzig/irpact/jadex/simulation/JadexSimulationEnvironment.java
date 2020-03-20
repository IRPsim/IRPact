package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.annotation.Experimental;
import de.unileipzig.irpact.core.simulation.EventManager;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import jadex.bridge.IExternalAccess;
import jadex.bridge.component.IExternalExecutionFeature;

/**
 * @author Daniel Abitz
 */
public interface JadexSimulationEnvironment extends SimulationEnvironment {

    IExternalAccess getPlatform();

    IExternalExecutionFeature getPlatformExec();

    @Override
    JadexMessageSystem getMessageSystem();

    @Override
    JadexSimulationConfiguration getConfiguration();

    @Override
    JadexEventManager getEventManager();

    //=========================
    //util
    //=========================

    @Experimental
    void poke();
}
