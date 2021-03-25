package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class CompleteGraphTopology extends NameableBase implements GraphTopologyScheme {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CompleteGraphTopology.class);

    protected SocialGraph.Type edgeType;
    protected double initialWeight;

    public CompleteGraphTopology() {
    }

    public CompleteGraphTopology(
            SocialGraph.Type edgeType,
            String name,
            double initialWeight) {
        setEdgeType(edgeType);
        setName(name);
        setInitialWeight(initialWeight);
    }

    public void setEdgeType(SocialGraph.Type edgeType) {
        this.edgeType = edgeType;
    }

    public SocialGraph.Type getEdgeType() {
        return edgeType;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "initialize complete graph");
        for(SocialGraph.Node src: graph.getNodes()) {
            SocialGraph.LinkageInformation srcLi = graph.getLinkageInformation(src);
            for(SocialGraph.Node tar: graph.getNodes()) {
                if(src == tar) {
                    continue;
                }
                LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "add edge: {}->{} ({},{})", src.getLabel(), tar.getLabel(), edgeType, initialWeight);
                graph.addEdge(src, tar, edgeType, initialWeight);

                srcLi.inc(tar.getAgent(ConsumerAgent.class).getGroup(), edgeType);
            }
        }
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                getInitialWeight(),
                getEdgeType().getHashCode()
        );
    }
}
