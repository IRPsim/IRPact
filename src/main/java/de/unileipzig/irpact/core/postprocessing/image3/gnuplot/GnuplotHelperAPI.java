package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.engine.GnuPlotImageScriptTask;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotFileScript;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface GnuplotHelperAPI<I extends InOutputImage2> {

    Path getTargetDir();

    String getCsvDelimiter();

    GnuPlotEngine getEngine();

    GnuPlotBuilder getBuilder(I image) throws Throwable;

    ImageData createData(I image) throws Throwable;

    default void handleImage(I image, boolean engineUsable) throws Throwable {
        GnuPlotBuilder builder = getBuilder(image);
        if(builder == null) {
            return;
        }

        ImageData data = createData(image);
        if(data == null) {
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
    }
}
