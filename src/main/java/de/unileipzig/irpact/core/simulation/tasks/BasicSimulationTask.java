package de.unileipzig.irpact.core.simulation.tasks;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class BasicSimulationTask extends AbstractTask implements SimulationTask {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicSimulationTask.class);

    public static final long ID = -100L;

    public BasicSimulationTask() {
        super(IRPactJson.SMILE.createObjectNode());
    }

    public BasicSimulationTask(byte[] data) throws IOException {
        super((ObjectNode) IRPactJson.fromBytesWithSmile(data));
    }

    @Override
    protected long getID() {
        return ID;
    }

    @Override
    public void run(SimulationEnvironment environment) {
        LOGGER.debug("'run' not implemented");
    }
}
