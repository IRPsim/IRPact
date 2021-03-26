package de.unileipzig.irpact.start;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.util.log.IRPLogger;
import picocli.CommandLine;

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CommandLineOptions.class);

    @CommandLine.Option(
            names = { "-?", "-h", "--help" },
            description = "print help",
            usageHelp = true
    )
    private boolean printHelp;

    @CommandLine.Option(
            names = { "-v", "--version" },
            description = "print version information",
            versionHelp = true
    )
    private boolean printVersion;

    @CommandLine.Option(
            names = { "-i", "--input" },
            description = "path to input file"
    )
    private String inputFile;
    private Path inputPath;

    @CommandLine.Option(
            names = { "-o", "--output" },
            description = "path to output file"
    )
    private String outputFile;
    private Path outputPath;

    @CommandLine.Option(
            names = { "--image" },
            description = "path to image output file"
    )
    private String imageFile;
    private Path imagePath;

    @CommandLine.Option(
            names = { "--noSimulation" },
            description = "disable simulation"
    )
    private boolean noSimulation;

    @CommandLine.Option(
            names = { "--useGamsNameTrimming" },
            defaultValue = "0",
            description = "Enables (1) or disables (0) gams name trimming. Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing."
    )
    private int useGamsNameTrimming;

    @CommandLine.Option(
            names = "--maxGamsNameLength",
            defaultValue = DefinitionMapper.MAX_GAMS_NAME_LENGTH_AS_STR,
            description = "Max length for gams field names. Value -1 disables this option. Max length for gams is 63. Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing."
    )
    private int maxGamsNameLength;

    @CommandLine.Option(
            names = "--minGamsPartLength",
            defaultValue = DefinitionMapper.MIN_PART_LENGTH_AS_STR,
            description = "Min length for gams field name parts (tags). Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing."
    )
    private int minGamsPartLength;

    //=========================
    //hidden
    //=========================

    @CommandLine.Option(
            names = { "--logPath" },
            description = "set path to log file, disables console logging",
            hidden = true
    )
    private String logFile;
    private Path logPath;

    @CommandLine.Option(
            names = { "--irptools" },
            description = "calls IRPtools, all arguments will be transmitted, IRPact is not called",
            hidden = true
    )
    private boolean callIRPtools;

    @CommandLine.Option(
            names = { "--paramToSpec" },
            description = "converts the parameter input to the alternative format",
            hidden = true
    )
    private String specOutputDir;
    private Path specOutputDirPath;

    @CommandLine.Option(
            names = { "--specToParam" },
            description = "converts the alternative format to the parameter input",
            hidden = true
    )
    private String specInputDir;
    private Path specInputDirPath;

    @CommandLine.Option(
            names = { "--dataDir" },
            description = "path to data directory",
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

    //=========================
    //dev
    //=========================

    @CommandLine.Option(
            names = { "--testMode" },
            description = "Enables test mode and disables command-line validation.",
            hidden = true
    )
    private boolean testMode;

    //=========================
    //data
    //=========================

    private final String[] ARGS;
    private int errorCode;
    private boolean executed = false;

    public CommandLineOptions(String[] args) {
        this.ARGS = args;
    }

    public int parse() {
        CommandLine cl = new CommandLine(this)
                .setUnmatchedArgumentsAllowed(true);
        errorCode = cl.execute(ARGS);
        return errorCode;
    }

    protected void checkExecuted() {
        if(!executed) {
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

    //=========================
    //options
    //=========================

    public boolean isPrintHelp() {
        return printHelp;
    }

    public boolean isPrintVersion() {
        return printVersion;
    }

    public Path getInputPath() {
        checkExecuted();
        return inputPath;
    }

    public Path getOutputPath() {
        checkExecuted();
        return outputPath;
    }

    public Path getImagePath() {
        checkExecuted();
        return imagePath;
    }

    public boolean hasImagePath() {
        checkExecuted();
        return imagePath != null;
    }

    public boolean hasLogPath() {
        checkExecuted();
        return logPath != null;
    }

    public Path getLogPath() {
        checkExecuted();
        return logPath;
    }

    public boolean isSimulation() {
        checkExecuted();
        return !noSimulation;
    }

    public boolean isNoSimulation() {
        checkExecuted();
        return noSimulation;
    }

    public boolean isCallIRPtools() {
        checkExecuted();
        return callIRPtools;
    }

    public boolean hasSpecOutputDirPath() {
        checkExecuted();
        return specOutputDirPath != null;
    }

    public Path getSpecOutputDirPath() {
        checkExecuted();
        return specOutputDirPath;
    }

    public boolean hasSpecInputDirPath() {
        checkExecuted();
        return specInputDirPath != null;
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

    //=========================
    //execute
    //=========================

    @Override
    public Integer call() {
        executed = true;

        if(printVersion || printHelp) {
            return CommandLine.ExitCode.OK;
        }

        if(callIRPtools) {
            return CommandLine.ExitCode.OK;
        }

        if(testMode) {
            return CommandLine.ExitCode.OK;
        }

        if(dataDir != null) {
            dataDirPath = Paths.get(dataDir);
        }

        if(handleInput()) {
            if(Files.notExists(inputPath)) {
                LOGGER.error("input file not found: {}", inputPath);
                return CommandLine.ExitCode.SOFTWARE;
            }
            if(!handleOutput() && !handleSpecOutput() && isSimulation()) {
                LOGGER.error("output file missing");
                return CommandLine.ExitCode.USAGE;
            }
        }
        else if(handleSpecInput()) {
            if(Files.notExists(specInputDirPath)) {
                LOGGER.error("input dir not found: {}", specInputDirPath);
                return CommandLine.ExitCode.SOFTWARE;
            }
            if(!handleOutput() && isSimulation()) {
                LOGGER.error("output file missing");
                return CommandLine.ExitCode.USAGE;
            }
        }
        else {
            LOGGER.error("input file missing");
            return CommandLine.ExitCode.USAGE;
        }

        if(imageFile != null) {
            imagePath = Paths.get(imageFile);
        }

        if(logFile != null) {
            logPath = Paths.get(logFile);
        }

        return CommandLine.ExitCode.OK;
    }

    private boolean handleInput() {
        if(inputPath != null) {
            return true;
        }
        if(inputFile != null) {
            inputPath = Paths.get(inputFile);
            return true;
        }
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean handleOutput() {
        if(outputPath != null) {
            return true;
        }
        if(outputFile != null) {
            outputPath = Paths.get(outputFile);
            return true;
        }
        return false;
    }

    private boolean handleSpecInput() {
        if(specInputDirPath != null) {
            return true;
        }
        if(specInputDir != null) {
            specInputDirPath = Paths.get(specInputDir);
            return true;
        }
        return false;
    }

    private boolean handleSpecOutput() {
        if(specOutputDirPath != null) {
            return true;
        }
        if(specOutputDir != null) {
            specOutputDirPath = Paths.get(specOutputDir);
            return true;
        }
        return false;
    }
}
