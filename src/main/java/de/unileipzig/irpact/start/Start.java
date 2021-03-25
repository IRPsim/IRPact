package de.unileipzig.irpact.start;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
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

    public static void start(String[] args, IRPactCallback... callbacks) {
        start(args, Arrays.asList(callbacks));
    }

    public static void start(String[] args, Collection<? extends IRPactCallback> callbacks) {
        IRPLogging.initConsole();
        CommandLineOptions options = new CommandLineOptions(args);
        int exitCode = options.parse();
        if(exitCode == CommandLine.ExitCode.OK) {
            if(options.isPrintHelp() || options.isPrintVersion()) {
                return;
            }
            Preloader loader = new Preloader(options, callbacks);
            try {
                loader.start();
                LOGGER.debug("Start finished");
            } catch (Throwable t) {
                LOGGER.error("Start failed with uncaught exception", t);
                System.exit(CommandLine.ExitCode.SOFTWARE);
            }
        } else {
            System.exit(CommandLine.ExitCode.USAGE);
        }
    }

    public static int startWithGui(String[] args, IRPactCallback... callbacks) throws Throwable {
        return startWithGui(args, Arrays.asList(callbacks));
    }

    public static int startWithGui(String[] args, Collection<? extends IRPactCallback> callbacks) throws Throwable {
        IRPLogging.initConsole();
        CommandLineOptions options = new CommandLineOptions(args);
        int exitCode = options.parse();
        if(exitCode == CommandLine.ExitCode.OK) {
            if(options.isPrintHelp() || options.isPrintVersion()) {
                return CommandLine.ExitCode.OK;
            }
            Preloader loader = new Preloader(options, callbacks);
            try {
                loader.start();
                LOGGER.debug("Start finished");
                return CommandLine.ExitCode.OK;
            } catch (Throwable t) {
                LOGGER.error("Start failed with uncaught exception", t);
                throw t;
            }
        } else {
            throw new IllegalArgumentException("Fehlerhafte Eingabe (...)");
        }
    }

    public static void main(String[] args) {
        start(args);
    }
}
