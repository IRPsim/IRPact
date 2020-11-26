package de.unileipzig.irpact.start;

import de.unileipzig.irpact.start.optact.OptActMain;
import de.unileipzig.irpact.v2.commons.log.Logback;

/**
 * Starts IRPact.
 *
 * @author Daniel Abitz
 */
public class Start {

//    private static final Logger logger = LoggerFactory.getLogger(Start.class);
//
//    private static void startIRPact(CommandLineInterpreter cli) throws IOException {
//        IRPactStarter starter = new IRPactStarter(cli.getInputPath(), cli.getOutputPath());
//        starter.start();
//    }
//
//    private static void startIRPtools(CommandLineInterpreter cli) throws Exception {
//        IRPtoolsStarter starter = new IRPtoolsStarter(cli);
//        starter.start();
//    }
//
//    @SuppressWarnings("ConstantConditions")
//    public static void main(String[] args) throws IOException {
////        if(true) {
////            UITest.main(args);
////            return;
////        }
//        if(true) {
//            OptActMain.main(args);
//            return;
//        }
//
//        try {
//            JadexUtil.redirectTerminateMessage();
//            logger.trace("args: {}", Arrays.toString(args));
//            CommandLineInterpreter cli = new CommandLineInterpreter(args);
//            if(cli.isOk()) {
//                if(cli.startIRPact()) {
//                    startIRPact(cli);
//                } else if(cli.startIRPtools()) {
//                    startIRPtools(cli);
//                } else {
//                    throw new IllegalStateException();
//                }
//            } else {
//                logger.error("Input Error", cli.getCause());
//            }
//        } catch (Throwable t) {
//            logger.error("Unchecked Error", t);
//        }
//    }

    public static void main(String[] args) {
        Logback.setupSystemOutAndErr();
        OptActMain.main(args);
    }
}
