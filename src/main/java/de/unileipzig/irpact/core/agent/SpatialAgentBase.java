package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public abstract class SpatialAgentBase extends AgentBase implements SpatialAgent {

    protected SpatialInformation spatialInformation;

    public SpatialAgentBase(SimulationEnvironment environment, String name, SpatialInformation spatialInformation) {
        super(environment, name);
        this.spatialInformation = Check.requireNonNull(spatialInformation, "spatialInformation");
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }
}
