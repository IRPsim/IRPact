package de.unileipzig.irpact.v2.core.network.topology;

import de.unileipzig.irpact.v2.core.misc.Scheme;
import de.unileipzig.irpact.v2.core.network.SocialGraph;
import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface EdgeWeightManipulationScheme extends Scheme {

    double weighEdge(SimulationEnvironment environment, SocialGraph.Node from, SocialGraph.Node to, SocialGraph.Type type);
}
