package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public abstract class SpatialInformationAgentBase extends AgentBase implements SpatialInformationAgent {

    protected double informationAuthority;
    protected SpatialInformation spatialInformation;

    public SpatialInformationAgentBase(
            SimulationEnvironment environment,
            String name,
            double informationAuthority,
            SpatialInformation spatialInformation) {
        super(environment, name);
        this.informationAuthority = informationAuthority;
        this.spatialInformation = Check.requireNonNull(spatialInformation, "spatialInformation");
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }

    @Override
    public AgentIdentifier getIdentifier() {
        throw new UnsupportedOperationException();
    }
}
