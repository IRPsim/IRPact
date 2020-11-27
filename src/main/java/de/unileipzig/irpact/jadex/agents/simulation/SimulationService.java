package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface SimulationService {

    JadexSimulationEnvironment getEnvironment();
}
