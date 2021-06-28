package de.unileipzig.irpact.start;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.IRPactException;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.logging.LoggingMessage;
import de.unileipzig.irpact.commons.resource.BasicResourceLoader;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.FileUtil;
import de.unileipzig.irpact.core.logging.*;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactCallback;
import de.unileipzig.irpact.start.irpact.IRPactExecutor;
import de.unileipzig.irpact.start.irpact.IRPactExecutors;
import de.unileipzig.irpact.start.irpact.callbacks.GetOutput;
import de.unileipzig.irpact.start.optact.OptAct;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.IRPData;
import de.unileipzig.irptools.io.IRPFile;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.data.AnnualEntry;
import de.unileipzig.irptools.io.base.file.Config;
import de.unileipzig.irptools.io.downloaded.DownloadedFile;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.io.swagger.DownloadedSwaggerFile;
import de.unileipzig.irptools.io.swagger.UploadableSwaggerFile;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class Preloader3 {

    /**
     * @author Daniel Abitz
     */
    private enum SubModul {
        UNKNOWN,
        IRPACT,
        OPTACT
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Preloader3.class);

    private static final String GETOUTPUT_CALLBACK_NAME = "Preloader_GetOutput";
    private static final int VALID_INDEX = 0;

    private final List<LoggingMessage> MESSAGES = new ArrayList<>();

    private MainCommandLineOptions clOptions;
    private Collection<? extends IRPactCallback> callbacks;
    private ResourceLoader resourceLoader;

    public Preloader3() {
    }

    //==================================================
    //general + util
    //==================================================

    private void setupResourceLoader() {
        BasicResourceLoader loader = new BasicResourceLoader();
        loader.setExternalPath(clOptions.getDataDirPath());
        resourceLoader = loader;
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "data dir: {}", clOptions.getDataDirPath());
    }

    public static void checkVersion(IRPFile fileWithSingleEntry) throws Exception {
        JsonPointer ptr = setsPointer(fileWithSingleEntry, InRoot.SET_VERSION);

        JsonNode versionNode = fileWithSingleEntry.root().at(ptr);
        if(versionNode == null) {
            throw new Exception("'" + InRoot.SET_VERSION + "' not found");
        }
        if(versionNode.isObject()) {
            ObjectNode versionObj = (ObjectNode) versionNode;
            if(versionObj.size() != 1) {
                throw new Exception("version node not valid (size != 1)");
            }
            Iterator<String> iter = versionNode.fieldNames();
            String inputVersion = iter.next();
            checkVersion(inputVersion);
        } else {
            throw new Exception("version node not valid (no object): " + versionNode.getNodeType());
        }
    }

    private static void checkVersion(String inputVersion) {
        LOGGER.info(IRPSection.GENERAL, "IRPact version: {}, input version: {}", IRPact.VERSION, inputVersion);
    }

    public static JsonPointer scalarsPointer(IRPFile validFile, String name) {
        return scalarsPointer(validFile.getType(), name);
    }

    public static JsonPointer scalarsPointer(IRPFile validFile, int index, String name) {
        return scalarsPointer(validFile.getType(), index, name);
    }

    public static JsonPointer scalarsPointer(ContentType type, String name) {
        return scalarsPointer(type, VALID_INDEX, name);
    }

    public static JsonPointer scalarsPointer(ContentType type, int index, String name) {
        switch(type) {
            case ANNUAL:
                return AnnualFile.scalars(name);

            case PERENNIAL:
                return PerennialFile.scalars(index, name);

            case DOWNLOADED:
                return DownloadedFile.scalars(VALID_INDEX, index, name);

            case DOWNLOADED_SWAGGER:
                return DownloadedSwaggerFile.scalars(VALID_INDEX, index, name);

            case UPLOADABLE_SWAGGER:
                return UploadableSwaggerFile.scalars(VALID_INDEX, index, name);

            case EMPTY:
                throw new IllegalArgumentException("empty file");

            case UNKNOWN:
                throw new IllegalArgumentException("unknown file type");

            default:
                throw new IllegalArgumentException("unsupported file type: " + type);
        }
    }

    public static JsonPointer setsPointer(IRPFile validFile, String name) {
        return setsPointer(validFile.getType(), name);
    }

    public static JsonPointer setsPointer(IRPFile validFile, int index, String name) {
        return setsPointer(validFile.getType(), index, name);
    }

    public static JsonPointer setsPointer(ContentType type, String name) {
        return setsPointer(type, VALID_INDEX, name);
    }

    public static JsonPointer setsPointer(ContentType type, int index, String name) {
        switch(type) {
            case ANNUAL:
                return AnnualFile.sets(name);

            case PERENNIAL:
                return PerennialFile.sets(index, name);

            case DOWNLOADED:
                return DownloadedFile.sets(VALID_INDEX, index, name);

            case DOWNLOADED_SWAGGER:
                return DownloadedSwaggerFile.sets(VALID_INDEX, index, name);

            case UPLOADABLE_SWAGGER:
                return UploadableSwaggerFile.sets(VALID_INDEX, index, name);

            case EMPTY:
                throw new IllegalArgumentException("empty file");

            case UNKNOWN:
                throw new IllegalArgumentException("unknown file type");

            default:
                throw new IllegalArgumentException("unsupported file type: " + type);
        }
    }

    public static Config getConfig(IRPFile validFile, int index) {
        switch(validFile.getType()) {
            case ANNUAL:
                AnnualFile annualFile = (AnnualFile) validFile;
                return annualFile.getYear().getConfig();

            case PERENNIAL:
                PerennialFile perennialFile = (PerennialFile) validFile;
                return perennialFile.getYears().get(index).getConfig();

            case DOWNLOADED:
                DownloadedFile downloadedFile = (DownloadedFile) validFile;
                return downloadedFile.getData().get(VALID_INDEX).getYears().get(index).getConfig();

            case DOWNLOADED_SWAGGER:
                DownloadedSwaggerFile modelsFile = (DownloadedSwaggerFile) validFile;
                return modelsFile.getModels().get(VALID_INDEX).getYears().get(index).getConfig();

            case UPLOADABLE_SWAGGER:
                UploadableSwaggerFile dataFile = (UploadableSwaggerFile) validFile;
                return dataFile.getData().getModels().get(VALID_INDEX).getYears().get(index).getConfig();

            case EMPTY:
                throw new IllegalArgumentException("empty file");

            case UNKNOWN:
                throw new IllegalArgumentException("unknown file type");

            default:
                throw new IllegalArgumentException("unsupported file type: " + validFile.getType());
        }
    }

    public static ObjectNode getRootAt(IRPFile validFile, int index) {
        switch(validFile.getType()) {
            case ANNUAL:
                AnnualFile annualFile = (AnnualFile) validFile;
                return annualFile.getYear().root();

            case PERENNIAL:
                PerennialFile perennialFile = (PerennialFile) validFile;
                return perennialFile.getYears().get(index).root();

            case DOWNLOADED:
                DownloadedFile downloadedFile = (DownloadedFile) validFile;
                return downloadedFile.getData().get(VALID_INDEX).getYears().get(index).root();

            case DOWNLOADED_SWAGGER:
                DownloadedSwaggerFile modelsFile = (DownloadedSwaggerFile) validFile;
                return modelsFile.getModels().get(VALID_INDEX).getYears().get(index).root();

            case UPLOADABLE_SWAGGER:
                UploadableSwaggerFile dataFile = (UploadableSwaggerFile) validFile;
                return dataFile.getData().getModels().get(VALID_INDEX).getYears().get(index).root();

            case EMPTY:
                throw new IllegalArgumentException("empty file");

            case UNKNOWN:
                throw new IllegalArgumentException("unknown file type");

            default:
                throw new IllegalArgumentException("unsupported file type: " + validFile.getType());
        }
    }

    private static boolean getBoolean(String fileName, IRPFile file, JsonPointer ptr) {
        JsonNode node = file.root().at(ptr);
        if(node == null) {
            throw new IRPactIllegalArgumentException("missing entry '{}' in file '{}'", ptr, fileName);
        }
        if(node.isBoolean()) {
            return node.booleanValue();
        }
        if(node.isNumber()) {
            return node.intValue() == 1;
        }
        throw new IRPactIllegalArgumentException("missing boolean entry '{}' in file '{}'", ptr, fileName);
    }

    private static int getInt(String fileName, IRPFile file, JsonPointer ptr) {
        JsonNode node = file.root().at(ptr);
        if(node == null) {
            throw new IRPactIllegalArgumentException("missing entry '{}' in file '{}'", ptr, fileName);
        }
        if(node.isNumber()) {
            return node.intValue();
        }
        throw new IRPactIllegalArgumentException("missing number entry '{}' in file '{}'", ptr, fileName);
    }

    public static void setupLogging(MainCommandLineOptions options) throws IOException {
        if(options.logToFile()) {
            IRPLoggingMessage deletedMsg = null;
            if(Files.exists(options.getLogPath())) {
                Files.delete(options.getLogPath());
                deletedMsg = new IRPLoggingMessage("old logfile '{}' deleted", options.getLogPath()).setSection(IRPSection.INITIALIZATION_PARAMETER);
            }

            if(options.logConsoleAndFile()) {
                IRPLogging.writeToConsoleAndFile(options.getLogPath());
                if(deletedMsg != null) {
                    deletedMsg.trace(LOGGER);
                }
                if(options.isNotPrintHelpOrVersion()) {
                    LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to console and file '{}'", options.getLogPath());
                }
            } else {
                IRPLogging.writeToFile(options.getLogPath());
                if(deletedMsg != null) {
                    deletedMsg.trace(LOGGER);
                }
                if(options.isNotPrintHelpOrVersion()) {
                    LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to file '{}'", options.getLogPath());
                }
            }
        } else {
            if(options.isNotPrintHelpOrVersion()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to console");
            }
        }
    }

    public static void parseAndApplyLoggingSettings(String fileName, IRPFile file, int index, SectionLoggingFilter filter) {
        int logLevel = getInt(fileName, file, scalarsPointer(file, InGeneral.SCA_INGENERAL_LOGLEVEL));

        boolean logAll = getBoolean(fileName, file, scalarsPointer(file, index, InGeneral.SCA_INGENERAL_LOGALL));
        boolean logAllIRPact = getBoolean(fileName, file, scalarsPointer(file, index, InGeneral.SCA_INGENERAL_LOGALLIRPACT));
        boolean logAllTools = getBoolean(fileName, file, scalarsPointer(file, index, InGeneral.SCA_INGENERAL_LOGALLTOOLS));
        boolean logInitialization = getBoolean(fileName, file, scalarsPointer(file, index, InGeneral.SCA_INGENERAL_LOGINITIALIZATION));
        boolean logSimulation = getBoolean(fileName, file, scalarsPointer(file, index, InGeneral.SCA_INGENERAL_LOGSIMULATION));

        IRPLevel level = IRPLevel.get(logLevel);
        if(level == null) {
            LOGGER.warn(IRPSection.GENERAL, "invalid log level {}, set level to default ({}) ", logLevel, IRPLevel.getDefault());
            level = IRPLevel.getDefault();
        }
        IRPLogging.setLevel(level);

        IRPSection.removeAllFrom(filter);
        IRPSection.addAllTo(logAll, filter);
        IRPSection.addAllNonToolsTo(logAllIRPact, filter);
        IRPSection.addAllToolsTo(logAllTools, filter);
        IRPSection.addInitialization(logInitialization, filter);
        IRPSection.addSimulation(logSimulation, filter);

        LOGGER.trace(IRPSection.GENERAL, "logLevel={}", logLevel);
        LOGGER.trace(IRPSection.GENERAL, "logAll={}", logAll);
        LOGGER.trace(IRPSection.GENERAL, "logAllIRPact={}", logAllIRPact);
        LOGGER.trace(IRPSection.GENERAL, "logAllTools={}", logAllTools);
        LOGGER.trace(IRPSection.GENERAL, "logInitialization={}", logInitialization);
        LOGGER.trace(IRPSection.GENERAL, "logSimulation={}", logSimulation);
    }

    public static void applyLoggingSettings(InRoot root, SectionLoggingFilter filter) {
        int logLevel = root.getGeneral().logLevel;

        boolean logAll = root.getGeneral().logAll;
        boolean logAllIRPact = root.getGeneral().logAllIRPact;
        boolean logAllTools = root.getGeneral().logAllTools;
        boolean logInitialization = root.getGeneral().logInitialization;
        boolean logSimulation = root.getGeneral().logSimulation;

        IRPLevel level = IRPLevel.get(logLevel);
        if(level == null) {
            LOGGER.warn(IRPSection.GENERAL, "invalid log level {}, set level to default ({}) ", logLevel, IRPLevel.getDefault());
            level = IRPLevel.getDefault();
        }
        IRPLogging.setLevel(level);

        IRPSection.removeAllFrom(filter);
        IRPSection.addAllTo(logAll, filter);
        IRPSection.addAllNonToolsTo(logAllIRPact, filter);
        IRPSection.addAllToolsTo(logAllTools, filter);
        IRPSection.addInitialization(logInitialization, filter);
        IRPSection.addSimulation(logSimulation, filter);

        LOGGER.trace(IRPSection.GENERAL, "logLevel={}", logLevel);
        LOGGER.trace(IRPSection.GENERAL, "logAll={}", logAll);
        LOGGER.trace(IRPSection.GENERAL, "logAllIRPact={}", logAllIRPact);
        LOGGER.trace(IRPSection.GENERAL, "logAllTools={}", logAllTools);
        LOGGER.trace(IRPSection.GENERAL, "logInitialization={}", logInitialization);
        LOGGER.trace(IRPSection.GENERAL, "logSimulation={}", logSimulation);
    }

    private void logRemainingMessages() {
        for(LoggingMessage msg: MESSAGES) {
            msg.log(LOGGER);
        }
        MESSAGES.clear();
    }

    private static Collection<? extends IRPactCallback> buildCallbacks(Collection<? extends IRPactCallback> callbacks, GetOutput getOutput) {
        if(getOutput == null) {
            return callbacks;
        } else {
            List<IRPactCallback> cbs = new ArrayList<>(callbacks);
            cbs.add(getOutput);
            return cbs;
        }
    }

    //==================================================
    //args based
    //==================================================

    public void load(MainCommandLineOptions clOptions, Collection<? extends IRPactCallback> callbacks) throws Exception {
        try {
            load0(clOptions, callbacks);
        } catch (Exception e) {
            logRemainingMessages();
            throw e;
        }
    }

    private void load0(MainCommandLineOptions clOptions, Collection<? extends IRPactCallback> callbacks) throws Exception {
        this.clOptions = clOptions;
        this.callbacks = callbacks == null ? Collections.emptyList() : callbacks;

        if(Files.isDirectory(clOptions.getInputPath())) {
            loadSpec();
        }  else {
            loadFile();
        }
    }

    private void loadSpec() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("currently not supported");
    }

    private void loadFile() throws Exception {
        MESSAGES.add(new IRPLoggingMessage()
                .setSection(IRPSection.INITIALIZATION_PARAMETER)
                .setLevel(Level.TRACE)
                .set("load input file '{}'", clOptions.getInputPathFileName())
        );
        IRPFile file = ContentTypeDetector.parse(clOptions.getInputPath(), StandardCharsets.UTF_8);
        validateInput(clOptions.getInputPath(), file);

        parseAndApplyLoggingSettings(clOptions.getInputPathFileName(), file, VALID_INDEX, IRPLogging.getFilter());
        logRemainingMessages();
        logSomeInfos();

        setupResourceLoader();

        if(runOptAct(file)) {
            callOptAct(file);
        } else {
            callIRPact(file);
        }
    }

    private void logSomeInfos() {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "input file: {}", clOptions.getInputPath());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "output file: {}", clOptions.getOutputPath());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "image file: {}", clOptions.getImagePath());
        if(clOptions.isNoSimulation()) {
            LOGGER.trace("simulation disabled");
        } else {
            LOGGER.trace("simulation enabled");
        }
    }

    private boolean runOptAct(IRPFile file) {
        String fileName = clOptions.getInputPath().getFileName().toString();
        return getBoolean(fileName, file, scalarsPointer(file, InGeneral.SCA_INGENERAL_RUNOPTACTDEMO));
    }

    private void validateInput(Path path, IRPFile file) {
        String fileName = path.getFileName().toString();
        MESSAGES.add(new IRPLoggingMessage()
                .setSection(IRPSection.INITIALIZATION_PARAMETER)
                .setLevel(Level.TRACE)
                .set("validating input file '{}' (type: {})", fileName, file.getType())
        );

        if(file.numberOfEntries() == 0) {
            throw new IRPactIllegalArgumentException("no scenario found in file '{}'", fileName);
        }

        switch(file.getType()) {
            case ANNUAL:
            case PERENNIAL:
                //always ok
                break;

            case DOWNLOADED:
                DownloadedFile df = (DownloadedFile) file;
                if(df.getData().size() != 1) {
                    throw new IRPactIllegalArgumentException("file '{}': illegal number of \"data\" entries: {}", fileName, df.getData().size());
                }
                break;

            case DOWNLOADED_SWAGGER:
                DownloadedSwaggerFile mf = (DownloadedSwaggerFile) file;
                if(mf.getModels().size() != 1) {
                    throw new IRPactIllegalArgumentException("file '{}': illegal number of \"models\" entries: {}", fileName, mf.getModels().size());
                }
                break;

            case UPLOADABLE_SWAGGER:
                UploadableSwaggerFile dataf = (UploadableSwaggerFile) file;
                if(dataf.getData().getModels().size() != 1) {
                    throw new IRPactIllegalArgumentException("file '{}': illegal number of \"models\" entries: {}", fileName, dataf.getData().getModels().size());
                }
                break;

            case EMPTY:
                throw new IRPactIllegalArgumentException("empty file");

            case UNKNOWN:
                throw new IRPactIllegalArgumentException("unknown file type");

            default:
                throw new IRPactIllegalArgumentException("unsupported file type: {}", file.getType());
        }
    }

    //=========================
    //optact
    //=========================

    private void callOptAct(IRPFile file) {
        LOGGER.trace(IRPSection.GENERAL, "call optact");
        OptAct optact = new OptAct(clOptions, (ObjectNode) file.root());
        optact.start();
    }

    //=========================
    //IRPact
    //=========================

    private MainCommandLineOptions updateOptions(int year) {
        MainCommandLineOptions copy = clOptions.copy();
        if(copy.logToFile()) {
            Path currentLogPath = copy.getLogPath();
            Path updatedLogPath = FileUtil.changeFileName(currentLogPath, "", "-" + year);
            LOGGER.trace("update log file path: {} -> {}", currentLogPath.getFileName(), updatedLogPath.getFileName());
            copy.setLogPath(updatedLogPath);
        }
        if(copy.hasOutputPath()) {
            Path currentOutputPath = copy.getOutputPath();
            Path updatedOutputPath = FileUtil.changeFileName(currentOutputPath, "", "-" + year);
            LOGGER.trace("update output file path: {} -> {}", currentOutputPath.getFileName(), updatedOutputPath.getFileName());
            copy.setOutputPath(updatedOutputPath);
        }
        return copy;
    }

    private void adaptLogging(MainCommandLineOptions clOptions) throws IOException {
        setupLogging(clOptions);
    }

    private void updateInRoot(InRoot next, OutRoot lastResult) {
        LOGGER.trace(IRPSection.GENERAL, "update 'binaryPersistData'");
        next.binaryPersistData = lastResult.getBinaryPersistData();
    }

    private void callIRPact(IRPFile file) throws Exception {
        checkVersion(file);

        IRPData<InRoot> data = file.deserialize(IRPact.getInputConverter(clOptions));
        List<AnnualEntry<InRoot>> entries = data.listEntries();

        int lastConfigYear = 0;
        OutRoot lastResult = null;
        LOGGER.trace(IRPSection.GENERAL, "calling IRPact {} time(s)", entries.size());
        for(int i = 0; i < entries.size(); i++) {
            LOGGER.trace(IRPSection.GENERAL, "calling IRPact ({}/{})", i + 1, entries.size());

            if(i > 0 && lastResult == null) {
                throw new IRPactException("result of previous call is null, terminating IRPact");
            }

            //forced cleanup
            if(i > 0) {
                System.gc();
            }

            Config config = getConfig(file, i);
            int configYear = config.getYear();

            MainCommandLineOptions clOpt;
            if(i > 0) {
                LOGGER.trace(IRPSection.GENERAL, "update options (config.year: {} -> {})", lastConfigYear, configYear);
                clOpt = updateOptions(configYear);
                adaptLogging(clOpt);
                parseAndApplyLoggingSettings(clOpt.getInputPathFileName(), file, i, IRPLogging.getFilter());
            } else {
                clOpt = clOptions;
            }

            AnnualEntry<InRoot> entry = entries.get(i);
            if(i > 0) {
                LOGGER.trace(IRPSection.GENERAL, "update entry (config.year: {} -> {})", lastConfigYear, configYear);
                updateInRoot(entry.getData(), lastResult);
            }

            ObjectNode jsonRoot = getRootAt(file, i);

            lastResult = callIRPact(clOpt, jsonRoot, entry, i + 1, entries.size());
            lastConfigYear = configYear;
        }
    }

    private OutRoot callIRPact(
            MainCommandLineOptions clOpt,
            ObjectNode jsonRoot,
            AnnualEntry<InRoot> entry,
            int current,
            int total) throws Exception {
        GetOutput outputCallback = current == total ? null : new GetOutput(GETOUTPUT_CALLBACK_NAME);
        Collection<? extends IRPactCallback> cbs = buildCallbacks(callbacks, outputCallback);
        IRPact irpact = new IRPact(clOpt, cbs, resourceLoader);
        irpact.init(jsonRoot, entry);

        int runMode = entry.getData().getGeneral().getRunMode();
        if(IRPactExecutors.hasNot(runMode)) {
            runMode = clOpt.getRunMode();
        }

        LOGGER.trace(IRPSection.GENERAL, "call IRPact (current={}, total={}, mode={})", current, total, runMode);
        IRPactExecutor exec = IRPactExecutors.get(runMode);
        exec.execute(irpact);

        return outputCallback == null
                ? null
                : outputCallback.getOutput().getData();
    }

    //==================================================
    //scenario based
    //==================================================

    public void load(Collection<? extends Start3.Input> inputs) throws Exception {
        OutRoot lastResult = null;
        int i = 0;
        LOGGER.trace(IRPSection.GENERAL, "calling IRPact {} time(s)", inputs.size());
        for(Start3.Input input: inputs) {
            LOGGER.trace(IRPSection.GENERAL, "calling IRPact ({}/{})", i + 1, inputs.size());

            if(i > 0 && lastResult == null) {
                throw new IRPactException("result of previous call is null, terminating IRPact");
            }

            setup(input);
            adaptLogging(clOptions);
            applyLoggingSettings(input.getScenarioData(), IRPLogging.getFilter());

            InRoot inputRoot = input.getScenarioData();

            if(i > 0) {
                //copy input root?
                updateInRoot(inputRoot, lastResult);
            }

            lastResult = callIRPact(input, clOptions, i + 1, inputs.size());
            i++;
        }
    }

    private void setup(Start3.Input input) {
        String[] args = input.getArgs();
        clOptions = new MainCommandLineOptions(args);
        clOptions.parse();
    }

    private OutRoot callIRPact(
            Start3.Input input,
            MainCommandLineOptions clOpt,
            int current,
            int total) throws Exception {
        GetOutput outputCallback = current == total ? null : new GetOutput(GETOUTPUT_CALLBACK_NAME);
        Collection<? extends IRPactCallback> cbs = buildCallbacks(callbacks, outputCallback);
        IRPact irpact = new IRPact(clOpt, cbs, resourceLoader);
        irpact.init(input.getScenario());

        int runMode = input.getScenario().getData().getGeneral().getRunMode();
        if(IRPactExecutors.hasNot(runMode)) {
            runMode = clOpt.getRunMode();
        }

        LOGGER.trace(IRPSection.GENERAL, "call IRPact (current={}, total={}, mode={})", current, total, runMode);
        IRPactExecutor exec = IRPactExecutors.get(runMode);
        exec.execute(irpact);

        return outputCallback == null
                ? null
                : outputCallback.getOutput().getData();
    }
}
