package de.unileipzig.irpact.core.postprocessing;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.InputStream;
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

    protected ObjectNode tryLoadYaml(String baseName, String extension) throws IOException {
        ObjectNode root = tryLoadExternalYaml(baseName, extension);
        if(root != null) return root;
        return tryLoadInternalYaml(baseName, extension);
    }

    protected ObjectNode tryLoadExternalYaml(String baseName, String extension) throws IOException {
        if(metaData.getLoader().hasLocalizedExternal(baseName, metaData.getLocale(), extension)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedExternal(baseName, metaData.getLocale(), extension));
            InputStream in = metaData.getLoader().getLocalizedExternalAsStream(baseName, metaData.getLocale(), extension);
            return tryLoadYamlAndCloseStream(in);
        } else {
            return null;
        }
    }

    protected ObjectNode tryLoadInternalYaml(String baseName, String extension) throws IOException {
        if(metaData.getLoader().hasLocalizedInternal(baseName, metaData.getLocale(), extension)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedInternal(baseName, metaData.getLocale(), extension));
            InputStream in = metaData.getLoader().getLocalizedInternalAsStream(baseName, metaData.getLocale(), extension);
            return tryLoadYamlAndCloseStream(in);
        } else {
            return null;
        }
    }

    protected ObjectNode tryLoadYamlAndCloseStream(InputStream in) throws IOException {
        if(in == null) {
            return null;
        }
        try {
            return JsonUtil.read(in, JsonUtil.YAML);
        } finally {
            in.close();
        }
    }
}
