package de.unileipzig.irpact.core.simulation.tasks;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class PredefinedPreAgentCreationTask extends PredefinedBinaryTask implements PreAgentCreationTask {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PredefinedPreAgentCreationTask.class);

    public static final int ID = -100;

    public static final int HELLO_WORLD = 1;

    public PredefinedPreAgentCreationTask() {
        super(JsonUtil.SMILE.createObjectNode());
        setInfo(NO_INFO);
        setTask(NO_TASK);
    }

    public PredefinedPreAgentCreationTask(byte[] content) throws IOException {
        super((ObjectNode) JsonUtil.fromBytesWithSmile(content));
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public void run(SimulationEnvironment environment) throws Exception {
        int task = getTask();
        switch (task) {
            case HELLO_WORLD:
                callHelloWorld();
                break;

            case NO_TASK:
                LOGGER.warn("task '{}' has no task number", getInfo());
                break;

            default:
                LOGGER.warn("task '{}' has invalid task number", getInfo());
                break;
        }
    }

    //=========================
    //tasks
    //=========================

    private void callHelloWorld() {
        LOGGER.info("[{}] Hello World", getInfo());
    }
}
