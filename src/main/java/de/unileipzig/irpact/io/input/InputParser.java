package de.unileipzig.irpact.io.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface InputParser {

    SimulationEnvironment parse(ObjectNode root) throws Exception;
}
