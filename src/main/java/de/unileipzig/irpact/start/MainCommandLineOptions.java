package de.unileipzig.irpact.start;

import de.unileipzig.irpact.commons.log.LoggingMessage;
import de.unileipzig.irpact.commons.util.AbstractCommandLineOptions;
import de.unileipzig.irpact.commons.util.MapResourceBundle;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactExecutors;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
@CommandLine.Command(
        name = "java -jar <IRPact.jar>",
        version = {IRPact.CL_NAME, IRPact.CL_VERSION, IRPact.CL_COPY}
)
public class MainCommandLineOptions extends AbstractCommandLineOptions {

    private static ResourceBundle fallback;

    private static synchronized ResourceBundle getFallbackBundle() {
        if(fallback == null) {
            MapResourceBundle bundle = new MapResourceBundle();

            bundle.put("printHelp", "Print help.");
            bundle.put("printVersion", "Print version information.");
            bundle.put("runMode", "The mode in which the program is to be executed. Currently supported are: '0: normal execution', '1: initialization and evaluation, no simulation'. Default value is ${DEFAULT-VALUE}.");
            bundle.put("inputPath", "Set path to input file.");
            bundle.put("outputPath", "Set path to output file.");
            bundle.put("imagePath", "Set path to image output file. Without '--noSimulation' the post-simulation network is printed.");
            bundle.put("noSimulation", "Disables everything except initialization. Combined with '--image' the initial network is printed.");
            bundle.put("checkOutputExistence", "Checks if the output file already exists. If it does, the program will be cancelled. This option is used to ensure that data is not overwritten.");

            bundle.put("useGamsNameTrimming", "Enables (1) or disables (0) gams name trimming. Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing.");
            bundle.put("maxGamsNameLength", "Max length for gams field names. Value -1 disables this option. Max length for gams is 63. Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing.");
            bundle.put("minGamsPartLength", "Min length for gams field name parts (tags). Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing.");

            bundle.put("logPath", "Set path to log file and disable console logging.");
            bundle.put("logConsoleAndFile", "Enable console logging if '--logPath' is set.");

            bundle.put("prefereCsv", "Prefere csv files when loading.");
            bundle.put("dataDirPath", "Set path to data directory. If not set, the execution directory will be used.");
            bundle.put("skipPersistenceCheck", "If set, checksum mismatches are ignore during restoration. Use this option only if you know what you are doing.");
            bundle.put("skipArgValidation", "Disables command line validation. Use this option only if you know what you are doing.");
            bundle.put("skipPersist", "Disables data persistence.");

            bundle.put("printIrptoolsHelp", "Calls IRPtools help.");
            bundle.put("callIrptools", "Calls IRPtools, all arguments will be transmitted to IRPtools, IRPact is not called.");

            bundle.put("printUtilitiesHelp", "Calls Utilities help.");
            bundle.put("callUtilities", "Calls Utilities, all arguments will be transmitted to Utilities, IRPact is not called.");

            bundle.put("testCl", "For testing the command line.");

            fallback = bundle;
        }
        return fallback;
    }

    private static final Path EXEC_DIR = Paths.get(".");

    private static final String TRUE1 = "1";
    private static final String FALSE0 = "0";

    //=========================
    //core irpact
    //=========================

    @CommandLine.Option(
            names = { "-?", "-h", "--help" },
            descriptionKey = "printHelp"
    )
    private boolean printHelp;

    @CommandLine.Option(
            names = { "-v", "--version" },
            descriptionKey = "printVersion"
    )
    private boolean printVersion;

    @CommandLine.Option(
            names = { "--mode" },
            defaultValue = IRPactExecutors.DEFAULT_MODE,
            descriptionKey = "runMode"
    )
    private int runMode;

    @CommandLine.Option(
            names = { "-i", "--input" },
            descriptionKey = "inputPath",
            converter = PathConverter.class
    )
    private Path inputPath;

    @CommandLine.Option(
            names = { "-o", "--output" },
            descriptionKey = "outputPath",
            converter = PathConverter.class
    )
    private Path outputPath;

    @CommandLine.Option(
            names = { "--image" },
            descriptionKey = "imagePath",
            converter = PathConverter.class
    )
    private Path imagePath;

    @CommandLine.Option(
            names = { "--noSimulation" },
            descriptionKey = "noSimulation"
    )
    private boolean noSimulation;

    @CommandLine.Option(
            names = { "--checkOutputExistence" },
            descriptionKey = "checkOutputExistence"
    )
    private boolean checkOutputExistence;

