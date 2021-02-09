package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public abstract class SpatialAgentBase extends AgentBase implements SpatialAgent {

    protected SpatialInformation spatialInformation;

    public void setSpatialInformation(SpatialInformation spatialInformation) {
        this.spatialInformation = spatialInformation;
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }
}
