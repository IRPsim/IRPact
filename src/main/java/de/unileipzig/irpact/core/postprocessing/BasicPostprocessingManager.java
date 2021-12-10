package de.unileipzig.irpact.core.postprocessing;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data4.DataProcessor4;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
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
        trace("running post analysis");
//        trace("running data");
//        new DataProcessor(metaData, clOptions, inRoot, environment).execute();
        trace("running data4");
        new DataProcessor4(metaData, clOptions, inRoot, environment).execute();
//        trace("running image");
//        new ImageProcessor(metaData, clOptions, inRoot, environment).execute();
        trace("running image2");
        new ImageProcessor2(metaData, clOptions, inRoot, environment).execute();
    }
}
