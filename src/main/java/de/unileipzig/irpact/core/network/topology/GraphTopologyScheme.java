package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface GraphTopologyScheme extends Scheme {

    void initalize(SimulationEnvironment environment, SocialGraph graph);

    void addSubsequently(SimulationEnvironment environment, SocialGraph graph, ConsumerAgent agent);

    void removeSubsequently(SimulationEnvironment environment, SocialGraph graph, ConsumerAgent agent);
}
