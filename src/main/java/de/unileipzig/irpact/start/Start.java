package de.unileipzig.irpact.start;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.start.optact.OptActMain;
import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irptools.util.log.IRPLogger;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Starts IRPact.
 *
 * @author Daniel Abitz
 */
public class Start implements Callable<Integer> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Start.class);

    @CommandLine.Option(
            names = { "-i", "--input" },
            description = "path to input file"
    )
    @SuppressWarnings("unused")
    private String inputFile;

    @CommandLine.Option(
            names = { "-o", "--output" },
            description = "path to output file"
    )
    @SuppressWarnings("unused")
    private String outputFile;

    @CommandLine.Option(
            names = { "--image" },
            description = "path to image file"
    )
    @SuppressWarnings("unused")
    private String imageFile;

    @CommandLine.Option(
            names = { "--noSimulation" },
            description = "disable simulation"
    )
    @SuppressWarnings("unused")
    private boolean noSimulation;

    private Path inputPath;
    private Path outputPath;
    private Path imagePath;

    public Start() {
        IRPLogging.initConsole();
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

    public boolean isNoSimulation() {
        return noSimulation;
    }

    @Override
    public Integer call() {
        if(inputFile == null) {
            LOGGER.error("input file missing");
            return CommandLine.ExitCode.USAGE;
        }
        inputPath = Paths.get(inputFile);
        LOGGER.debug("input file: {}", inputFile);
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
            LOGGER.debug("output file: {}", outputPath);
        }

        if(imageFile != null) {
            imagePath = Paths.get(imageFile);
            LOGGER.debug("image file: {}", imagePath);
        }

        if(noSimulation) {
            LOGGER.debug("simulation disabled");
        } else {
            LOGGER.debug("simulation enabled");
        }

        return CommandLine.ExitCode.OK;
    }

    public static void main(String[] args) {
        Start start = new Start();
        CommandLine cmdLine = new CommandLine(start);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            IRPact irpact = new IRPact(start);
            irpact.start();
        } else {
            System.exit(CommandLine.ExitCode.USAGE);
        }
    }
}
