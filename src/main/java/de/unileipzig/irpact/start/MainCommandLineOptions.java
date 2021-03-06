package de.unileipzig.irpact.start;

import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.AbstractCommandLineOptions;
import de.unileipzig.irpact.commons.resource.MapResourceBundle;
import de.unileipzig.irpact.core.logging.IRPLoggingMessage;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.executors.IRPactExecutors;
import de.unileipzig.irpact.util.R.RscriptEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
@CommandLine.Command(
        name = "java -jar <IRPact.jar>",
        version = {IRPact.CL_NAME, IRPact.CL_VERSION, IRPact.CL_COPY}
)
public class MainCommandLineOptions extends AbstractCommandLineOptions {

    private static ResourceBundle fallback;

    private static synchronized ResourceBundle getFallbackBundle() {
        if(fallback == null) {
            MapResourceBundle bundle = new MapResourceBundle();

            bundle.put("printHelp", "Print help.");
            bundle.put("printVersion", "Print version information.");
            bundle.put("runMode", "The mode in which the program is to be executed. Default value is ${DEFAULT-VALUE}. Currently supported are: '0: normal execution', '1: minimal simulation (only system agents)'. '100: test mode (creates dummy output)', '666: error mode (guaranteed exception)'");
            bundle.put("filterError", "If set, errors are logged only to System.err.");
            bundle.put("inputPath", "Set path to input file.");
            bundle.put("outputPath", "Set path to output file.");
            bundle.put("outputDir", "Set path to output directory. If not set, the output file directory is used.");
            bundle.put("downloadDir", "Set path to download directory. If not set, the '" + IRPact.DOWNLOAD_DIR_NAME + "' directory in the output directory is used.");
            bundle.put("imagePath", "Set path to image output file. Without '--noSimulation' the post-simulation network is printed.");
            bundle.put("noSimulation", "Disables everything except initialization. Combined with '--image' the initial network is printed.");
            bundle.put("checkOutputExistence", "Checks if the output file already exists. If it does, the program will be cancelled. This option is used to ensure that data is not overwritten.");

            bundle.put("gnuplotCommand", "Path or command for gnuplot. Default value is '${DEFAULT-VALUE}'.");
            bundle.put("rscriptCommand", "Path or command for Rscript. Default value is '${DEFAULT-VALUE}'.");

            bundle.put("useGamsNameTrimming", "Enables (1) or disables (0) gams name trimming. Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing.");
            bundle.put("maxGamsNameLength", "Max length for gams field names. Value -1 disables this option. Max length for gams is 63. Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing.");
            bundle.put("minGamsPartLength", "Min length for gams field name parts (tags). Default value is ${DEFAULT-VALUE}. Change this option only if you know what you are doing.");

            bundle.put("logPath", "Set path to log file and disable console logging.");
            bundle.put("logConsoleAndFile", "Enable console logging if '--logPath' is set.");

            bundle.put("prefereCsv", "Prefere csv files when loading.");
            bundle.put("dataDirPath", "Set path to data directory. If not set, the execution directory will be used.");
            bundle.put("skipPersistenceCheck", "If set, checksum mismatches are ignore during restoration. Use this option only if you know what you are doing.");
            bundle.put("skipArgValidation", "Disables command line validation. Use this option only if you know what you are doing.");
            bundle.put("skipPersist", "Disables data persistence.");
            bundle.put("languageTag", "Used language. Default value is '${DEFAULT-VALUE}'. Currently supported: 'de'");

            bundle.put("printIrptoolsHelp", "Calls IRPtools help.");
            bundle.put("callIrptools", "Calls IRPtools, all arguments will be transmitted to IRPtools, IRPact is not called.");

            bundle.put("printUtilitiesHelp", "Calls Utilities help.");
            bundle.put("callUtilities", "Calls Utilities, all arguments will be transmitted to Utilities, IRPact is not called.");

            bundle.put("testCl", "For testing the command line.");

            bundle.put("calculatePerformance", "currently supported: RMSD, MAE, FSAPE, globalAdoptionDelta, absoluteAnnualAdoptionDelta, cumulativeAnnualAdoptionDelta");
            bundle.put("noConsole", "disables console logging");

            fallback = bundle;
        }
        return fallback;
    }

