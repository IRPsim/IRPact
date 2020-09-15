package de.unileipzig.irpact.start;

import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.start.irptools.IRPtoolsStarter;
import de.unileipzig.irpact.temp.jadex.IRPactStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * Starts IRPact.
 *
 * @author Daniel Abitz
 */
public class Start {

    private static final Logger logger = LoggerFactory.getLogger(Start.class);

    private static void startIRPact(CommandLineInterpreter cli) throws IOException {
        IRPactStarter starter = new IRPactStarter(cli.getInputPath(), cli.getOutputPath());
        starter.start();
    }

    private static void startIRPtools(CommandLineInterpreter cli) throws Exception {
        IRPtoolsStarter starter = new IRPtoolsStarter(cli);
        starter.start();
    }

    public static void main(String[] args) {
        if(true) {
            throw new RuntimeException("UI-TEST");
        }

        try {
            JadexUtil.redirectTerminateMessage();
            logger.trace("args: {}", Arrays.toString(args));
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
