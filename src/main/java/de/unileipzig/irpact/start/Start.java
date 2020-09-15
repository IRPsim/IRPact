package de.unileipzig.irpact.start;

import de.unileipzig.irpact.jadex.util.JadexRedirectPrintStream;
import de.unileipzig.irpact.start.hardcodeddemo.HardCodedAgentDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

/**
 * Starts IRPact.
 *
 * @author Daniel Abitz
 */
public class Start {

    private static final Logger logger = LoggerFactory.getLogger(Start.class);

    private static void redirectSystem() {
        PrintStream out = System.out;
        PrintStream err = System.err;
        System.setOut(new JadexRedirectPrintStream(out, false));
        System.setErr(new JadexRedirectPrintStream(err, true));
    }

    private static void startIRPact(CommandLineInterpreter cli) {

    }

    private static void startIRPtools(CommandLineInterpreter cli) {

    }

    public static void main(String[] args) {
        try {
            CommandLineInterpreter cli = new CommandLineInterpreter(args);
            if(cli.isOk()) {
                if(cli.startIRPact()) {
                    startIRPact(cli);
                } else if(cli.startIRPtools()) {
                    startIRPtools(cli);
                } else {
                    throw new IllegalStateException();
                }
            } else {
                logger.error("Input Error", cli.getCause());
            }
        } catch (Throwable t) {
            logger.error("Unchecked Error", t);
        }
    }
}
