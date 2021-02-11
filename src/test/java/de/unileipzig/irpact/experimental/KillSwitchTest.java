package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.simulation.KillSwitch;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public class KillSwitchTest {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(KillSwitchTest.class);

    @Test
    void doKill() {
        IRPLogging.initConsole();
        KillSwitch killSwitch = new KillSwitch();
        killSwitch.setTimeout(5000);
        killSwitch.start();
        LOGGER.info("start");
        ConcurrentUtil.sleepSilently(10000);
        LOGGER.info("finished");
    }

    @Test
    void doReset() {
        IRPLogging.initConsole();
        KillSwitch killSwitch = new KillSwitch();
        killSwitch.setTimeout(5000);
        killSwitch.start();
        LOGGER.info("start");
        ConcurrentUtil.sleepSilently(3000);
        LOGGER.info("reset 1");
        killSwitch.reset();
        ConcurrentUtil.sleepSilently(3000);
        LOGGER.info("reset 2");
        killSwitch.reset();
        ConcurrentUtil.sleepSilently(10000);
        LOGGER.info("finished");
    }

    @Test
    void doFinish() {
        IRPLogging.initConsole();
        KillSwitch killSwitch = new KillSwitch();
        killSwitch.setTimeout(5000);
        killSwitch.start();
        LOGGER.info("start");
        ConcurrentUtil.sleepSilently(2000);
        LOGGER.info("finished");
        killSwitch.finished();
    }

    @Test
    void killSwitchWithFile() {
        IRPLogging.initConsole();
        IRPLogging.initFile(Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact", "testfiles", "0x0", "file.log"));
        KillSwitch killSwitch = new KillSwitch();
        killSwitch.setTimeout(5000);
        killSwitch.start();
        LOGGER.info("start");
        ConcurrentUtil.sleepSilently(2000);
        LOGGER.info("finished");
        killSwitch.finished();
        System.out.println("X");
    }
}