    //=========================
    //converter settings
    //=========================

    @CommandLine.Option(
            names = { "--useGamsNameTrimming" },
            defaultValue = FALSE0,
            descriptionKey = "useGamsNameTrimming"
    )
    private int useGamsNameTrimming;

    @CommandLine.Option(
            names = "--maxGamsNameLength",
            defaultValue = DefinitionMapper.IGNORE_NAME_LENGTH_AS_STR,
            descriptionKey = "maxGamsNameLength"
    )
    private int maxGamsNameLength;

    @CommandLine.Option(
            names = "--minGamsPartLength",
            defaultValue = DefinitionMapper.MIN_PART_LENGTH_AS_STR,
            descriptionKey = "minGamsPartLength"
    )
    private int minGamsPartLength;

    //=========================
    //logging
    //=========================

    @CommandLine.Option(
            names = { "--logPath" },
            descriptionKey = "logPath",
            converter = PathConverter.class
    )
    private Path logPath;

    @CommandLine.Option(
            names = { "--logConsoleAndFile" },
            descriptionKey = "logConsoleAndFile"
    )
    private boolean logConsoleAndFile;

    //=========================
    //diverses
    //=========================

    @Todo
    @CommandLine.Option(
            names = { "--prefereCsv" },
            descriptionKey = "prefereCsv",
            hidden = true
    )
    private boolean prefereCsv;

    @CommandLine.Option(
            names = { "--dataDir" },
            descriptionKey = "dataDirPath",
            converter = PathConverter.class
    )
    private Path dataDirPath;

    @CommandLine.Option(
            names = { "--skipPersistenceCheck" },
            descriptionKey = "skipPersistenceCheck"
    )
    private boolean skipPersistenceCheck;

    @CommandLine.Option(
            names = { "--skipArgValidation" },
            descriptionKey = "skipArgValidation"
    )
    private boolean skipArgValidation;

    @CommandLine.Option(
            names = { "--skipPersist" },
            descriptionKey = "skipPersist"
    )
    private boolean skipPersist;

    //=========================
    //irptools
    //=========================

    @CommandLine.Option(
            names = { "--irptoolsUsage" },
            descriptionKey = "printIrptoolsHelp"
    )
    protected boolean printIrptoolsHelp;

    @CommandLine.Option(
            names = { "--irptools" },
            descriptionKey = "callIrptools"
    )
    private boolean callIrptools;

    //=========================
    //utilities
    //=========================

    @CommandLine.Option(
            names = { "--utilitiesUsage" },
            descriptionKey = "printUtilitiesHelp"
    )
    protected boolean printUtilitiesHelp;

    @CommandLine.Option(
            names = { "--utilities" },
            descriptionKey = "callUtilities"
    )
    private boolean callUtilities;

    //=========================
    //develop
    //=========================

    @CommandLine.Option(
            names = { "--testCl" },
            descriptionKey = "testCl",
            hidden = true
    )
    private boolean testCl;

    //=========================
    //rest
    //=========================

    private boolean executed = false;
    private LoggingMessage executeResultMessage;

    private boolean hasCustomInput = false;
    private boolean hasCallback = false;

    public MainCommandLineOptions(String... args) {
        super(args);
    }

    public void setHasCustomInput(boolean hasCustomInput) {
        this.hasCustomInput = hasCustomInput;
    }

    public boolean hasCustomInput() {
        return hasCustomInput;
    }

    public void setHasCallback(boolean hasCallback) {
        this.hasCallback = hasCallback;
    }

    public boolean hasCallback() {
        return hasCallback;
    }

    protected void checkExecuted() {
        if(!executed) {
            throw new IllegalStateException("not executed");
        }
    }

    public int getExitCode() {
        checkExecuted();
        return exitCode;
    }

    public boolean wasExecuted() {
        return executed;
    }

    public boolean hasExecuteResultMessage() {
        return executeResultMessage != null;
    }

    public LoggingMessage getExecuteResultMessage() {
        return executeResultMessage;
    }

    @Override
    protected ResourceBundle getFallback() {
        return getFallbackBundle();
    }

    //=========================
    //options
    //=========================

    public boolean isPrintHelp() {
        checkExecuted();
        return printHelp;
    }

    public boolean isPrintVersion() {
        checkExecuted();
        return printVersion;
    }

    public boolean isPrintIrptoolsHelp() {
        checkExecuted();
        return printIrptoolsHelp;
    }

