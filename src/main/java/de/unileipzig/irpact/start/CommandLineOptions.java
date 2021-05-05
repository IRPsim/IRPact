package de.unileipzig.irpact.start;

import de.unileipzig.irpact.commons.log.LoggingMessage;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.util.log.IRPLogger;
import picocli.CommandLine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
@CommandLine.Command(
        name = "java -jar <IRPact.jar>",
        version = "IRPact version " + IRPact.VERSION_STRING
)
public class CommandLineOptions implements Callable<Integer> {

    private static final String TRUE1 = "1";
    private static final String FALSE0 = "0";

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CommandLineOptions.class);

    @CommandLine.Option(
            names = { "-?", "-h", "--help" },
            description = "Print help.",
            usageHelp = true
    )
    private boolean printHelp;

    @CommandLine.Option(
            names = { "-v", "--version" },
            description = "Print version information.",
            versionHelp = true
    )
    private boolean printVersion;

    @CommandLine.Option(
            names = { "-i", "--input" },
            description = "Set path to input file."
    )
    private String inputFile;
    private Path inputPath;

    @CommandLine.Option(
            names = { "-o", "--output" },
            description = "Set path to output file."
    )
    private String outputFile;
    private Path outputPath;

    @CommandLine.Option(
            names = { "--image" },
            description = "Set path to image output file."
    )
    private String imageFile;
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
            description = "Set path to log file and disable console logging."
    )
    private String logFile;
    private Path logPath;

    @CommandLine.Option(
            names = { "--logConsoleAndFile" },
            description = "Enable logging to console if '--logPath' is set."
    )
    private boolean logConsoleAndFile;

    @CommandLine.Option(
            names = { "--prefereCsv" },
            description = "Prefere csv files when loading."
    )
    private boolean prefereCsv;

    @CommandLine.Option(
            names = { "--irptools" },
            description = "Enables calling IRPtools, all arguments will be transmitted to IRPtools, IRPact is not called."
    )
    private boolean callIRPtools;

    @CommandLine.Option(
            names = { "--irptoolsUsage" },
            description = "Calls IRPtools help."
    )
    protected boolean printIRPtoolsHelp;

    @CommandLine.Option(
            names = { "--printInput" },
            description = "Saves input data to the specified file."
    )
    private String inputOutFile;
    private Path inputOutPath;

    @CommandLine.Option(
            names = { "--printInputCharset" },
            description = "Sets the charset for saving the input data."
    )
    private String inputOutCharsetName;
    private Charset inputOutCharset;

    //=========================
    //hidden
    //=========================

    @CommandLine.Option(
            names = { "--paramToSpec" },
            description = "Converts the parameter input format to the alternative format.",
            hidden = true
    )
    private String specOutputDir;
    private Path specOutputDirPath;

    @CommandLine.Option(
            names = { "--specToParam" },
            description = "Convert the alternative format to the parameter input format.",
            hidden = true
    )
    private String specInputDir;
    private Path specInputDirPath;

    @CommandLine.Option(
            names = { "--dataDir" },
            description = "Set path to data directory.",
            hidden = true
    )
    private String dataDir;
    private Path dataDirPath;

    @CommandLine.Option(
            names = { "--ignorePersistenceCheck" },
            description = "ignore load mismatches",
            hidden = true
    )
    private boolean ignorePersistenceCheck;

    @CommandLine.Option(
            names = { "--skipArgValidation" },
            description = "Skips command line validation.",
            hidden = true
    )
    private boolean skipArgValidation;

    //=========================
    //data
    //=========================

    private final String[] ARGS;
    private int errorCode;
    private boolean executed = false;
    private LoggingMessage executeResult;

    private boolean hasCustomInput = false;
    private boolean hasCallback = false;

    public CommandLineOptions(String[] args) {
        this.ARGS = args;
    }

    public int parse() {
        CommandLine cl = new CommandLine(this)
                .setUnmatchedArgumentsAllowed(true);
        errorCode = cl.execute(ARGS);
        return errorCode;
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
        if(isNotPrintHelpOrVersion() && !executed) {
            throw new IllegalStateException("not executed");
        }
    }

    public String[] getArgs() {
        return ARGS;
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
        return printHelp;
    }

    public boolean isPrintVersion() {
        return printVersion;
    }

    public boolean isPrintIRPtoolsHelp() {
        return printIRPtoolsHelp;
    }

    public boolean isPrintHelpOrVersion() {
        return isPrintHelp() || isPrintVersion() || isPrintIRPtoolsHelp();
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
        return logConsoleAndFile;
    }

    public boolean isPrefereCsv() {
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

    public boolean isCallIRPtools() {
        checkExecuted();
        return callIRPtools;
    }

    public boolean hasSpecOutputDirPath() {
        return getSpecOutputDirPath() != null;
    }
    public Path getSpecOutputDirPath() {
        checkExecuted();
        return specOutputDirPath;
    }

    public boolean hasSpecInputDirPath() {
        return getSpecInputDirPath() != null;
    }
    public Path getSpecInputDirPath() {
        checkExecuted();
        return specInputDirPath;
    }

    public boolean hasSpecialArgumentForIRPact() {
        checkExecuted();
        return hasSpecOutputDirPath()
                || hasSpecInputDirPath();
    }

    public Path getDataDirPath() {
        checkExecuted();
        return dataDirPath == null
                ? Paths.get("irpactdata")
                : dataDirPath;
    }

    public boolean isIgnorePersistenceCheck() {
        return ignorePersistenceCheck;
    }

    public int getMaxGamsNameLength() {
        return maxGamsNameLength;
    }

    public int getMinGamsPartLength() {
        return minGamsPartLength;
    }

    public boolean isEnableGamsNameTrimming() {
        return useGamsNameTrimming == 1;
    }

    public boolean hasInputOutPath() {
        return inputOutPath != null;
    }

    public Path getInputOutPath() {
        return inputOutPath;
    }

    public Charset getInputOutCharset() {
        if(inputOutCharsetName == null) {
            return StandardCharsets.UTF_8;
        } else {
            if(inputOutCharset == null) {
                inputOutCharset = Charset.forName(inputOutCharsetName);
            }
            return inputOutCharset;
        }
    }

    //=========================
    //execute
    //=========================

    @Override
    public Integer call() {
        executed = true;
        setup();
        return validate();
    }

    private void setup() {
        inputPath = tryGetPath(inputFile);
        outputPath = tryGetPath(outputFile);
        imagePath = tryGetPath(imageFile);
        logPath = tryGetPath(logFile);
        specOutputDirPath = tryGetPath(specOutputDir);
        specInputDirPath = tryGetPath(specInputDir);
        dataDirPath = tryGetPath(dataDir);
        inputOutPath = tryGetPath(inputOutFile);
    }

    private static Path tryGetPath(String pathStr) {
        return pathStr == null
                ? null
                : Paths.get(pathStr);
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

        int outputOk = validateInput();
        if(outputOk != CommandLine.ExitCode.OK) {
            return outputOk;
        }

        int consistencyOk = validateConsistency();
        if(consistencyOk != CommandLine.ExitCode.OK) {
            return consistencyOk;
        }

        return CommandLine.ExitCode.OK;
    }

    private boolean cancelValidation() {
        return skipArgValidation
                || printHelp
                || printVersion
                || callIRPtools
                || printIRPtoolsHelp;
    }

    private int validateInput() {
        int specifiedInput = countSpecified(inputPath, specInputDirPath);

        if(hasCustomInput()) {
            if(specifiedInput == 0b00) {
                return CommandLine.ExitCode.OK;
            } else {
                executeResult = new LoggingMessage("custom input and file input specified");
                return CommandLine.ExitCode.USAGE;
            }
        }

        switch (specifiedInput) {
            case 0b00:
                executeResult = new LoggingMessage("missing input");
                return CommandLine.ExitCode.USAGE;

            case 0b01:
                if(Files.notExists(inputPath)) {
                    executeResult = new LoggingMessage("input file '{}' not exists", inputPath);
                    return CommandLine.ExitCode.USAGE;
                }
                if(Files.isDirectory(inputPath)) {
                    executeResult = new LoggingMessage("input file '{}' is a directory", inputPath);
                    return CommandLine.ExitCode.USAGE;
                }
                break;

            case 0b10:
                if(Files.notExists(specInputDirPath)) {
                    executeResult = new LoggingMessage("input file '{}' not exists", specInputDirPath);
                    return CommandLine.ExitCode.USAGE;
                }
                if(Files.isDirectory(specInputDirPath)) {
                    executeResult = new LoggingMessage("input file '{}' is a directory", specInputDirPath);
                    return CommandLine.ExitCode.USAGE;
                }
                break;

            case 0b11:
                executeResult = new LoggingMessage("input file '{}' and spec input dir '{}' specified", inputPath, specInputDirPath);
                return CommandLine.ExitCode.USAGE;
        }

        return CommandLine.ExitCode.OK;
    }

    private int validateOutput() {
        int specifiedOutput = countSpecified(outputPath, specOutputDirPath);
        switch (specifiedOutput) {
            case 0b00:
                if(hasCallback()) {
                    return CommandLine.ExitCode.OK;
                }
                executeResult = new LoggingMessage("missing output");
                return CommandLine.ExitCode.USAGE;

            case 0b01:
                if(checkOutputExistence && Files.exists(outputPath)) {
                    executeResult = new LoggingMessage("output file '{}' already exists", outputPath);
                    return CommandLine.ExitCode.USAGE;
                }
                if(Files.isDirectory(outputPath)) {
                    executeResult = new LoggingMessage("output file '{}' is a directory", outputPath);
                    return CommandLine.ExitCode.USAGE;
                }
                break;

            case 0b10:
                if(!Files.isDirectory(specOutputDirPath)) {
                    executeResult = new LoggingMessage("output file '{}' is not a directory", specOutputDirPath);
                    return CommandLine.ExitCode.USAGE;
                }
                break;

            case 0b11:
                executeResult = new LoggingMessage("output file '{}' and spec output dir '{}' specified", outputPath, specOutputDirPath);
                return CommandLine.ExitCode.USAGE;
        }

        return CommandLine.ExitCode.OK;
    }

    private int validateConsistency() {
        return CommandLine.ExitCode.OK;
    }

    private static int countSpecified(Object obj0, Object obj1) {
        int out = 0;
        if(obj0 != null) out |= 0b01;
        if(obj1 != null) out |= 0b10;
        return out;
    }
}
