package de.unileipzig.irpact.v2.jadex.agents.simulation;

import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface SimulationService {

    JadexSimulationEnvironment getEnvironment();
}
