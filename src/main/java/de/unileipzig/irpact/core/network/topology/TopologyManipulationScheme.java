package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.core.misc.Scheme;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface TopologyManipulationScheme extends Scheme {

    void manipulate(SimulationEnvironment environment, SocialGraph graph);
}
