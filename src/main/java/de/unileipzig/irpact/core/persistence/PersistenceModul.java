package de.unileipzig.irpact.core.persistence;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.JadexRestoreUpdater;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;

/**
 * @author Daniel Abitz
 */
public interface PersistenceModul {

    void store(
            MetaData metaData,
            SimulationEnvironment environment,
            OutRoot root) throws Exception;

    SimulationEnvironment restore(
            MetaData metaData,
            MainCommandLineOptions options,
            int year,
            JadexRestoreUpdater updater,
            InRoot root) throws Exception;
}
