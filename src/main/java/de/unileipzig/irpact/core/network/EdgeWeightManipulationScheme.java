package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface EdgeWeightManipulationScheme extends Scheme {

    double getEdgeWeight(
            SimulationEnvironment environment,
            SocialGraph.Node source,
            SocialGraph.Node target,
            EdgeType edgeType
    );
}
