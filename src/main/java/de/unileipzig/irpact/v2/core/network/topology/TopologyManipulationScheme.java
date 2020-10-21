package de.unileipzig.irpact.v2.core.network.topology;

import de.unileipzig.irpact.v2.core.misc.Scheme;
import de.unileipzig.irpact.v2.core.network.SocialGraph;
import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface TopologyManipulationScheme extends Scheme {

    void manipulate(SimulationEnvironment environment, SocialGraph graph);
}
