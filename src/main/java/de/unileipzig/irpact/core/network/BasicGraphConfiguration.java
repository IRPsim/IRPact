package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.network.topology.GraphTopologyScheme;

/**
 * @author Daniel Abitz
 */
public class BasicGraphConfiguration implements GraphConfiguration {

    protected GraphTopologyScheme topologyScheme;

    public BasicGraphConfiguration() {
    }

    @Override
    public GraphTopologyScheme getGraphTopologyScheme() {
        return topologyScheme;
    }

    public void setGraphTopologyScheme(GraphTopologyScheme topologyScheme) {
        this.topologyScheme = topologyScheme;
    }
}
