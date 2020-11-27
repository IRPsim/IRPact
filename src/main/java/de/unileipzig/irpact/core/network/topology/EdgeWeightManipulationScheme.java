package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.core.misc.Scheme;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface EdgeWeightManipulationScheme extends Scheme {

    double weighEdge(SimulationEnvironment environment, SocialGraph.Node from, SocialGraph.Node to, SocialGraph.Type type);
}
