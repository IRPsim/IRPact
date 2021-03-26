package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.network.topology.GraphTopologyScheme;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicSocialNetwork implements SocialNetwork {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicSocialNetwork.class);

    protected SimulationEnvironment environment;
    protected GraphTopologyScheme topologyScheme;
    protected SocialGraph graph;

    public BasicSocialNetwork() {
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                topologyScheme.getChecksum(),
                graph.getChecksum()
        );
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void initDefaultGraph() {
        BasicSocialGraph socialGraph = new BasicSocialGraph(SupportedGraphStructure.DIRECTED_ADJACENCY_LIST_MULTI_GRAPH);
        setGraph(socialGraph);
    }

    public void setGraph(SocialGraph graph) {
        this.graph = graph;
    }

    @Override
    public SocialGraph getGraph() {
        return graph;
    }

    @Override
    public GraphTopologyScheme getGraphTopologyScheme() {
        return topologyScheme;
    }

    public void setGraphTopologyScheme(GraphTopologyScheme topologyScheme) {
        this.topologyScheme = topologyScheme;
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
        LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "added node: {}", agent.getName());
    }

    @Override
    public void postAgentCreation(boolean initialCall) {
        if(initialCall) {
            topologyScheme.initalize(
                    environment,
                    getGraph()
            );
        }
    }
}
