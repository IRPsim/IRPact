package de.unileipzig.irpact.core.postprocessing.image;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class GnuPlotImageScriptTask extends AbstractImageScriptTask {

    public static final String GNUPLOT = "gnu";

    public GnuPlotImageScriptTask(boolean storeScript, boolean storeData, boolean storeImage) {
        super(storeScript, storeData, storeImage);
    }

    @Override
    public void setupPaths(Path dir, String baseName, String dataExtension, String imageExtension) {
        setupPaths(dir, baseName, GNUPLOT ,dataExtension, imageExtension);
    }
}
