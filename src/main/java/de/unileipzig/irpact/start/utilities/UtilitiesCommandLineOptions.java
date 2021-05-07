package de.unileipzig.irpact.start.utilities;

import de.unileipzig.irpact.commons.util.AbstractCommandLineOptions;
import picocli.CommandLine;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class UtilitiesCommandLineOptions extends AbstractCommandLineOptions {

    //=========================
    //options
    //=========================

    @CommandLine.Option(
            names = { "--printInput" },
            description = "Saves input data to the specified file.",
            converter = PathConverter.class
    )
    private Path inputOutPath;

    @CommandLine.Option(
            names = { "--printInputCharset" },
            description = "Sets the charset for saving the input data.",
            converter = CharsetConverter.class
    )
    private Charset inputOutCharset;

    @CommandLine.Option(
            names = { "--spec2param" },
            arity = "2",
            description = "Converts the specification format to the parameter format.",
            converter = PathConverter.class
    )
    private List<Path> spec2paramPaths;

    @CommandLine.Option(
            names = { "--param2spec" },
            arity = "2",
            description = "Converts the parameter format to the specification format.",
            converter = PathConverter.class
    )
    private List<Path> param2specPaths;

    //=========================
    //rest
    //=========================

    public UtilitiesCommandLineOptions(String[] args) {
        super(args);
    }

    @Override
    public Integer call() throws Exception {
        return CommandLine.ExitCode.OK;
    }
}
