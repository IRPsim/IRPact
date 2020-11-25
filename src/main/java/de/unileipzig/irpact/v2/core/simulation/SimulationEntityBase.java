package de.unileipzig.irpact.v2.core.simulation;

import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class SimulationEntityBase extends NameableBase implements SimulationEntity {

    protected SimulationEnvironment environment;

    public SimulationEntityBase() {
    }

    public SimulationEntityBase(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        return environment;
    }
}
