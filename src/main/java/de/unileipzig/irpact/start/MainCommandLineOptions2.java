package de.unileipzig.irpact.start;

import de.unileipzig.irpact.commons.log.LoggingMessage;
import de.unileipzig.irpact.commons.util.AbstractCommandLineOptions;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
@CommandLine.Command(
        name = "java -jar <IRPact.jar>",
        version = {"IRPact", "Version " + IRPact.VERSION_STRING, "(c) 2019-2021"}
)
public final class MainCommandLineOptions2 extends AbstractCommandLineOptions {

    private static final Path EXEC_DIR = Paths.get(".");

    private static final String TRUE1 = "1";
    private static final String FALSE0 = "0";

    //=========================
    //options
    //=========================

    @CommandLine.Option(
            names = { "-?", "-h", "--help" },
            description = "Print help."
    )
    private boolean printHelp;

    @CommandLine.Option(
            names = { "-v", "--version" },
            description = "Print version information."
    )
    private boolean printVersion;

    @CommandLine.Option(
            names = { "-i", "--input" },
            description = "Set path to input file.",
            converter = PathConverter.class
    )
    private Path inputPath;

    @CommandLine.Option(
            names = { "-o", "--output" },
            description = "Set path to output file.",
            converter = PathConverter.class
    )
    private Path outputPath;

    @CommandLine.Option(
            names = { "--image" },
            description = "Set path to image output file.",
            converter = PathConverter.class
    )
    private Path imagePath;

    @CommandLine.Option(
            names = { "--noSimulation" },
            description = "Disable simulation, run initialization only."
    )
    private boolean noSimulation;

    @CommandLine.Option(
            names = { "--useGamsNameTrimming" },
            defaultValue = FALSE0,
            description = "Enables (1) or disables (0) gams name trimming. Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing."
    )
    private int useGamsNameTrimming;

    @CommandLine.Option(
            names = "--maxGamsNameLength",
            defaultValue = DefinitionMapper.IGNORE_NAME_LENGTH_AS_STR,
            description = "Max length for gams field names. Value -1 disables this option. Max length for gams is 63. Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing."
    )
    private int maxGamsNameLength;

    @CommandLine.Option(
            names = "--minGamsPartLength",
            defaultValue = DefinitionMapper.MIN_PART_LENGTH_AS_STR,
            description = "Min length for gams field name parts (tags). Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing."
    )
    private int minGamsPartLength;

    @CommandLine.Option(
            names = { "--checkOutputExistence" },
            description = "Checks if output already exists."
    )
    private boolean checkOutputExistence;

    @CommandLine.Option(
            names = { "--logPath" },
            description = "Set path to log file and disable console logging.",
            converter = PathConverter.class
    )
    private Path logPath;

    @CommandLine.Option(
            names = { "--logConsoleAndFile" },
            description = "Enable console logging if '--logPath' is set."
    )
    private boolean logConsoleAndFile;

    @CommandLine.Option(
            names = { "--prefereCsv" },
            description = "Prefere csv files when loading."
    )
    private boolean prefereCsv;

    @CommandLine.Option(
            names = { "--dataDir" },
            description = "Set path to data directory. If not set, the execution directory will be used.",
            converter = PathConverter.class
    )
    private Path dataDirPath;

    @CommandLine.Option(
            names = { "--ignorePersistenceCheck" },
            description = "If set, checksum mismatches are ignore during restoration. Use this option only if you know what you are doing."
    )
    private boolean ignorePersistenceCheck;

    @CommandLine.Option(
            names = { "--skipArgValidation" },
            description = "Skips command line validation. Use this option only if you know what you are doing."
    )
    private boolean skipArgValidation;

    //=========================
    //sub calls
    //=========================

    @CommandLine.Option(
            names = { "--irptools" },
            description = "Enables calling IRPtools, all arguments will be transmitted to IRPtools, IRPact is not called."
    )
    private boolean callIrptools;

    @CommandLine.Option(
            names = { "--irptoolsUsage" },
            description = "Calls IRPtools help."
    )
    protected boolean printIrptoolsHelp;

    @CommandLine.Option(
            names = { "--utilities" },
            description = "Enables calling Utilities, all arguments will be transmitted to Utilities, IRPact is not called."
    )
    private boolean callUtilities;

    @CommandLine.Option(
            names = { "--utilitiesUsage" },
            description = "Calls Utilities help."
    )
    protected boolean printUtilitiesHelp;

    //=========================
    //rest
    //=========================

    private boolean executed = false;
    private LoggingMessage executeResult;

    private boolean hasCustomInput = false;
    private boolean hasCallback = false;

    public MainCommandLineOptions2(String[] args) {
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

    public int getErrorCode() {
        checkExecuted();
        return errorCode;
    }

    public boolean wasExecuted() {
        return executed;
    }

    public boolean hasExecuteResultMessage() {
        return executeResult != null;
    }

    public LoggingMessage getExecuteResultMessage() {
        return executeResult;
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
                || isPrintVersion()
                || isPrintIrptoolsHelp()
                || isPrintUtilitiesHelp();
    }

    public boolean isNotPrintHelpOrVersion() {
        return !isPrintHelpOrVersion();
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

    public boolean isIgnorePersistenceCheck() {
        checkExecuted();
        return ignorePersistenceCheck;
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
                executeResult = new LoggingMessage("custom input and file input specified");
                return CommandLine.ExitCode.USAGE;
            } else {
                return CommandLine.ExitCode.OK;
            }
        } else {
            if(hasCustomInput()) {
                return CommandLine.ExitCode.OK;
            } else {
                executeResult = new LoggingMessage("missing input");
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
                executeResult = new LoggingMessage("missing output");
                return CommandLine.ExitCode.USAGE;
            }
        }
    }
}
