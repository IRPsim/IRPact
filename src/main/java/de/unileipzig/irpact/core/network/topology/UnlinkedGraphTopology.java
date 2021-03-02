package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class UnlinkedGraphTopology extends NameableBase implements GraphTopologyScheme {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CompleteGraphTopology.class);

    protected SocialGraph.Type edgeType;

    public UnlinkedGraphTopology() {
    }

    public UnlinkedGraphTopology(SocialGraph.Type edgeType, String name) {
        setEdgeType(edgeType);
        setName(name);
    }

    public void setEdgeType(SocialGraph.Type edgeType) {
        this.edgeType = edgeType;
    }

    public SocialGraph.Type getEdgeType() {
        return edgeType;
    }

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "initialize unlinked graph");
        graph.removeAllEdges(edgeType);
    }

    @Override
    public int getHashCode() {
        return Objects.hash(getName(), getEdgeType().getHashCode());
    }
}
