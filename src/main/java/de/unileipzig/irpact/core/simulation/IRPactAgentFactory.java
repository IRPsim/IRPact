package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.simulation.IRPactAgentAPI;

import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public interface IRPactAgentFactory extends Supplier<IRPactAgentAPI> {
}
