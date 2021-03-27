package de.unileipzig.irpact.start;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.SectionLoggingFilter;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;
import picocli.CommandLine;

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

    private static void setupLogging(CommandLineOptions options) {
        if(options.logToFile()) {
            if(options.logConsoleAndFile()) {
                IRPLogging.initConsoleAndFile(options.getLogPath());
                LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "log to console and file '{}'", options.getLogPath());
            } else {
                IRPLogging.initFile(options.getLogPath());
                LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "log to file '{}'", options.getLogPath());
            }
        } else {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "log to console");
        }
    }

    private static StartResult startApp(String[] args, Collection<? extends IRPactCallback> callbacks) {
        prepareLogging();
        CommandLineOptions options = new CommandLineOptions(args);
        int exitCode = options.parse();
        setupLogging(options);

        if(exitCode == CommandLine.ExitCode.OK) {
            if(options.hasExecuteResultMessage()) {
                options.getExecuteResultMessage().log(LOGGER, Level.INFO);
            }
            if(options.isPrintHelp() || options.isPrintVersion()) {
                return new StartResult(CommandLine.ExitCode.OK);
            }
            Preloader loader = new Preloader(options, callbacks);
            try {
                loader.start();
                LOGGER.debug("Start finished");
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

    public static void start(String[] args, IRPactCallback... callbacks) {
        start(args, Arrays.asList(callbacks));
    }

    public static void start(String[] args, Collection<? extends IRPactCallback> callbacks) {
        StartResult result = startApp(args, callbacks);
        System.exit(result.getErrorCode());
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

    public static void main(String[] args) {
        start(args);
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
