package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class ConstantTopology implements TopologyManipulationScheme {

    public static final String NAME = "ConstantTopologyScheme";
    public static final ConstantTopology INSTANCE = new ConstantTopology();

    @Override
    public void apply(SimulationEnvironment environment, SocialGraph graph) {
    }
}
