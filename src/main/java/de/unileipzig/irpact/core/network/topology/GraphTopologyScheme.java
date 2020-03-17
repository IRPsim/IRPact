package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface GraphTopologyScheme extends Scheme {

    void initalize(SimulationEnvironment environment, SocialNetwork graph);

    void addSubsequently(SimulationEnvironment environment, SocialNetwork graph, ConsumerAgent agent);

    void removeSubsequently(SimulationEnvironment environment, SocialNetwork graph, ConsumerAgent agent);
}
