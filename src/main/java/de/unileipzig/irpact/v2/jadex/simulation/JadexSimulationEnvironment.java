package de.unileipzig.irpact.v2.jadex.simulation;

import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.v2.jadex.time.JadexTimeModel;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface JadexSimulationEnvironment extends SimulationEnvironment {

    @Override
    JadexTimeModel getTimeModel();

    @Override
    JadexSimulationControl getSimulationControl();
}
