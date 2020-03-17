package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.annotation.ToImpl;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
@ToImpl("einfuegen aller Komponenten")
public class EgoistTopology implements GraphTopologyScheme {

    public static final String NAME = EgoistTopology.class.getSimpleName();
    public static final EgoistTopology INSTANCE = new EgoistTopology();

    @Override
    public void initalize(SimulationEnvironment environment, SocialNetwork graph) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addSubsequently(SimulationEnvironment environment, SocialNetwork graph, ConsumerAgent agent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSubsequently(SimulationEnvironment environment, SocialNetwork graph, ConsumerAgent agent) {
        throw new UnsupportedOperationException();
    }
}
