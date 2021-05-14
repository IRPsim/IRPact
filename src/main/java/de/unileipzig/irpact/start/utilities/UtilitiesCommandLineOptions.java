package de.unileipzig.irpact.start.utilities;

import de.unileipzig.irpact.commons.log.LoggingMessage;
import de.unileipzig.irpact.commons.util.AbstractCommandLineOptions;
import de.unileipzig.irpact.commons.util.Args;
import de.unileipzig.irpact.commons.util.MapResourceBundle;
import de.unileipzig.irpact.start.irpact.IRPact;
import picocli.CommandLine;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("RedundantIfStatement")
@CommandLine.Command(
        name = "java -jar <IRPact.jar> --utilities",
        version = {IRPact.CL_NAME, IRPact.CL_VERSION, IRPact.CL_COPY}
)
public final class UtilitiesCommandLineOptions extends AbstractCommandLineOptions {

    private static ResourceBundle fallback;

    private static synchronized ResourceBundle getFallbackBundle() {
        if(fallback == null) {
            MapResourceBundle bundle = new MapResourceBundle();

            bundle.put("printHelp", "Print help.");
            bundle.put("printVersion", "Print version information.");
            bundle.put("storeInputPath", "Saves input data to the specified file.");
            bundle.put("inputOutCharset", "Sets the charset for saving the input data.");
            bundle.put("spec2paramPaths", "Converts the specification format to the parameter format.");
            bundle.put("param2specPaths", "Converts the parameter format to the specification format.");
            bundle.put("rExe", "Path to R. Type (e.g. R or RScript) depends on selected function.");
            bundle.put("rScript", "Path to R script file.");
            bundle.put("rInput", "Input file for R. Type depends on selected function.");
            bundle.put("rOutput", "Output file for R. Type depends on selected function.");
            bundle.put("printCumulativeAdoptions", "Print cumulative adoptions.");
            bundle.put("milieus", "Milieus to print. Can be used with '--printCumulativeAdoptions'.");

            bundle.put("testCl", "For testing the command line.");

            fallback = bundle;
        }
        return fallback;
    }

    //=========================
    //options
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
            names = { "--printInput" },
            descriptionKey = "storeInputPath",
            converter = PathConverter.class
    )
    private Path storeInputPath;

    @CommandLine.Option(
            names = { "--printInputCharset" },
            descriptionKey = "inputOutCharset",
            converter = CharsetConverter.class
    )
    private Charset inputOutCharset;

    @CommandLine.Option(
            names = { "--spec2param" },
            arity = "2",
            descriptionKey = "spec2paramPaths",
            converter = PathConverter.class
    )
    private Path[] spec2paramPaths;

    @CommandLine.Option(
            names = { "--param2spec" },
            arity = "2",
            descriptionKey = "param2specPaths",
            converter = PathConverter.class
    )
    private Path[] param2specPaths;

    //=========================
    //R
    //=========================

    @CommandLine.Option(
            names = { "-R" },
            descriptionKey = "rExe",
            converter = PathConverter.class
    )
    private Path rExe;

    @CommandLine.Option(
            names = { "-rscript" },
            descriptionKey = "rScript",
            converter = PathConverter.class
    )
    private Path rScript;

    @CommandLine.Option(
            names = { "--rinput" },
            descriptionKey = "rInput",
            converter = PathConverter.class
    )
    private Path rInput;

    @CommandLine.Option(
            names = { "--routput" },
            descriptionKey = "rOutput",
            converter = PathConverter.class
    )
    private Path rOutput;

    @CommandLine.Option(
            names = { "--printCumulativeAdoptions" },
            descriptionKey = "printCumulativeAdoptions"
    )
    private boolean printCumulativeAdoptions;

    @CommandLine.Option(
            names = { "--milieus" },
            descriptionKey = "milieus",
            arity = "1..*",
            split = ","
    )
    private String[] milieus;

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

    public UtilitiesCommandLineOptions(String... args) {
        super(args);
    }

    public UtilitiesCommandLineOptions(Args args) {
        super(args);
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
    //getter
    //=========================

    public boolean isPrintHelp() {
        return printHelp;
    }

    public boolean isPrintVersion() {
        return printVersion;
    }

    public boolean isPrintHelpOrVersion() {
        return isPrintHelp() || isPrintVersion();
    }

    public Path[] getParam2specPaths() {
        return param2specPaths;
    }

    public Path getRExe() {
        checkExecuted();
        return rExe;
    }

    public Path getRScript() {
        return rScript;
    }

    public Path getRInput() {
        checkExecuted();
        return rInput;
    }

    public Path getROutput() {
        checkExecuted();
        return rOutput;
    }

    public boolean isPrintCumulativeAdoptions() {
        checkExecuted();
        return printCumulativeAdoptions;
    }

    public String[] getMilieus() {
        checkExecuted();
        return milieus;
    }

    public boolean isTestCl() {
        checkExecuted();
        return testCl;
    }

    //=========================
    //rest
    //=========================

    @Override
    public Integer call() throws Exception {
        if(executed) {
            throw new IllegalStateException("already executed");
        }
        executed = true;
        return validate();
    }

    private int validate() {
        int rscriptOk = validateRScript();
        if(rscriptOk != CommandLine.ExitCode.OK) {
            return rscriptOk;
        }

        return CommandLine.ExitCode.OK;
    }

    private int hasValidRScriptInputAndOutput() {
        if(rExe == null) {
            executeResultMessage = new LoggingMessage("missing RScript execution path");
            return CommandLine.ExitCode.USAGE;
        }

        if(Files.notExists(rExe)) {
            executeResultMessage = new LoggingMessage("'{}' not found", rExe);
            return CommandLine.ExitCode.USAGE;
        }

        if(Files.notExists(rScript)) {
            executeResultMessage = new LoggingMessage("'{}' not found", rScript);
            return CommandLine.ExitCode.USAGE;
        }

        if(rInput == null) {
            executeResultMessage = new LoggingMessage("missing input for RScript");
            return CommandLine.ExitCode.USAGE;
        }

        if(Files.notExists(rInput)) {
            executeResultMessage = new LoggingMessage("'{}' not found", rInput);
            return CommandLine.ExitCode.USAGE;
        }

        if(rOutput == null) {
            executeResultMessage = new LoggingMessage("missing output for RScript");
            return CommandLine.ExitCode.USAGE;
        }

        return CommandLine.ExitCode.OK;
    }

    private int validateRScript() {
        if(printCumulativeAdoptions) {
            int rscriptOk = hasValidRScriptInputAndOutput();
            if(rscriptOk != CommandLine.ExitCode.OK) {
                return rscriptOk;
            }
        }

        return CommandLine.ExitCode.OK;
    }
}
