package de.unileipzig.irpact.core.persistence;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;

/**
 * @author Daniel Abitz
 */
public interface PersistenceModul {

    void store(
            SimulationEnvironment environment,
            OutRoot root) throws Exception;

    SimulationEnvironment restore(
            MainCommandLineOptions options,
            InRoot root) throws Exception;
}
