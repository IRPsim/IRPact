package de.unileipzig.irpact.start;

import de.unileipzig.irpact.commons.log.LazyPrinter;
import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPLoggingMessage;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.SectionLoggingFilter;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.irpact.IRPactCallback;
import de.unileipzig.irpact.start.utilities.Utilities;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.IRPLogger;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Starts IRPact.
 *
 * @author Daniel Abitz
 */
public final class Start {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Start.class);

    private MainCommandLineOptions options;
    private StartResult result;

    private Start() {
    }

    private static void prepareLogging() {
        IRPLogging.initConsole();
        SectionLoggingFilter filter = new SectionLoggingFilter();
        IRPLogging.setFilter(filter);
        IRPtools.setLoggingFilter(IRPLogging.getFilter());
        IRPSection.addSectionsToTools();
        IRPSection.addAllNonToolsTo(filter);
    }

    private void setupLogging() throws IOException {
        if(options.logToFile()) {

            boolean hasOldLogFile = Files.exists(options.getLogPath());
            IRPLoggingMessage deletedMsg = null;
            if(hasOldLogFile) {
                Files.delete(options.getLogPath());
                deletedMsg = new IRPLoggingMessage("old logfile '{}' deleted", options.getLogPath()).setSection(IRPSection.INITIALIZATION_PARAMETER);
            }

            if(options.logConsoleAndFile()) {
                IRPLogging.initConsoleAndFile(options.getLogPath());
                if(deletedMsg != null) {
                    deletedMsg.trace(LOGGER);
                }
                if(options.isNotPrintHelpOrVersion()) {
                    LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to console and file '{}'", options.getLogPath());
                }
            } else {
                IRPLogging.initFile(options.getLogPath());
                if(deletedMsg != null) {
                    deletedMsg.trace(LOGGER);
                }
                if(options.isNotPrintHelpOrVersion()) {
                    LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to file '{}'", options.getLogPath());
                }
            }
        } else {
            if(options.isNotPrintHelpOrVersion()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to console");
            }
        }
    }

    private void parseArgs(
            String[] args,
            AnnualEntry<InRoot> scenario,
            Collection<? extends IRPactCallback> callbacks) {
        LOGGER.trace("args: {}", LazyPrinter.printArray(args));
        options = new MainCommandLineOptions(args);
        options.setHasCustomInput(scenario != null);
        options.setHasCallback(callbacks != null && callbacks.size() > 0);
        options.parse();
    }

    private boolean initLogging() {
        try {
            if(options.isFilterError()) {
                Logback.filterError(true);
            }
            setupLogging();
            return true;
        } catch (IOException e) {
            LOGGER.error("setup logging failed, start canceled", e);
            result = new StartResult(CommandLine.ExitCode.SOFTWARE, e);
            return false;
        }
    }

    private boolean validateOptions() {
        int exitCode = options.getExitCode();
        if(exitCode == CommandLine.ExitCode.OK) {
            if(options.hasExecuteResultMessage()) {
                options.getExecuteResultMessage().trace(LOGGER);
            }
            return true;
        } else {
            if(options.hasExecuteResultMessage()) {
                options.getExecuteResultMessage().error(LOGGER);
            }
            result = new StartResult(exitCode);
            return false;
        }
    }

    private boolean isHelpOrVersion() {
        if(options.isPrintVersion()) {
            options.getCommandLine().printVersionHelp(System.out);
        }
        if(options.isPrintHelp()) {
            options.getCommandLine().usage(System.out);
        }
        if(options.isPrintHelpOrVersion()) {
            result = new StartResult(options.getExitCode());
            return true;
        }
        return false;
    }

    private boolean isRunIRPtools() {
        return options.isCallIrptools() || options.isPrintIrptoolsHelp();
    }

    private boolean isRunUtilities() {
        return options.isCallUtilities() || options.isPrintUtilitiesHelp();
    }

    private StartResult run(String[] args) {
        return run(args, null, Collections.emptyList());
    }

    private StartResult run(
            String[] args,
            AnnualEntry<InRoot> scenario,
            Collection<? extends IRPactCallback> callbacks) {
        prepareLogging();
        parseArgs(args, scenario, callbacks);
        if(!initLogging()) return result;
        if(!validateOptions()) return result;
        if(isHelpOrVersion()) return result;
        if(isRunIRPtools()) return runIRPtools();
        if(isRunUtilities()) return runUtilities();
        return runPreloader(scenario, callbacks);
    }

    private StartResult runIRPtools() {
        try {
            if(options.isPrintIrptoolsHelp()) {
                IRPtools.main(new String[]{"-?"});
            } else {
                LOGGER.trace(IRPSection.GENERAL, "executing IRPtools");
                SectionLoggingFilter filter = IRPLogging.getFilter();
                IRPSection.addAllToolsTo(filter);
                IRPtools.main(options.getArgs());
            }
            return new StartResult(options.getExitCode());
        } catch (Throwable t) {
            LOGGER.error("Running IRPtools failed with unknown exception.", t);
            return new StartResult(options.getExitCode(), t);
        }
    }

    private StartResult runUtilities() {
        try {
            if(options.isPrintUtilitiesHelp()) {
                Utilities.run(options, new String[]{"-?"});
            } else {
                LOGGER.trace(IRPSection.GENERAL, "executing Utilities");
                Utilities.run(options, options.getArgs());
            }
            return new StartResult(options.getExitCode());
        } catch (Throwable t) {
            LOGGER.error("Running Utilities failed with unknown exception.", t);
            return new StartResult(options.getExitCode(), t);
        }
    }

    private StartResult runPreloader(
            AnnualEntry<InRoot> scenario,
            Collection<? extends IRPactCallback> callbacks) {
        Preloader loader = new Preloader(options, callbacks);
        try {
            if(scenario == null) {
                loader.start();
            } else {
                loader.start(scenario);
            }
            LOGGER.trace(IRPSection.GENERAL, "Start finished");
            return new StartResult(CommandLine.ExitCode.OK);
        } catch (Throwable t) {
            LOGGER.error("Start failed with uncaught exception", t);
            return new StartResult(CommandLine.ExitCode.SOFTWARE, t);
        }
    }

    //=========================
    //starter
    //=========================

    public static int start(
            String[] args,
            IRPactCallback... callbacks) throws Throwable {
        return start(args, null, callbacks);
    }

    public static int start(
            String[] args,
            Collection<? extends IRPactCallback> callbacks) throws Throwable {
        return start(args, null, callbacks);
    }

    public static int start(
            String[] args,
            AnnualEntry<InRoot> scenario,
            IRPactCallback... callbacks) throws Throwable {
        return start(args, scenario, Arrays.asList(callbacks));
    }

    public static int start(
            String[] args,
            AnnualEntry<InRoot> scenario,
            Collection<? extends IRPactCallback> callbacks) throws Throwable {
        Start start = new Start();
        StartResult result = start.run(args, scenario, callbacks);
        if(result.getCause() != null) {
            throw result.getCause();
        }
        if(result.isFailure()) {
            throw new IllegalArgumentException("invalid arguments");
        }
        return result.getExitCode();
    }

    public static void main(String[] args) {
        Start start = new Start();
        StartResult result = start.run(args);
        System.exit(result.getExitCode());
    }

    //=========================
    //helper
    //=========================

    /**
     * @author Daniel Abitz
     */
    public static final class StartResult {

        private final int EXIT_CODE;
        private final Throwable CAUSE;

        public StartResult(int exitCode) {
            this(exitCode, null);
        }

        public StartResult(int exitCode, Throwable cause) {
            this.EXIT_CODE = exitCode;
            this.CAUSE = cause;
        }

        public int getExitCode() {
            return EXIT_CODE;
        }

        public boolean isOk() {
            return getExitCode() == CommandLine.ExitCode.OK;
        }

        public boolean isFailure() {
            return !isOk();
        }

        public Throwable getCause() {
            return CAUSE;
        }
    }
}
