package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.color.ColorPalette;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.data.DataStore;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractImageHandler<I extends InOutputImage2> implements ImageHandler, LoggingHelper {

    protected ImageProcessor2 processor;
    protected I imageConfiguration;

    public AbstractImageHandler(
            ImageProcessor2 processor,
            I imageConfiguration) {
        this.processor = processor;
        this.imageConfiguration = imageConfiguration;
    }

    public Path getTargetDir() throws UncheckedIOException {
        try {
            return processor.getTargetDir();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected ColorPalette getPaletteOrNull() {
        try {
            return imageConfiguration.hasColorPalette()
                    ? imageConfiguration.getColorPalette().toPalette()
                    : null;
        } catch (ParsingException e) {
            error("color platte not usable, use default colors", e);
            return null;
        }
    }

    protected List<String> getHexRGBPaletteOrNull() {
        ColorPalette palette = getPaletteOrNull();
        return palette == null
                ? null
                : palette.stream()
                .map(ColorPalette::printRGBHex)
                .collect(Collectors.toList());
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

    protected boolean hasValidScale() {
        return getEnvironment().getAgents().getInitialAgentPopulation().hasValidScale();
    }

    protected abstract SupportedEngine getSupportedEngine();

    protected abstract String getResourceKey();

    protected String[] buildKey(String key) {
        return new String[] {getSupportedEngine().name(), getResourceKey(), key};
    }

    protected String getLocalizedString(String key) {
        return processor.getLocalizedData().getString(buildKey(key));
    }

    protected String getLocalizedFormattedString(String key, Object... args) {
        return processor.getLocalizedData().getFormattedString(buildKey(key), args);
    }
}
