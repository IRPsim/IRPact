package de.unileipzig.irpact.core.postprocessing.image;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.util.script.Engine;
import de.unileipzig.irpact.util.script.FileScript;
import de.unileipzig.irpact.util.script.ProcessBasedFileScript;
import de.unileipzig.irpact.util.script.ScriptException;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractImageScriptTask extends NameableBase {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AbstractImageScriptTask.class);

    protected static final String SUFFIX_DATA = "-data";
    protected static final String SUFFIX_SCRIPT = "-script";

    public static final String CSV = "csv";
    public static final String PNG = "png";

    protected String delimiter = ";";
    protected Charset charset = StandardCharsets.UTF_8;

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

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    protected String printName() {
        return "[" + getName() + "]";
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
        setName(baseName);
        scriptPath = dir.resolve(baseName + SUFFIX_SCRIPT + "." + scriptExtension);
        dataPath = dir.resolve(baseName + SUFFIX_DATA + "." + dataExtension);
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

    public boolean writeScript(FileScript<?> script) {
        try {
            writeScript0(script);
            return true;
        } catch (IOException e) {
            LOGGER.error(printName() + " writing script failed", e);
            return false;
        }
    }

    protected void writeScript0(FileScript<?> script) throws IOException {
        script.store(getScriptPath(), charset);
        LOGGER.trace("created script: {}", getScriptPath());
    }

    public boolean writeCsvData(List<List<String>> dataWithHeader) {
        try {
            writeCsvData0(dataWithHeader);
            return true;
        } catch (IOException e) {
            LOGGER.error(printName() + " writing data failed", e);
            return false;
        }
    }

    protected void writeCsvData0(List<List<String>> dataWithHeader) throws IOException {
        CsvPrinter<String> printer = new CsvPrinter<>(CsvPrinter.STRING_IDENTITY);
        printer.setDelimiter(delimiter);
        try(BufferedWriter writer = Files.newBufferedWriter(getDataPath(), charset)) {
            printer.setWriter(writer);
            printer.writeHeader(dataWithHeader.get(0));
            for(int i = 1; i < dataWithHeader.size(); i++) {
                printer.appendRow(dataWithHeader.get(i));
            }
        }
        LOGGER.trace(IRPSection.RESULT, "created data: {}", getDataPath());
    }

    public <E extends Engine> boolean execute(E engine, ProcessBasedFileScript<E> script) {
        script.setPath(getScriptPath());
        script.addPathArgument(getDataPath());
        script.addPathArgument(getImagePath());
        try {
            script.execute(engine);
            LOGGER.trace(IRPSection.RESULT, "executed script, image: {}", getImagePath());
            if(script.hasWarnMessage()) {
                LOGGER.warn(IRPSection.RESULT, "script warning:\n{}", script.getWarnMessage());
            }
            return true;
        } catch (IOException | InterruptedException | ScriptException e) {
            LOGGER.error(printName() + " executing script failed", e);
            return false;
        }
    }

    public <E extends Engine> boolean run(
            E engine,
            List<List<String>> dataWithHeader,
            ProcessBasedFileScript<E> script) {
        try {
            return writeCsvData(dataWithHeader)
                    && writeScript(script)
                    && execute(engine, script);
        } finally {
            cleanUp();
        }
    }

    public void deleteAll() {
        delete(dataPath);
        delete(scriptPath);
        delete(imagePath);
    }

    private static void delete(Path path) {
        try {
            boolean exists = Files.exists(path);
            boolean deleted = Files.deleteIfExists(path);
            LOGGER.trace(IRPSection.RESULT, "deleted: '{}' (exists={}, deleted={})", path, exists, deleted);
        } catch (IOException e) {
            LOGGER.error("deleting '" + path + "' failed", e);
        }
    }

    public void cleanUp() {
        cleanUp(storeData, dataPath);
        cleanUp(storeScript, scriptPath);
        cleanUp(storeImage, imagePath);
    }

    private static void cleanUp(boolean storeFlag, Path path) {
        if(storeFlag) {
            LOGGER.trace(IRPSection.RESULT, "stored '{}' (exists={})", path, Files.exists(path));
        } else {
            delete(path);
        }
    }
}
