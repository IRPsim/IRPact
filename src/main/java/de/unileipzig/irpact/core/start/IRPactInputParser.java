package de.unileipzig.irpact.core.start;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.InRoot;

/**
 * @author Daniel Abitz
 */
public interface IRPactInputParser extends InputParser {

    //=========================
    //util
    //=========================

    int getSimulationYear();

    ResourceLoader getResourceLoader();

    Rnd deriveRnd();

    SimulationEnvironment getEnvironment();

    boolean isRestored();

    InRoot getRoot();

    void initLoggingOnly(InRoot root) throws ParsingException;

    //=========================
    //parse
    //=========================

    @Override
    SimulationEnvironment parseRoot(InRoot root) throws ParsingException;
}
