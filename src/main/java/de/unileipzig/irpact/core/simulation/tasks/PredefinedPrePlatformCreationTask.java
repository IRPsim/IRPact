package de.unileipzig.irpact.core.simulation.tasks;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.process.ra.RAProcessPlan;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class PredefinedPrePlatformCreationTask extends PredefinedBinaryTask implements PrePlatformCreationTask {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PredefinedPrePlatformCreationTask.class);

    public static final int ID = -300;

    public static final int HELLO_WORLD = 1;
    public static final int FINANCIAL_COMPONENT = 2;

    public PredefinedPrePlatformCreationTask() {
        super(JsonUtil.SMILE.createObjectNode());
        setInfo(NO_INFO);
        setTask(NO_TASK);
    }

    public PredefinedPrePlatformCreationTask(byte[] content) throws IOException {
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
            case FINANCIAL_COMPONENT:
//                callFinancialComponent(environment);
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

//    private void callFinancialComponent(SimulationEnvironment environment) {
//        LOGGER.trace("call FinancialComponent-Task ({})", getInfo());
//        int year = environment.getSettings().getFirstSimulationYear();
//        RAProcessModel model = (RAProcessModel) CollectionUtil.get(environment.getProcessModels().getProcessModels(), 0);
//        environment.getAgents().streamConsumerAgents()
//                .forEach(ca -> fcLogCa(year, model, ca));
//    }

//    private void fcLogCa(int year, RAProcessModel model, ConsumerAgent ca) {
//        RAProcessPlan plan = (RAProcessPlan) model.newPlan(ca, null, null);
//        double fc = plan.getFinancialComponent(year);
//        int id = ca.findAttribute(RAConstants.ID).asValueAttribute().getIntValue();
//        String milieu = ca.getGroup().getName();
//        IRPLogging.getResultLogger().trace("[FCX] {},{},{}", id, milieu, fc);
//    }
}
