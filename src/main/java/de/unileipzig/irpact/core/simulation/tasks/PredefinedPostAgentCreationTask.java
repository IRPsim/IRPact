package de.unileipzig.irpact.core.simulation.tasks;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class PredefinedPostAgentCreationTask extends PredefinedBinaryTask implements PostAgentCreationTask {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PredefinedPostAgentCreationTask.class);

    public static final int ID = -200;

    public static final int HELLO_WORLD = 1;
    public static final int ADD_ONE_AGENT_TO_EVERY_GROUP = 2;

    public PredefinedPostAgentCreationTask() {
        super(IRPactJson.SMILE.createObjectNode());
        setInfo(NO_INFO);
        setTask(NO_TASK);
    }

    public PredefinedPostAgentCreationTask(byte[] content) throws IOException {
        super((ObjectNode) IRPactJson.fromBytesWithSmile(content));
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

            case ADD_ONE_AGENT_TO_EVERY_GROUP:
                addOneAgentToEveryGroup(environment);
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

    private void addOneAgentToEveryGroup(SimulationEnvironment environment) {
        LOGGER.trace("called 'addOneAgentToEveryGroup' ('{}')", environment.getName());
        AgentManager manager = environment.getAgents();
        SocialGraph graph = environment.getNetwork().getGraph();
        for(ConsumerAgentGroup cag: manager.getConsumerAgentGroups()) {
            ConsumerAgent newAgent = cag.deriveAgent();
            cag.addAgent(newAgent);
            graph.addAgent(newAgent);
            LOGGER.trace("added new agent '{}' to '{}'", newAgent.getName(), cag.getName());
        }
    }
}
