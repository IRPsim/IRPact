package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.dev.ToImpl;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
@ToImpl("einfuegen aller Komponenten")
public class EgoistTopology implements GraphTopologyScheme {

    public static final String NAME = EgoistTopology.class.getSimpleName();
    public static final EgoistTopology INSTANCE = new EgoistTopology();

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addSubsequently(SimulationEnvironment environment, SocialGraph graph, ConsumerAgent agent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSubsequently(SimulationEnvironment environment, SocialGraph graph, ConsumerAgent agent) {
        throw new UnsupportedOperationException();
    }
}
