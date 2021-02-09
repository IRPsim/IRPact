package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.LoggingPart;
import de.unileipzig.irpact.core.log.LoggingType;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicSocialNetwork implements SocialNetwork {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicSocialNetwork.class);

    protected SimulationEnvironment environment;
    protected SocialGraph graph = new BasicSocialGraph();
    protected GraphConfiguration configuration;

    public BasicSocialNetwork() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void setGraph(SocialGraph graph) {
        this.graph = graph;
    }

    @Override
    public SocialGraph getGraph() {
        return graph;
    }

    @Override
    public void setConfiguration(GraphConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GraphConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void initialize() {
        environment.getAgents()
                .streamConsumerAgents()
                .forEach(this::addNode);
    }

    protected void addNode(ConsumerAgent agent) {
        SocialGraph.Node node = getGraph().addAgentAndGetNode(agent);
        agent.setSocialGraphNode(node);
        LOGGER.trace(LoggingType.INITIALIZATION, LoggingPart.NETWORK, "added node: {}", agent.getName());
    }

    @Override
    public void validate() throws ValidationException {
    }
}