    private static final String TRUE1 = "1";
    private static final String FALSE0 = "0";

    //=========================
    //core irpact
    //=========================

    @CommandLine.Option(
            names = { "-?", "-h", "--help", "--irpactUsage" },
            descriptionKey = "printHelp"
    )
    private boolean printHelp;

    @CommandLine.Option(
            names = { "-v", "--version" },
            descriptionKey = "printVersion"
    )
    private boolean printVersion;

    @CommandLine.Option(
            names = { "--irpactMode" },
            defaultValue = IRPactExecutors.DEFAULT_MODE,
            descriptionKey = "runMode"
    )
    private int runMode;

    @CommandLine.Option(
            names = { "--filterError" },
            descriptionKey = "filterError"
    )
    private boolean filterError;

    @CommandLine.Option(
            names = { "-i", "--input" },
            descriptionKey = "inputPath",
            converter = PathConverter.class
    )
    private Path inputPath;

    @CommandLine.Option(
            names = { "-o", "--output" },
            descriptionKey = "outputPath",
            converter = PathConverter.class
    )
    private Path outputPath;

    @CommandLine.Option(
            names = { "--outputDir" },
            descriptionKey = "outputDir",
            converter = PathConverter.class
    )
    private Path outputDir;

    @CommandLine.Option(
            names = { "--downloadDir" },
            descriptionKey = "downloadDir",
            converter = PathConverter.class
    )
    private Path downloadDir;

    @CommandLine.Option(
            names = { "--image" },
            descriptionKey = "imagePath",
            converter = PathConverter.class
    )
    private Path imagePath;

    @CommandLine.Option(
            names = { "--noSimulation" },
            descriptionKey = "noSimulation"
    )
    private boolean noSimulation;

    @CommandLine.Option(
            names = { "--checkOutputExistence" },
            descriptionKey = "checkOutputExistence"
    )
    private boolean checkOutputExistence;

    @CommandLine.Option(
            names = { "--gnuplotCommand" },
            defaultValue = GnuPlotEngine.DEFAULT_COMMAND,
            descriptionKey = "gnuplotCommand"
    )
    private String gnuplotCommand;

    @CommandLine.Option(
            names = { "--rscriptCommand" },
            defaultValue = RscriptEngine.DEFAULT_COMMAND,
            descriptionKey = "rscriptCommand"
    )
    private String rscriptCommand;

    //=========================
    //converter settings
    //=========================

    @CommandLine.Option(
            names = { "--useGamsNameTrimming" },
            defaultValue = FALSE0,
            descriptionKey = "useGamsNameTrimming"
    )
    private int useGamsNameTrimming;

    @CommandLine.Option(
            names = "--maxGamsNameLength",
            defaultValue = DefinitionMapper.IGNORE_NAME_LENGTH_AS_STR,
            descriptionKey = "maxGamsNameLength"
    )
    private int maxGamsNameLength;

    @CommandLine.Option(
            names = "--minGamsPartLength",
            defaultValue = DefinitionMapper.MIN_PART_LENGTH_AS_STR,
            descriptionKey = "minGamsPartLength"
    )
    private int minGamsPartLength;

    //=========================
    //logging
    //=========================

    @CommandLine.Option(
            names = { "--logPath" },
            descriptionKey = "logPath",
            converter = PathConverter.class
    )
    private Path logPath;

    @CommandLine.Option(
            names = { "--logConsoleAndFile" },
            descriptionKey = "logConsoleAndFile"
    )
    private boolean logConsoleAndFile;

    //=========================
    //diverses
    //=========================

    @Todo
    @CommandLine.Option(
            names = { "--prefereCsv" },
            descriptionKey = "prefereCsv",
            hidden = true //aktuell noch nicht richtig implementiert
    )
    private boolean prefereCsv;

