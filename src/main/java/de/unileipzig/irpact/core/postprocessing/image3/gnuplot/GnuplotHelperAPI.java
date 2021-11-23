package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.engine.GnuPlotImageScriptTask;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotFileScript;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author Daniel Abitz
 */
public interface GnuplotHelperAPI<I extends InOutputImage2> extends LoggingHelper {

    Path getTargetDir();

    String getCsvDelimiter();

    GnuPlotEngine getEngine();

    GnuPlotBuilder getBuilder(I image, ImageData data) throws Throwable;

    ImageData createData(I image) throws Throwable;

    default void handleImage(I image, boolean engineUsable) throws Throwable {
        ImageData data = createData(image);
        if(data == null) {
            return;
        }

        GnuPlotBuilder builder = getBuilder(image, data);
        if(builder == null) {
            return;
        }

        execute(image, builder, data, engineUsable);
    }

    default void execute(I image, GnuPlotBuilder builder, ImageData data, boolean engineUsable) throws Throwable {
        GnuPlotFileScript fileScript = builder.build();
        GnuPlotImageScriptTask task = new GnuPlotImageScriptTask(image.isStoreScript(), image.isStoreData(), image.isStoreImage());
        task.setupCsvAndPng(getTargetDir(), image.getBaseFileName());
        task.setDelimiter(getCsvDelimiter());
        if(engineUsable) {
            task.run(getEngine(), data, fileScript);
        } else {
            task.runWihoutEngine(data, fileScript);
        }
        copyImageIfRequired(image, task.getImagePath());
    }

    default void copyImageIfRequired(I image, Path imagePath) throws IOException {
        if(IRPact.isValidImageId(image.getCustomImageId())) {
            if(Files.exists(imagePath)) {
                Path customImagePath = getTargetDir().resolve(IRPact.getCustomImagePng(image.getCustomImageId()));
                if(Files.exists(customImagePath)) {
                    warn("image '{}' already exists, overwrite file", customImagePath);
                } else {
                    info("create custom '{}'", customImagePath);
                }
                Files.copy(imagePath, customImagePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                warn("file '{}' not found, skip custom image id {}", imagePath, image.getCustomImageId());
            }
        }
    }
}
