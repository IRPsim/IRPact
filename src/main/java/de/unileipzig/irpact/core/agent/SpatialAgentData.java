package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public class SpatialAgentData extends AgentData {

    public SpatialAgentData() {
    }

    protected SpatialInformation spatialInformation;
    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }
    public void setSpatialInformation(SpatialInformation spatialInformation) {
        this.spatialInformation = spatialInformation;
    }
}