    public boolean isPrintUtilitiesHelp() {
        checkExecuted();
        return printUtilitiesHelp;
    }

    public boolean isPrintHelpOrVersion() {
        return isPrintHelp()
                || isPrintVersion();
    }

    public boolean isNotPrintHelpOrVersion() {
        return !isPrintHelpOrVersion();
    }

    public int getRunMode() {
        checkExecuted();
        return runMode;
    }

    public boolean hasInputPath() {
        return getInputPath() != null;
    }
    public Path getInputPath() {
        checkExecuted();
        return inputPath;
    }

    public boolean hasOutputPath() {
        return getOutputPath() != null;
    }
    public Path getOutputPath() {
        checkExecuted();
        return outputPath;
    }

    public boolean hasNoImagePath() {
        return !hasImagePath();
    }
    public boolean hasImagePath() {
        return getImagePath() != null;
    }
    public Path getImagePath() {
        checkExecuted();
        return imagePath;
    }

    public boolean logToFile() {
        return getLogPath() != null;
    }
    public Path getLogPath() {
        checkExecuted();
        return logPath;
    }

    public boolean logConsoleAndFile() {
        checkExecuted();
        return logConsoleAndFile;
    }

    public boolean isPrefereCsv() {
        checkExecuted();
        return prefereCsv;
    }

    public boolean isSimulation() {
        checkExecuted();
        return !noSimulation;
    }

    public boolean isNoSimulation() {
        checkExecuted();
        return noSimulation;
    }

    public boolean isNoSimulationAndNoImage() {
        return isNoSimulation() && hasNoImagePath();
    }

    public boolean hasCustomDataDirPath() {
        return getDataDirPath() != EXEC_DIR;
    }
    public Path getDataDirPath() {
        checkExecuted();
        return dataDirPath == null
                ? EXEC_DIR
                : dataDirPath;
    }

    public boolean isSkipPersistenceCheck() {
        checkExecuted();
        return skipPersistenceCheck;
    }

    public int getMaxGamsNameLength() {
        checkExecuted();
        return maxGamsNameLength;
    }

    public int getMinGamsPartLength() {
        checkExecuted();
        return minGamsPartLength;
    }

    public boolean isEnableGamsNameTrimming() {
        checkExecuted();
        return useGamsNameTrimming == 1;
    }

    public boolean isCallIrptools() {
        checkExecuted();
        return callIrptools;
    }

    public boolean isCallUtilities() {
        checkExecuted();
        return callUtilities;
    }

    public boolean isSkipPersist() {
        return skipPersist;
    }

    public boolean isTestCl() {
        checkExecuted();
        return testCl;
    }

    //=========================
    //call
    //=========================

    @Override
    public Integer call() throws Exception {
        if(executed) {
            throw new IllegalStateException("already executed");
        }
        executed = true;
        return validate();
    }

    private static Path tryGetPath(String pathStr) {
        return pathStr == null
                ? null
                : Paths.get(pathStr);
    }

    private boolean callSub() {
        return callIrptools || callUtilities;
    }

    private boolean cancelValidation() {
        return skipArgValidation
                || callSub()
                || isPrintHelpOrVersion();
    }

    @SuppressWarnings("RedundantIfStatement")
    private int validate() {
        if(cancelValidation()) {
            return CommandLine.ExitCode.OK;
        }

        int inputOk = validateInput();
        if(inputOk != CommandLine.ExitCode.OK) {
            return inputOk;
        }

        int outputOk = validateOutput();
        if(outputOk != CommandLine.ExitCode.OK) {
            return outputOk;
        }

        return CommandLine.ExitCode.OK;
    }

    private int validateInput() {
        if(hasInputPath()) {
            if(hasCustomInput()) {
                executeResultMessage = new LoggingMessage("custom input and file input specified");
                return CommandLine.ExitCode.USAGE;
            } else {
                return CommandLine.ExitCode.OK;
            }
        } else {
            if(hasCustomInput()) {
                return CommandLine.ExitCode.OK;
            } else {
                executeResultMessage = new LoggingMessage("missing input");
                return CommandLine.ExitCode.USAGE;
            }
        }
    }

    private int validateOutput() {
        if(hasOutputPath()) {
            return CommandLine.ExitCode.OK;
        } else {
            if(hasCallback()) {
                return CommandLine.ExitCode.OK;
            } else {
                executeResultMessage = new LoggingMessage("missing output");
                return CommandLine.ExitCode.USAGE;
            }
        }
    }
}
