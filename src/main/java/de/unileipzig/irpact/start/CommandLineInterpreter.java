package de.unileipzig.irpact.start;

import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * Handles and validates commandline arguments.
 *
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
@CommandLine.Command(
        name = "IRPact",
        description = "todo"
)
public final class CommandLineInterpreter implements Callable<Integer> {

    //=========================
    //IRPact
    //=========================

    @CommandLine.Option(
            names = { "-i", "--input" },
            description = "Path to input file"
    )
    private String inputPathString;
    private Path inputPath;

    @CommandLine.Option(
            names = { "-o", "--output" },
            description = "Path to output file"
    )
    private String outputPathString;
    private Path outputPath;

    //=========================
    //IRPtools
    //=========================

    @CommandLine.Option(
            names = {"--tools"},
            description = "Starts IRPtools, ignores -i and -o."
    )
    private boolean irptools;

    @CommandLine.Option(
            names = { "--inputgms" },
            description = "Path to input.gms"
    )
    private String inputgmsPathString;
    private Path inputgmsPath;

    @CommandLine.Option(
            names = { "--uiinput" },
            description = "Path to ui-input.edn"
    )
    private String uiinputPathString;
    private Path uiinputPath;

    @CommandLine.Option(
            names = { "--uiinputdelta" },
            description = "Path to ui-input-delta.edn"
    )
    private String uiinputdeltaPathString;
    private Path uiinputdeltaPath;

    @CommandLine.Option(
            names = { "--outputgms" },
            description = "Path to output.gms"
    )
    private String outputgmsPathString;
    private Path outputgmsPath;

    @CommandLine.Option(
            names = { "--uioutput" },
            description = "Path to ui-output.edn"
    )
    private String uioutputPathString;
    private Path uioutputPath;

    //=========================
    //CommandLineInterpreter
    //=========================

    private String[] args;
    private int exitCode;
    private Throwable cause;

    public CommandLineInterpreter(String[] args) {
        this.args = args;
        this.exitCode = new CommandLine(this).execute(args);
    }

    public boolean startIRPtools() {
        return irptools;
    }

    public boolean startIRPact() {
        return irptools;
    }

    public boolean isOk() {
        return getExitCode() == CommandLine.ExitCode.OK;
    }

    public boolean isErr() {
        return getExitCode() != CommandLine.ExitCode.OK;
    }

    public int getExitCode() {
        return exitCode;
    }

    public Throwable getCause() {
        return cause;
    }

    public Path getInputPath() {
        return inputPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public Path getInputgmsPath() {
        return inputgmsPath;
    }

    public Path getUiinputPath() {
        return uiinputPath;
    }

    public Path getUiinputdeltaPath() {
        return uiinputdeltaPath;
    }

    public Path getOutputgmsPath() {
        return outputgmsPath;
    }

    public Path getUioutputPath() {
        return uioutputPath;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public Integer call() {
        return irptools
                ? validateIRPTools()
                : validateIRPact();
    }

    private int validateIRPTools() {
        PathResult inputgmsPathResult = getPathToFile(inputgmsPathString);
        if(inputgmsPathResult.isErr()) {
            cause = inputgmsPathResult.cause;
            return CommandLine.ExitCode.USAGE;
        }
        if(inputgmsPathResult.hasValue()) {
            inputgmsPath = inputgmsPathResult.path;
        }

        PathResult uiinputPathResult = getPathToFile(uiinputPathString);
        if(uiinputPathResult.isErr()) {
            cause = uiinputPathResult.cause;
            return CommandLine.ExitCode.USAGE;
        }
        if(uiinputPathResult.hasValue()) {
            uiinputPath = uiinputPathResult.path;
        }

        PathResult uiinputdeltaPathResult = getPathToFile(uiinputdeltaPathString);
        if(uiinputdeltaPathResult.isErr()) {
            cause = uiinputdeltaPathResult.cause;
            return CommandLine.ExitCode.USAGE;
        }
        if(uiinputdeltaPathResult.hasValue()) {
            uiinputdeltaPath = uiinputdeltaPathResult.path;
        }

        PathResult outputgmsPathResult = getPathToFile(outputgmsPathString);
        if(outputgmsPathResult.isErr()) {
            cause = outputgmsPathResult.cause;
            return CommandLine.ExitCode.USAGE;
        }
        if(outputgmsPathResult.hasValue()) {
            outputgmsPath = outputgmsPathResult.path;
        }

        PathResult uioutputPathResult = getPathToFile(uioutputPathString);
        if(uioutputPathResult.isErr()) {
            cause = uioutputPathResult.cause;
            return CommandLine.ExitCode.USAGE;
        }
        if(uioutputPathResult.hasValue()) {
            uioutputPath = uioutputPathResult.path;
        }

        return CommandLine.ExitCode.OK;
    }

    private int validateIRPact() {
        PathResult inputPathResult = getValidPathToFile("inputFile", inputPathString, true);
        if(inputPathResult.isErr()) {
            cause = inputPathResult.cause;
            return CommandLine.ExitCode.USAGE;
        } else {
            inputPath = inputPathResult.path;
        }

        PathResult outputPathResult = getValidPathToFile("outputFile", outputPathString, false);
        if(outputPathResult.isErr()) {
            cause = outputPathResult.cause;
            return CommandLine.ExitCode.USAGE;
        } else {
            outputPath = outputPathResult.path;
        }

        return CommandLine.ExitCode.OK;
    }

    //=========================
    //util
    //=========================

    private static PathResult getValidPathToFile(
            String pathIdentifier,
            String pathStr,
            boolean mustExist) {
        if(pathStr == null) {
            return new PathResult(new NullPointerException(pathIdentifier));
        }

        Path path = Paths.get(pathStr);
        if(mustExist && Files.notExists(path)) {
            return new PathResult(new NoSuchFileException(path.toString()));
        }

        if(Files.isDirectory(path)) {
            return new PathResult(new IOException("is directory: '" + path.toString() + "'"));
        }

        return new PathResult(path);
    }

    private static PathResult getPathToFile(String pathStr) {
        if(pathStr == null) {
            return PathResult.EMPTY;
        }

        Path path = Paths.get(pathStr);
        if(Files.isDirectory(path)) {
            return new PathResult(new IOException("is directory: '" + path.toString() + "'"));
        }

        return new PathResult(path);
    }

    private static class PathResult {

        private static final PathResult EMPTY = new PathResult();

        private Path path;
        private Throwable cause;

        private PathResult() {
            this(null, null);
        }

        private PathResult(Throwable cause) {
            this(null, cause);
        }

        private PathResult(Path path) {
            this(path, null);
        }

        private PathResult(Path path, Throwable cause) {
            this.path = path;
            this.cause = cause;
        }

        private boolean isEmpty() {
            return isOk() && path == null;
        }

        private boolean hasValue() {
            return isOk() && !isEmpty();
        }

        private boolean isOk() {
            return cause == null;
        }

        private boolean isErr() {
            return cause != null;
        }
    }
}
