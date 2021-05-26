package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface IRPactInputParser extends InputParser {

    int getSimulationYear();

    ResourceLoader getResourceLoader();

    Rnd deriveRnd();

    SimulationEnvironment getEnvironment();

    boolean isRestored();

    InRoot getRoot();

    void initLoggingOnly(InRoot root) throws ParsingException;
}
