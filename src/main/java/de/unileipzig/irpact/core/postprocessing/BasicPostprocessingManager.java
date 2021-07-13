package de.unileipzig.irpact.core.postprocessing;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.DataProcessor;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicPostprocessingManager extends PostProcessor implements PostprocessingManager {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicPostprocessingManager.class);

    public BasicPostprocessingManager(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            InRoot inRoot,
            SimulationEnvironment environment) {
        super(metaData, clOptions, inRoot, environment);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void execute() {
        trace("running data");
        new DataProcessor(metaData, clOptions, inRoot, environment).execute();
        trace("running image");
        new ImageProcessor(metaData, clOptions, inRoot, environment).execute();
    }
}
