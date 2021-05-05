package de.unileipzig.irpact.start;

import de.unileipzig.irpact.commons.log.IRPLoggingMessage;
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
            IRPLoggingMessage deletedMsg = null;
            if(hasOldLogFile) {
                Files.delete(options.getLogPath());
                deletedMsg = new IRPLoggingMessage(IRPSection.INITIALIZATION_PARAMETER, "old logfile '{}' deleted", options.getLogPath());
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

    private static StartResult startApp(
            String[] args,
            AnnualEntry<InRoot> scenario,
            Collection<? extends IRPactCallback> callbacks) {
        prepareLogging();

        CommandLineOptions options = new CommandLineOptions(args);
        options.setHasCustomInput(scenario != null);
        options.setHasCallback(callbacks != null && callbacks.size() > 0);
        int exitCode = options.parse();

        try {
            setupLogging(options);
        } catch (IOException e) {
            LOGGER.error("setup logging failed, start canceled", e);
            return new StartResult(CommandLine.ExitCode.SOFTWARE, e);
        }

        if(exitCode == CommandLine.ExitCode.OK) {
            if(options.hasExecuteResultMessage()) {
                options.getExecuteResultMessage().log(LOGGER, Level.INFO);
            }

            if(options.isPrintHelpOrVersion()) {
                if(options.isPrintIRPtoolsHelp()) {
                    IRPtools.main(new String[]{"-?"});
                }
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
        StartResult result = startApp(args, scenario, callbacks);
        if(result.getCause() != null) {
            throw result.getCause();
        }
        if(result.isFailure()) {
            throw new IllegalArgumentException("invalid arguments");
        }
        return result.getExitCode();
    }

    public static void main(String[] args) throws StartException {
        try {
            int exitCode = start(args);
            System.exit(exitCode);
        } catch (Throwable throwable) {
            throw new StartException(throwable);
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static final class StartException extends RuntimeException {

        private StartException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static final class StartResult {

        private final int EXIT_CODE;
        private final Throwable CAUSE;

        private StartResult(int exitCode) {
            this(exitCode, null);
        }

        private StartResult(int exitCode, Throwable cause) {
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
