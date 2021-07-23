package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.postprocessing.image.engine.RImageScriptTask;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.R.RFileScript;
import de.unileipzig.irpact.util.R.RscriptEngine;
import de.unileipzig.irpact.util.R.builder.RScriptBuilder;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractRscriptDataVisualizer extends AbstractDataVisualizer {

    protected static final StringSettings GNUPLOT_STRING_SETTINGS = new StringSettings();

    public AbstractRscriptDataVisualizer(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    protected boolean isEngineNotUsable() {
        return imageProcessor.isRNotUsable();
    }

    protected RscriptEngine getEngine() {
        return imageProcessor.getRscriptEngine();
    }

    @Override
    protected String getEngineName() {
        return "Rscript";
    }

    protected String map(String input) {
        return input;
    }

    protected abstract RScriptBuilder getBuilder(InOutputImage image);

    protected abstract ImageData createData(InOutputImage image);

    @Override
    protected void handleImageWithWorkingEngine(InOutputImage image) throws IOException {
        RScriptBuilder builder = getBuilder(image);
        if(builder == null) {
            return;
        }

        ImageData data = createData(image);
        if(data == null) {
            return;
        }

        execute(image, builder, data);
    }

    protected void execute(InOutputImage image, RScriptBuilder builder, ImageData data) throws IOException {
        builder.setSettings(GNUPLOT_STRING_SETTINGS);
        RFileScript fileScript = builder.build();
        RImageScriptTask task = new RImageScriptTask(image.isStoreScript(), image.isStoreData(), image.isStoreImage());
        task.setupCsvAndPng(getTargetDir(), image.getBaseFileName());
        task.setDelimiter(getLocalizedImageData().getSep(getMode()));
        task.run(
                getEngine(),
                data,
                fileScript
        );
    }
}
