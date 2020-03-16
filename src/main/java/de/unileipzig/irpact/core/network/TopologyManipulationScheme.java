package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface TopologyManipulationScheme extends Scheme {

    void apply(
            SimulationEnvironment environment,
            SocialGraph graph
    );
}
