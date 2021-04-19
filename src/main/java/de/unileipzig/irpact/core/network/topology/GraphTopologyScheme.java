package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.misc.Scheme;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface GraphTopologyScheme extends Scheme {

    void initalize(SimulationEnvironment environment, SocialGraph graph) throws InitializationException;
}
