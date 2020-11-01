package de.unileipzig.irpact.v2.jadex.simulation;

import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.v2.jadex.time.JadexTimeModel;

/**
 * @author Daniel Abitz
 */
public interface JadexSimulationEnvironment extends SimulationEnvironment {

    @Override
    JadexTimeModel getTimeModel();


}
