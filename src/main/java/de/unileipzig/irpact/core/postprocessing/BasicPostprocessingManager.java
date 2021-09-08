package de.unileipzig.irpact.core.postprocessing;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.DataProcessor;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Path;

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
        runPostAnalysis();
        trace("running data");
        new DataProcessor(metaData, clOptions, inRoot, environment).execute();
        trace("running image");
        new ImageProcessor(metaData, clOptions, inRoot, environment).execute();
    }

    private void runPostAnalysis() {
        logNonAdopter();
        finishPostAnalysis();
    }

    private void logNonAdopter() {
        if(inRoot.getGeneral().isLogNonAdopterAnalysis()) {
            for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                for(ConsumerAgent ca: cag.getAgents()) {
                    if(ca.getAdoptedProducts().isEmpty()) {
                        environment.getPostAnalysisLogger().logNonAdopter(ca);
                    }
                }
            }
        }
    }

    private void finishPostAnalysis() {
        try {
            Path zipPath = clOptions.getCreatedDownloadDir().resolve(IRPact.ANALYSIS_ZIP);
            environment.getPostAnalysisLogger().finish(zipPath);
        } catch (IOException e) {
            LOGGER.error("'finishPostAnalysis' failed", e);
        }
    }
}
