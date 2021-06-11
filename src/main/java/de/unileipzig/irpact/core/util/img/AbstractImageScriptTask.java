package de.unileipzig.irpact.core.util.img;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.util.script.Engine;
import de.unileipzig.irpact.util.script.ProcessBasedFileScript;
import de.unileipzig.irpact.util.script.ScriptException;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractImageScriptTask {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AbstractImageScriptTask.class);

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

    public void setupCsvAndPng(Path dir, String baseName) {
        setupPaths(dir, baseName, CSV, PNG);
    }

    public Path getScriptPath() {
        return scriptPath;
    }

    public Path getDataPath() {
        return dataPath;
    }

    public Path getImagePath() {
        return imagePath;
    }

    public <E extends Engine> void execute(E engine, ProcessBasedFileScript<E> script) {
        script.setPath(getScriptPath());
        script.addPathArgument(getDataPath());
        script.addPathArgument(getImagePath());
        try {
            script.execute(engine);
        } catch (IOException | InterruptedException | ScriptException e) {
            LOGGER.error("executing script '" + getScriptPath() + "' failed", e);
        }
    }

    public void cleanUp() {
        cleanUp(storeData, dataPath);
        cleanUp(storeScript, scriptPath);
        cleanUp(storeImage, imagePath);
    }

    private static void cleanUp(boolean storeFlag, Path path) {
        if(storeFlag) {
            LOGGER.trace(IRPSection.RESULT, "store '{}'", path);
        } else {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                LOGGER.error("deleting '" + path + "' failed", e);
            }
        }
    }
}
