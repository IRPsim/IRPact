package de.unileipzig.irpact.jadex.agent.simulation;

import de.unileipzig.irpact.core.simulation.SimulationCache;
import de.unileipzig.irpact.core.simulation.SimulationEntity;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public interface JadexSimulationCache extends SimulationCache {

    //=========================
    //get
    //=========================

    Collection<? extends IExternalAccess> getAccesses();

    IExternalAccess getAccess(String name);

    default IComponentIdentifier getIdentifier(String name) {
        IExternalAccess access = getAccess(name);
        return access == null
                ? null
                : access.getId();
    }

    //=========================
    //find
    //=========================

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

    //=========================
    //util
    //=========================

    boolean register(String name, IExternalAccess access);

    boolean register(String name, IExternalAccess access, SimulationEntity entity);
}
