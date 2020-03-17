package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.core.simulation.BasicSimulationCache;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEntity;
import jadex.bridge.IExternalAccess;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@ToDo("testen, ob rwlock benoetigt wird")
public class BasicJadexSimulationCache extends BasicSimulationCache implements JadexSimulationCache {

    protected Map<String, IExternalAccess> accessMap;

    public BasicJadexSimulationCache(
            Map<String, SimulationEntity> entitiyMap,
            Map<EntityType, Set<SimulationEntity>> partitionedEntitiyMap,
            Map<String, IExternalAccess> accessMap) {
        super(entitiyMap, partitionedEntitiyMap);
        this.accessMap = accessMap;
    }

    //=========================
    //get
    //=========================

    @Override
    public Collection<? extends IExternalAccess> getAccesses() {
        return accessMap.values();
    }

    @Override
    public IExternalAccess getAccess(String name) {
        return accessMap.get(name);
    }

    //=========================
    //find
    //=========================

    //=========================
    //util
    //=========================

    @Override
    public boolean register(String name, IExternalAccess access) {
        if(accessMap.containsKey(name)) {
            return false;
        }
        accessMap.put(name, access);
        return true;
    }

    @Override
    public boolean register(String name, IExternalAccess access, SimulationEntity entity) {
        if(entitiyMap.containsKey(name) || accessMap.containsKey(name)) {
            return false;
        }
        entitiyMap.put(name, entity);
        accessMap.put(name, access);
        return true;
    }

    @Override
    public boolean unregister(String name) {
        if(super.unregister(name)) {
            accessMap.remove(name);
            return true;
        }
        return false;
    }
}
