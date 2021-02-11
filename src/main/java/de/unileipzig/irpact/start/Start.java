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
@SuppressWarnings("unused")
@CommandLine.Command(
        name = "IRPact"
)
public class Start implements Callable<Integer> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Start.class);

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
            description = "path to image file"
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
            hidden = true
    )
    private String logFile;
    private Path logPath;

    @CommandLine.Option(
            names = { "--irptools" },
            hidden = true
    )
    private boolean callIRPtools;

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

    public boolean hasLogPath() {
        return logPath != null;
    }

    public Path getLogPath() {
        return logPath;
    }

    public boolean isNoSimulation() {
        return noSimulation;
    }

    public boolean isCallIRPtools() {
        return callIRPtools;
    }

    @Override
    public Integer call() {
        if(callIRPtools) {
            return CommandLine.ExitCode.OK;
        }

        if(inputFile == null) {
            LOGGER.error("input file missing");
            return CommandLine.ExitCode.USAGE;
        }
        inputPath = Paths.get(inputFile);
        if(Files.notExists(inputPath)) {
            LOGGER.error("input file not found: {}", inputPath);
            return CommandLine.ExitCode.SOFTWARE;
        }

        if(outputFile == null) {
            if(!noSimulation) {
                LOGGER.error("output file missing");
                return CommandLine.ExitCode.USAGE;
            }
        } else {
            outputPath = Paths.get(outputFile);
        }

        if(imageFile != null) {
            imagePath = Paths.get(imageFile);
        }

        if(logFile != null) {
            logPath = Paths.get(logFile);
        }

        return CommandLine.ExitCode.OK;
    }

    public static void main(String[] args) {
        IRPLogging.initConsole();
        Start start = new Start(args);
        CommandLine cmdLine = new CommandLine(start)
                .setUnmatchedArgumentsAllowed(true);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            Preloader irpact = new Preloader(start);
            try {
                irpact.start();
            } catch (Throwable t) {
                LOGGER.error("Start failed", t);
            }
        } else {
            System.exit(CommandLine.ExitCode.USAGE);
        }
    }
/*
ALT:
Logback.setupSystemOutAndErr();
OptActMain.main(args);
*/
}
