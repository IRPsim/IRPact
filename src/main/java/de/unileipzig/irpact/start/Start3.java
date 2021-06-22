package de.unileipzig.irpact.start;

import de.unileipzig.irpact.commons.logging.LazyPrinter;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.SectionLoggingFilter;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.irpact.IRPactCallback;
import de.unileipzig.irpact.start.utilities.Utilities;
import de.unileipzig.irptools.io.base.data.AnnualEntry;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.log.IRPLogger;
import picocli.CommandLine;

import java.io.IOException;
import java.util.*;

/**
 * Starts IRPact.
 *
 * @author Daniel Abitz
 */
public final class Start3 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Start3.class);

    private MainCommandLineOptions options;
    private Result result;

    private Start3() {
    }

    private static void prepareLogging() {
        IRPLogging.initalize();
    }

    private void setupLogging() throws IOException {
        Preloader3.setupLogging(options);
    }

    private void parseArgs(
            String[] args,
            PerennialData<InRoot> scenario,
            Collection<? extends IRPactCallback> callbacks) {
        LOGGER.trace("args: {}", LazyPrinter.printArray(args));
        options = new MainCommandLineOptions(args);
        options.setHasCustomInput(scenario != null);
        options.setHasCallback(callbacks != null && callbacks.size() > 0);
        options.parse();
    }

    private boolean initLogging() {
        try {
            IRPLogging.setFilterError(options.isFilterError());
            setupLogging();
            return true;
        } catch (IOException e) {
            LOGGER.error("setup logging failed, start canceled", e);
            result = new Result(CommandLine.ExitCode.SOFTWARE, e);
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
            result = new Result(exitCode);
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
            result = new Result(options.getExitCode());
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

    private Result run(String[] args) {
        return run(args, null, Collections.emptyList());
    }

    private Result run(
            String[] args,
            PerennialData<InRoot> scenario,
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

    private Result runIRPtools() {
        try {
            if(options.isPrintIrptoolsHelp()) {
                IRPtools.main(new String[]{"-?"});
            } else {
                LOGGER.trace(IRPSection.GENERAL, "executing IRPtools");
                SectionLoggingFilter filter = IRPLogging.getFilter();
                IRPSection.addAllToolsTo(filter);
                IRPtools.main(options.getArgs());
            }
            return new Result(options.getExitCode());
        } catch (Throwable t) {
            LOGGER.error("Running IRPtools failed with unknown exception.", t);
            return new Result(options.getExitCode(), t);
        }
    }

    private Result runUtilities() {
        try {
            if(options.isPrintUtilitiesHelp()) {
                Utilities.run(options, new String[]{"-?"});
            } else {
                LOGGER.trace(IRPSection.GENERAL, "executing Utilities");
                Utilities.run(options, options.getArgs());
            }
            return new Result(options.getExitCode());
        } catch (Throwable t) {
            LOGGER.error("Running Utilities failed with unknown exception.", t);
            return new Result(options.getExitCode(), t);
        }
    }

    private Result runPreloader(
            PerennialData<InRoot> scenario,
            Collection<? extends IRPactCallback> callbacks) {
        Preloader loader = new Preloader(options, callbacks);
        try {
            if(scenario == null) {
                loader.start();
            } else {
                //loader.start(scenario);
                Dev.throwException();
            }
            LOGGER.trace(IRPSection.GENERAL, "Start finished");
            return new Result(CommandLine.ExitCode.OK);
        } catch (Throwable t) {
            LOGGER.error("Start failed with uncaught exception", t);
            return new Result(CommandLine.ExitCode.SOFTWARE, t);
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
            PerennialData<InRoot> scenario,
            IRPactCallback... callbacks) throws Throwable {
        return start(args, scenario, Arrays.asList(callbacks));
    }

    public static int start(
            String[] args,
            PerennialData<InRoot> scenario,
            Collection<? extends IRPactCallback> callbacks) throws Throwable {
        Start3 start = new Start3();
        Result result = start.run(args, scenario, callbacks);
        if(result.getCause() != null) {
            throw result.getCause();
        }
        if(result.isFailure()) {
            throw new IllegalArgumentException("invalid arguments");
        }
        return result.getExitCode();
    }

    public static void main(String[] args) {
        Start3 start = new Start3();
        Result result = start.run(args);
        System.exit(result.getExitCode());
    }

    //=========================
    //helper
    //=========================

    /**
     * @author Daniel Abitz
     */
    public static final class Input {

        private final String[] args;
        private final AnnualEntry<InRoot> scenario;
        private final Collection<? extends IRPactCallback> callbacks;

        public Input(String[] args, AnnualEntry<InRoot> scenario, Collection<? extends IRPactCallback> callbacks) {
            this.args = args;
            this.scenario = scenario;
            this.callbacks = callbacks;
        }

        public String[] getArgs() {
            return args;
        }

        public InRoot getScenarioData() {
            return scenario.getData();
        }

        public AnnualEntry<InRoot> getScenario() {
            return scenario;
        }

        public Collection<? extends IRPactCallback> getCallbacks() {
            return callbacks;
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static final class Result {

        private final int EXIT_CODE;
        private final Throwable CAUSE;

        public Result(int exitCode) {
            this(exitCode, null);
        }

        public Result(int exitCode, Throwable cause) {
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
