package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Check;

/**
 * @author Daniel Abitz
 */
public abstract class SimulationEntityBase implements SimulationEntity {

    protected SimulationEnvironment environment;
    protected String name;

    public SimulationEntityBase() {
    }

    public SimulationEntityBase(SimulationEnvironment environment, String name) {
        this.environment = Check.requireNonNull(environment, "environment");
        this.name = Check.requireNonNull(name, "name");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        return environment;
    }
}
