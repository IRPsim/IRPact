package de.unileipzig.irpact.start;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * Starts IRPact.
 *
 * @author Daniel Abitz
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
@CommandLine.Command(
        name = "java -jar <IRPact.jar>",
        version = "IRPact version " + IRPact.VERSION_STRING
)
public class Start implements Callable<Integer> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Start.class);

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
            names = { "--testMode" },
            description = "test mode",
            hidden = true
    )
    private boolean testMode;

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
    //data
    //=========================

    private final String[] ARGS;

    public Start(String[] args) {
        this.ARGS = args;
    }

    public String[] getArgs() {
        return ARGS;
    }

    public Path getInputPath() {
        return inputPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public Path getImagePath() {
        return imagePath;
    }

    public boolean hasImagePath() {
        return imagePath != null;
    }

    public boolean hasLogPath() {
        return logPath != null;
    }

    public Path getLogPath() {
        return logPath;
    }

    public boolean isSimulation() {
        return !noSimulation;
    }

    public boolean isNoSimulation() {
        return noSimulation;
    }

    public boolean isCallIRPtools() {
        return callIRPtools;
    }

    public boolean hasSpecOutputDirPath() {
        return specOutputDirPath != null;
    }

    public Path getSpecOutputDirPath() {
        return specOutputDirPath;
    }

    public boolean hasSpecInputDirPath() {
        return specInputDirPath != null;
    }

    public Path getSpecInputDirPath() {
        return specInputDirPath;
    }

    public boolean hasSpecialArgumentForIRPact() {
        return hasSpecOutputDirPath()
                || hasSpecInputDirPath();
    }

    public Path getDataDirPath() {
        return dataDirPath == null
                ? Paths.get("irpactdata")
                : dataDirPath;
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

    public boolean isIgnorePersistenceCheck() {
        return ignorePersistenceCheck;
    }

    @Override
    public Integer call() {
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

    public static Start testMode() {
        IRPLogging.initConsole();
        String[] args = new String[]{"--testMode"};
        Start start = new Start(args);
        CommandLine cmdLine = new CommandLine(start)
                .setUnmatchedArgumentsAllowed(true);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            return start;
        } else {
            throw new RuntimeException("exit code: " + CommandLine.ExitCode.USAGE);
        }
    }

    //TODO listener access einfuegen fuer gui
    public static void start(String[] args) {
        IRPLogging.initConsole();
        Start start = new Start(args);
        CommandLine cmdLine = new CommandLine(start)
                .setUnmatchedArgumentsAllowed(true);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            if(start.printHelp || start.printVersion) {
                return;
            }
            Preloader loader = new Preloader(start);
            try {
                loader.start();
            } catch (Throwable t) {
                LOGGER.error("Start failed with uncaught exception", t);
                System.exit(CommandLine.ExitCode.SOFTWARE);
            }
        } else {
            System.exit(CommandLine.ExitCode.USAGE);
        }
    }

    public static void main(String[] args) {
        start(args);
    }
/*
ALT:
Logback.setupSystemOutAndErr();
OptActMain.main(args);
*/
}
