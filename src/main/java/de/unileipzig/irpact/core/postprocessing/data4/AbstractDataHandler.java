package de.unileipzig.irpact.core.postprocessing.data4;

import de.unileipzig.irpact.commons.util.data.DataStore;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data3.FileType;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.io.param.input.postdata.InPostDataAnalysis;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDataHandler<I extends InPostDataAnalysis> implements DataHandler, LoggingHelper {

    protected DataProcessor4 processor;
    protected I dataConfiguration;

    public AbstractDataHandler(
            DataProcessor4 processor,
            I dataConfiguration) {
        this.processor = processor;
        this.dataConfiguration = dataConfiguration;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    public Path getTargetDir() throws UncheckedIOException {
        try {
            return processor.getTargetDir();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected Path getTargetFile(String name) {
        return getTargetDir().resolve(name);
    }

    protected SimulationEnvironment getEnvironment() {
        return processor.getEnvironment();
    }

    protected DataStore getGlobalData() {
        return getEnvironment().getGlobalData();
    }

    protected abstract String getResourceKey();

    protected String getCsvDelimiter(FileType type) {
        return getLocalizedString(type, "sep");
    }

    protected String[] buildKey(FileType type, String key) {
        return new String[] {type.name(), getResourceKey(), key};
    }

    protected String getLocalizedString(FileType type, String key) {
        return processor.getLocalizedData().getString(buildKey(type, key));
    }

    protected String getLocalizedFormattedString(FileType type, String key, Object... args) {
        return processor.getLocalizedData().getFormattedString(buildKey(type, key), args);
    }
}
