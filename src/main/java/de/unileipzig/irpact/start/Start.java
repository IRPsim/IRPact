package de.unileipzig.irpact.start;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.SectionLoggingFilter;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

/**
 * Starts IRPact.
 *
 * @author Daniel Abitz
 */
public final class Start {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Start.class);

    private Start() {
    }

    private static void prepareLogging() {
        IRPLogging.initConsole();
        SectionLoggingFilter filter = new SectionLoggingFilter();
        IRPLogging.setFilter(filter);
        IRPtools.setLoggingFilter(filter);
        IRPSection.addSectionsToTools();
        IRPSection.addAllNonToolsTo(filter);
    }

    private static void setupLogging(CommandLineOptions options) throws IOException {
        if(options.logToFile()) {

            boolean hasOldLogFile = Files.exists(options.getLogPath());
            if(hasOldLogFile) {
                Files.delete(options.getLogPath());
            }

            if(options.logConsoleAndFile()) {
                IRPLogging.initConsoleAndFile(options.getLogPath());
                logOldFileDeleted(hasOldLogFile, options.getLogPath());
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to console and file '{}'", options.getLogPath());
            } else {
                IRPLogging.initFile(options.getLogPath());
                logOldFileDeleted(hasOldLogFile, options.getLogPath());
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to file '{}'", options.getLogPath());
            }
        } else {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "log to console");
        }
    }

    private static void logOldFileDeleted(boolean deleted, Path path) {
        if(deleted) {
            LOGGER.trace(IRPSection.GENERAL, "old logfile '{}' deleted", path);
        }
    }

    private static StartResult startApp(String[] args, AnnualEntry<InRoot> scenario, Collection<? extends IRPactCallback> callbacks) {
        prepareLogging();
        CommandLineOptions options = new CommandLineOptions(args);
        int exitCode = options.parse();

        try {
            setupLogging(options);
        } catch (IOException e) {
            LOGGER.error("Setup logging failed, cancel start", e);
            return new StartResult(CommandLine.ExitCode.SOFTWARE, e);
        }

        if(exitCode == CommandLine.ExitCode.OK) {
            if(options.hasExecuteResultMessage()) {
                options.getExecuteResultMessage().log(LOGGER, Level.INFO);
            }
            if(options.isPrintHelp() || options.isPrintVersion()) {
                return new StartResult(CommandLine.ExitCode.OK);
            }
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
        } else {
            if(options.hasExecuteResultMessage()) {
                options.getExecuteResultMessage().error(LOGGER);
            }
            return new StartResult(options.getErrorCode());
        }
    }

    private static StartResult startApp(String[] args, Collection<? extends IRPactCallback> callbacks) {
        return startApp(args, null, callbacks);
    }

    public static int start(String[] args, IRPactCallback... callbacks) {
        return start(args, Arrays.asList(callbacks));
    }

    public static int start(String[] args, Collection<? extends IRPactCallback> callbacks) {
        StartResult result = startApp(args, callbacks);
        return result.getErrorCode();
    }

    public static int startWithGui(String[] args, IRPactCallback... callbacks) throws Throwable {
        return startWithGui(args, Arrays.asList(callbacks));
    }

    public static int startWithGui(String[] args, Collection<? extends IRPactCallback> callbacks) throws Throwable {
        StartResult result = startApp(args, callbacks);
        if(result.getCause() != null) {
            throw result.getCause();
        }
        if(result.getErrorCode() != CommandLine.ExitCode.OK) {
            throw new IllegalArgumentException("invalid arguments");
        }
        return CommandLine.ExitCode.OK;
    }

    public static int start(String[] args, AnnualEntry<InRoot> scenario, IRPactCallback... callbacks) {
        return start(args, scenario, Arrays.asList(callbacks));
    }

    public static int start(String[] args, AnnualEntry<InRoot> scenario, Collection<? extends IRPactCallback> callbacks) {
        StartResult result = startApp(args, scenario, callbacks);
        return result.getErrorCode();
    }

    //start nutzen, wenn nicht via konsole!
    public static void main(String[] args) {
        int errorCode = start(args);
        System.exit(errorCode);
    }

    /**
     * @author Daniel Abitz
     */
    private static final class StartResult {

        private final int ERROR_CODE;
        private final Throwable CAUSE;

        private StartResult(int errorCode) {
            this(errorCode, null);
        }

        private StartResult(int errorCode, Throwable cause) {
            this.ERROR_CODE = errorCode;
            this.CAUSE = cause;
        }

        public int getErrorCode() {
            return ERROR_CODE;
        }

        public Throwable getCause() {
            return CAUSE;
        }
    }
}
