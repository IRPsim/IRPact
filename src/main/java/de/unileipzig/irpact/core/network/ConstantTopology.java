package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
@ToDo("topologien eigenes package, da sonst zu voll")
public class ConstantTopology implements TopologyManipulationScheme {

    public static final String NAME = "ConstantTopologyScheme";
    public static final ConstantTopology INSTANCE = new ConstantTopology();

    @Override
    public void apply(SimulationEnvironment environment, SocialGraph graph) {
    }
}
