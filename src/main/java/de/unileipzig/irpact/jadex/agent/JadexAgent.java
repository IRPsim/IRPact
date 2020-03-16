package de.unileipzig.irpact.jadex.agent;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface JadexAgent extends Agent {

    @Override
    JadexSimulationEnvironment getEnvironment();
}
