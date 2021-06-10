package de.unileipzig.irpact.start.irpact.modes;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactExecutor;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public final class TestMode implements IRPactExecutor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(TestMode.class);

    public static final int ID = 100;
    public static final String ID_STR = "100";
    public static final TestMode INSTANCE = new TestMode();

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute(IRPact irpact) throws Exception {
        LOGGER.info(IRPSection.GENERAL, "execute TestMode");

        irpact.postSimulationWithDummyOutput("DUMMY_TestMode");
    }
}