    @CommandLine.Option(
            names = { "--dataDir" },
            descriptionKey = "dataDirPath",
            converter = PathConverter.class
    )
    private Path dataDirPath;

    @CommandLine.Option(
            names = { "--skipPersistenceCheck" },
            descriptionKey = "skipPersistenceCheck"
    )
    private boolean skipPersistenceCheck;

    @CommandLine.Option(
            names = { "--skipArgValidation" },
            descriptionKey = "skipArgValidation"
    )
    private boolean skipArgValidation;

    @CommandLine.Option(
            names = { "--skipPersist" },
            descriptionKey = "skipPersist"
    )
    private boolean skipPersist;

    @CommandLine.Option(
            names = { "--language" },
            defaultValue = "de",
            descriptionKey = "languageTag"
    )
    private String languageTag;

    //=========================
    //irptools
    //=========================

    @CommandLine.Option(
            names = { "--irptoolsUsage" },
            descriptionKey = "printIrptoolsHelp"
    )
    protected boolean printIrptoolsHelp;

    @CommandLine.Option(
            names = { "--irptools" },
            descriptionKey = "callIrptools"
    )
    private boolean callIrptools;

    //=========================
    //utilities
    //=========================

    @CommandLine.Option(
            names = { "--utilitiesUsage" },
            descriptionKey = "printUtilitiesHelp"
    )
    protected boolean printUtilitiesHelp;

    @CommandLine.Option(
            names = { "--utilities" },
            descriptionKey = "callUtilities"
    )
    private boolean callUtilities;

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
    //Performance
    //=========================

    @CommandLine.Option(
            names = { "--calculatePerformance" },
            descriptionKey = "calculatePerformance",
            split = ","
    )
    private String[] calculatePerformance;

    @CommandLine.Option(
            names = { "--noConsole" },
            descriptionKey = "noConsole"
    )
    private boolean noConsole;

    //=========================
    //rest
    //=========================

    private boolean executed = false;
    private IRPLoggingMessage executeResultMessage;

    private boolean hasCustomInput = false;
    private boolean hasCallback = false;

    public MainCommandLineOptions(String... args) {
        super(args);
    }

