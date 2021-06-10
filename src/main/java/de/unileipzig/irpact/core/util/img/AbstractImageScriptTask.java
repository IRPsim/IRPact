package de.unileipzig.irpact.core.util.img;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractImageScriptTask {

    public static final String CSV = "csv";
    public static final String PNG = "png";

    protected boolean storeScript;
    protected boolean storeData;
    protected boolean storeImage;

    protected Path scriptPath;
    protected Path dataPath;
    protected Path imagePath;

    public AbstractImageScriptTask(boolean storeScript, boolean storeData, boolean storeImage) {
        this.storeScript = storeScript;
        this.storeData = storeData;
        this.storeImage = storeImage;
    }

    public boolean isStoreScript() {
        return storeScript;
    }

    public boolean isStoreData() {
        return storeData;
    }

    public boolean isStoreImage() {
        return storeImage;
    }

    public void setupPaths(
            Path dir, String baseName,
            String scriptExtension, String dataExtension, String imageExtension) {
        scriptPath = dir.resolve(baseName + "." + scriptExtension);
        dataPath = dir.resolve(baseName + "." + dataExtension);
        imagePath = dir.resolve(baseName + "." + imageExtension);
    }

    public abstract void setupPaths(
            Path dir, String baseName,
            String dataExtension, String imageExtension);

    public Path getScriptPath() {
        return scriptPath;
    }

    public Path getDataPath() {
        return dataPath;
    }

    public Path getImagePath() {
        return imagePath;
    }
}
