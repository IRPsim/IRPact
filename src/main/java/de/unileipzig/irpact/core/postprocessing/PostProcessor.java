package de.unileipzig.irpact.core.postprocessing;

import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public abstract class PostProcessor implements LoggingHelper {

    protected MetaData metaData;
    protected MainCommandLineOptions clOptions;
    protected InRoot inRoot;
    protected SimulationEnvironment environment;

    public PostProcessor(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            InRoot inRoot,
            SimulationEnvironment environment) {
        this.metaData = metaData;
        this.clOptions = clOptions;
        this.inRoot = inRoot;
        this.environment = environment;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.RESULT;
    }

    public abstract void execute();

    protected Settings getSettings() {
        return environment.getSettings();
    }

    public Path getTargetDir() throws IOException {
        return clOptions.getCreatedDownloadDir();
    }

    protected List<Integer> years;
    public List<Integer> getAllSimulationYears() {
        if(years == null) {
            int firstYear = metaData.getOldestRunInfo().getActualFirstSimulationYear();
            int lastYear = metaData.getCurrentRunInfo().getLastSimulationYear();
            years = IntStream.rangeClosed(firstYear, lastYear)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return years;
    }
}
