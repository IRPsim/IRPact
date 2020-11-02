package de.unileipzig.irpact.start.irpact2;

import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * @author Daniel Abitz
 */
public class IRPactCommandLine implements Callable<Integer> {

    @CommandLine.Option(
            names = { "-i", "--input" },
            description = "path to input file"
    )
    private String inputFile;

    @CommandLine.Option(
            names = { "-o", "--output" },
            description = "path to output file"
    )
    private String outputFile;

    private String[] args;
    private int exitCode;

    public IRPactCommandLine(String[] args) {
        this.args = args;
        CommandLine commandLine = new CommandLine(this);
        exitCode = commandLine.execute(args);
    }

    @Override
    public Integer call() {
        if(inputFile == null) {
            throw new NullPointerException("input file missing");
        }
        if(outputFile == null) {
            throw new NullPointerException("output file missing");
        }
        return CommandLine.ExitCode.OK;
    }

    public Path getInputPath() {
        return Paths.get(inputFile);
    }

    public Path getOutputPath() {
        return Paths.get(outputFile);
    }

    public int getExitCode() {
        return exitCode;
    }
}
