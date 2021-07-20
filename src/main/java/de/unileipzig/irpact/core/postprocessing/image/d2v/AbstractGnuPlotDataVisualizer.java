package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.postprocessing.image.engine.GnuPlotImageScriptTask;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotFileScript;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractGnuPlotDataVisualizer extends AbstractDataVisualizer {

    protected static final StringSettings GNUPLOT_STRING_SETTINGS = new StringSettings();

    public AbstractGnuPlotDataVisualizer(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    protected boolean isEngineNotUsable() {
        return imageProcessor.isGnuPlotNotUsable();
    }

    protected GnuPlotEngine getEngine() {
        return imageProcessor.getGnuPlotEngine();
    }

    @Override
    protected String getEngineName() {
        return "gnuplot";
    }

    protected String map(String input) {
        return input.replace("_", "\\\\\\_");
    }

    protected abstract GnuPlotBuilder getBuilder(InOutputImage image);

    protected abstract ImageData createData(InOutputImage image);

    @Override
    protected void handleImageWithWorkingEngine(InOutputImage image) throws IOException {
        GnuPlotBuilder builder = getBuilder(image);
        if(builder == null) {
            return;
        }

        ImageData data = createData(image);
        if(data == null) {
            return;
        }

        execute(image, builder, data);
    }

    protected void execute(InOutputImage image, GnuPlotBuilder builder, ImageData data) throws IOException {
        builder.setSettings(GNUPLOT_STRING_SETTINGS);
        GnuPlotFileScript fileScript = builder.build();
        GnuPlotImageScriptTask task = new GnuPlotImageScriptTask(image.isStoreScript(), image.isStoreData(), image.isStoreImage());
        task.setupCsvAndPng(getTargetDir(), image.getBaseFileName());
        task.setDelimiter(getLocalizedImageData().getSep(getMode()));
        task.run(
                getEngine(),
                data,
                fileScript
        );
    }
}
