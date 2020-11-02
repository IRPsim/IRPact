package de.unileipzig.irpact.v2.core.agent;

import de.unileipzig.irpact.v2.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public class SpatialAgentBase extends AgentBase implements SpatialAgent {

    protected SpatialInformation spatialInformation;

    public void setSpatialInformation(SpatialInformation spatialInformation) {
        this.spatialInformation = spatialInformation;
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }
}
