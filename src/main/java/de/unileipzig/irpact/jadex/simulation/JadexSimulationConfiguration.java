package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.SimulationConfiguration;
import de.unileipzig.irpact.core.simulation.SimulationEntity;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public interface JadexSimulationConfiguration extends SimulationConfiguration {

    //=========================
    //Entities
    //=========================

    boolean hasAccess(IExternalAccess access);

    Collection<? extends IExternalAccess> getAccesses();

    IExternalAccess getAccess(String name);

    default IComponentIdentifier getIdentifier(String name) {
        IExternalAccess access = getAccess(name);
        return access == null
                ? null
                : access.getId();
    }

    default <T extends SimulationEntity> T getEntity(IComponentIdentifier identifier) {
        return getEntity(identifier.getLocalName());
    }

    default IExternalAccess findAccess(String name) throws NoSuchElementException {
        IExternalAccess access = getAccess(name);
        if(access == null) {
            throw new NoSuchElementException(name);
        }
        return access;
    }

    default IComponentIdentifier findIdentifier(String name) throws NoSuchElementException {
        IComponentIdentifier identifier = getIdentifier(name);
        if(identifier == null) {
            throw new NoSuchElementException(name);
        }
        return identifier;
    }

    default <T extends SimulationEntity> T findEntity(IComponentIdentifier identifier) throws NoSuchElementException {
        T entity = getEntity(identifier);
        if(entity == null) {
            throw new NoSuchElementException(identifier.getLocalName());
        }
        return entity;
    }

    //=========================
    //util
    //=========================

    boolean register(IExternalAccess access);

    boolean register(IExternalAccess access, SimulationEntity entity);
}
