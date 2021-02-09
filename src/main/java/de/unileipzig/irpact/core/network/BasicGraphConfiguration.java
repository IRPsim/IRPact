package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.network.topology.GraphTopologyScheme;
import de.unileipzig.irpact.core.network.topology.TopologyManipulationScheme;

/**
 * @author Daniel Abitz
 */
public class BasicGraphConfiguration implements GraphConfiguration {

    protected GraphTopologyScheme topologyScheme;
    protected TopologyManipulationScheme manipulationScheme;

    public BasicGraphConfiguration() {
    }

    @Override
    public GraphTopologyScheme getGraphTopologyScheme() {
        return topologyScheme;
    }

    public void setGraphTopologyScheme(GraphTopologyScheme topologyScheme) {
        this.topologyScheme = topologyScheme;
    }

    @Override
    public TopologyManipulationScheme getTopologyManipulationScheme() {
        return manipulationScheme;
    }

    public void setTopologyManipulationScheme(TopologyManipulationScheme manipulationScheme) {
        this.manipulationScheme = manipulationScheme;
    }
}
