package de.unileipzig.irpact.core;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Set;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public abstract class AbstractGroup<T> extends SimulationEntityBase implements Group<T> {

    protected Set<T> entities;

    public AbstractGroup(SimulationEnvironment environment, String name, Set<T> entities) {
        super(environment, name);
        this.entities = Check.requireNonNull(entities, "entities");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<T> getEntities() {
        return entities;
    }

    @Override
    public boolean addEntity(T entitiy) {
        return entities.add(entitiy);
    }
}
