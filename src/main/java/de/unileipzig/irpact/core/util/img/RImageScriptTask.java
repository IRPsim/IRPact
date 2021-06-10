package de.unileipzig.irpact.core.util.img;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class RImageScriptTask extends AbstractImageScriptTask {

    public static final String R = "R";

    public RImageScriptTask(boolean storeScript, boolean storeData, boolean storeImage) {
        super(storeScript, storeData, storeImage);
    }

    @Override
    public void setupPaths(Path dir, String baseName, String dataExtension, String imageExtension) {
        setupPaths(dir, baseName, R ,dataExtension, imageExtension);
    }
}
