package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.network.EdgeType;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
@ToDo("gefaellt mir noch nicht")
public interface EdgeWeightManipulationScheme extends Scheme {

    double getEdgeWeight(
            SimulationEnvironment environment,
            SocialNetwork.Node source,
            SocialNetwork.Node target,
            EdgeType edgeType
    );

    /*
    void updateWeight(
            SimulationEnvironment environment,
            SocialGraph graph,
            SocialGraph.Node source,
            SocialGraph.Node target,
            EdgeType type
    );

    void updateWeight(
            SimulationEnvironment environment,
            SocialGraph graph,
            SocialGraph.Edge edge,
            EdgeType type
    );
    */
}
