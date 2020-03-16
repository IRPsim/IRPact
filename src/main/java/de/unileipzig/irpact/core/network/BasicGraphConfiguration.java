package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public class BasicGraphConfiguration implements GraphConfiguration {

    private GraphTopologyScheme graphTopologyScheme;
    private TopologyManipulationScheme topologyManipulationScheme;
    private EdgeWeightManipulationScheme edgeWeightManipulationScheme;

    public BasicGraphConfiguration(
            GraphTopologyScheme graphTopologyScheme,
            TopologyManipulationScheme topologyManipulationScheme,
            EdgeWeightManipulationScheme edgeWeightManipulationScheme) {
        this.graphTopologyScheme = graphTopologyScheme;
        this.topologyManipulationScheme = topologyManipulationScheme;
        this.edgeWeightManipulationScheme = edgeWeightManipulationScheme;
    }

    @Override
    public GraphTopologyScheme getGraphTopologyScheme() {
        return graphTopologyScheme;
    }

    @Override
    public TopologyManipulationScheme getTopologyManipulationScheme() {
        return topologyManipulationScheme;
    }

    @Override
    public EdgeWeightManipulationScheme getEdgeWeightManipulationScheme() {
        return edgeWeightManipulationScheme;
    }
}
