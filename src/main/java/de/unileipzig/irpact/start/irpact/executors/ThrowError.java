package de.unileipzig.irpact.start.irpact.executors;

import de.unileipzig.irpact.commons.exception.IRPactException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactExecutor;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public final class ThrowError implements IRPactExecutor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ThrowError.class);

    public static final int ID = 666;
    public static final String ID_STR = "666";
    public static final ThrowError INSTANCE = new ThrowError();

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute(IRPact irpact) throws Exception {
        if(irpact == null) {
            throw new NullPointerException("IRPact instance is null");
        }

        try {
            LOGGER.info("execute ThrowError");
            throw new IRPactException("forced exception");
        } catch (Exception e) {
            if(irpact.shouldCreateDummyOutputWithErrorMessage()) {
                irpact.postSimulationWithDummyOutputAndErrorMessage(e);
            } else {
                throw e;
            }
        }
    }
}
