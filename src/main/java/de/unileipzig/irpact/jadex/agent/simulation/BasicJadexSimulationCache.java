package de.unileipzig.irpact.jadex.agent.simulation;

import de.unileipzig.irpact.core.simulation.BasicSimulationCache;
import de.unileipzig.irpact.core.simulation.SimulationEntity;
import jadex.bridge.IExternalAccess;

import java.util.Collection;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicJadexSimulationCache extends BasicSimulationCache implements JadexSimulationCache {

    /*
    private Map<String, IExternalAccess> accessMap = new HashMap<>();
    private Map<String, IComponentIdentifier> componentIdentifierMap = new HashMap<>();
    private Map<String, JadexAgent> agentMap = new HashMap<>();
    private Map<Class<?>, Set<JadexAgent>> agentTypeMap = new HashMap<>();
     */
    protected Map<String, IExternalAccess> accessMap;

    public BasicJadexSimulationCache(
            Map<String, SimulationEntity> entitiyMap,
            Map<String, IExternalAccess> accessMap) {
        super(entitiyMap);
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
        if(accessMap.containsKey(name) || entitiyMap.containsKey(name)) {
            return false;
        }
        accessMap.put(name, access);
        entitiyMap.put(name, entity);
        return true;
    }
}
