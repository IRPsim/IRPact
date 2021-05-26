package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
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
        BasicSocialGraph socialGraph = new BasicSocialGraph(SupportedGraphStructure.FAST_DIRECTED_MULTI_GRAPH2_CONCURRENT);
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
    public void postAgentCreation() throws InitializationException {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "initalize graph using topology '{}'", topologyScheme.getName());
        topologyScheme.initalize(
                environment,
                getGraph()
        );
    }
}