    public MainCommandLineOptions copy() {
        MainCommandLineOptions copy = new MainCommandLineOptions(getArgsCopy());
        copy.setHasCustomInput(hasCustomInput());
        copy.setHasCallback(hasCallback());
        copy.parse();
        return copy;
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

    public IRPLoggingMessage getExecuteResultMessage() {
        return executeResultMessage;
    }

    @Override
    protected ResourceBundle getFallback() {
        return getFallbackBundle();
    }

    public void printVersionAndHelp() {
        getCommandLine().printVersionHelp(System.out);
        getCommandLine().usage(System.out);
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
                || isPrintVersion();
    }

    public boolean isNotPrintHelpOrVersion() {
        return !isPrintHelpOrVersion();
    }

    public int getRunMode() {
        checkExecuted();
        return runMode;
    }

    public boolean isFilterError() {
        checkExecuted();
        return filterError;
    }

    public boolean hasInputPath() {
        return getInputPath() != null;
    }
    public Path getInputPath() {
        checkExecuted();
        return inputPath;
    }
    public String getInputPathFileName() {
        return getInputPath().getFileName().toString();
    }

    public boolean hasOutputPath() {
        return getOutputPath() != null;
    }
    public Path getOutputPath() {
        checkExecuted();
        return outputPath;
    }
    public void setOutputPath(Path outputPath) {
        checkExecuted();
        this.outputPath = outputPath;
    }
    public Path getOutputPathDir() {
        Path output = getOutputPath();
        Path parent = output.getParent();
        if(parent == null) {
            parent = output.toAbsolutePath().getParent();
        }
        //still null
        if(parent == null) {
            throw new IllegalArgumentException("invalid output path, no parent: " + output);
        }
        return parent;
    }

    public Path getOutputDir() {
        checkExecuted();
        if(outputDir == null) {
            return getOutputPathDir();
        } else {
            return outputDir;
        }
    }
    public Path getCreatedOutputDir() throws IOException {
        Path outputDir = getOutputDir();
        if(Files.notExists(outputDir)) {
            Files.createDirectories(outputDir);
        }
        else if(!Files.isDirectory(outputDir)) {
            throw new IOException("no directoy: " + outputDir);
        }
        return outputDir;
    }

    public Path getDownloadDir() {
        checkExecuted();
        if(downloadDir == null) {
            return getOutputDir().resolve(IRPact.DOWNLOAD_DIR_NAME);
        } else {
            return downloadDir;
        }
    }
    public Path getCreatedDownloadDir() throws IOException {
        Path downloadDir = getDownloadDir();
        if(Files.notExists(downloadDir)) {
            Files.createDirectories(downloadDir);
        }
        else if(!Files.isDirectory(downloadDir)) {
            throw new IOException("no directoy: " + downloadDir);
        }
        return downloadDir;
    }

    public String getGnuplotCommand() {
        return gnuplotCommand == null
                ? GnuPlotEngine.DEFAULT_COMMAND
                : gnuplotCommand;
    }

    public String getRscriptCommand() {
        return rscriptCommand == null
                ? RscriptEngine.DEFAULT_COMMAND
                : rscriptCommand;
    }

    public boolean hasNoImagePath() {
        return !hasImagePath();
    }
    public boolean hasImagePath() {
        return getImagePath() != null;
    }
    public boolean hasImagePathWithoutFile() {
        return hasImagePath() && Files.notExists(getImagePath());
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
    public void setLogPath(Path logPath) {
        checkExecuted();
        this.logPath = logPath;
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
        return getDataDirPath() != ResourceLoader.EXTERN_RESOURCES_PATH;
    }
    public Path getDataDirPath() {
        checkExecuted();
        return dataDirPath == null
                ? ResourceLoader.EXTERN_RESOURCES_PATH
                : dataDirPath;
    }

    public boolean isSkipPersistenceCheck() {
        checkExecuted();
        return skipPersistenceCheck;
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

    public boolean isSkipPersist() {
        return skipPersist;
    }

    public boolean isTestCl() {
        checkExecuted();
        return testCl;
    }

    public String getLanguageTag() {
        checkExecuted();
        return languageTag;
    }

    public Locale getLocale(Locale ifNotFound) {
        String tag = getLanguageTag();
        try {
            return Locale.forLanguageTag(tag);
        } catch (Exception e) {
            return ifNotFound;
        }
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

    private boolean isCallSub() {
        return isPrintUtilitiesHelp()
                || isCallUtilities()
                || isPrintIrptoolsHelp()
                || isCallIrptools();
    }

    private boolean cancelValidation() {
        return skipArgValidation
                || isTestCl()
                || isCallSub()
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
                executeResultMessage = new IRPLoggingMessage("custom input and file input specified");
                return CommandLine.ExitCode.USAGE;
            } else {
                return CommandLine.ExitCode.OK;
            }
        } else {
            if(hasCustomInput()) {
                return CommandLine.ExitCode.OK;
            } else {
                executeResultMessage = new IRPLoggingMessage("missing input");
                return CommandLine.ExitCode.USAGE;
            }
        }
    }

    private int validateOutput() {
        if(hasOutputPath()) {
            return CommandLine.ExitCode.OK;
        } else {
            if(hasCallback() || isNoSimulation()) {
                return CommandLine.ExitCode.OK;
            } else {
                executeResultMessage = new IRPLoggingMessage("missing output");
                return CommandLine.ExitCode.USAGE;
            }
        }
    }

    public String[] getCalculatePerformanceArray() {
        return calculatePerformance == null
                ? new String[0]
                : calculatePerformance;
    }

    public boolean isNoConsole() {
        return noConsole;
    }
}
