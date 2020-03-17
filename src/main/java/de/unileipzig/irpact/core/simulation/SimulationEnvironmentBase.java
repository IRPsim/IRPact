package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.concurrent.ResettableTimer;
import de.unileipzig.irpact.core.network.AgentNetwork;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import org.slf4j.Logger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public abstract class SimulationEnvironmentBase implements SimulationEnvironment {

    //cache
    //protected Map<String, Identifier> identifierMap = new HashMap<>();
    //protected Map<Identifier, SimulationEntity> entityMap = new HashMap<>();
    protected Map<Class<?>, Set<SimulationEntity>> entityTypeMap = new HashMap<>();
    //para
    protected Timestamp.Mode mode = Timestamp.Mode.SYSTEM;
    protected SpatialModel spatialModel;
    protected AgentNetwork agentNetwork;
    protected EconomicSpace economicSpace;
    protected ResettableTimer aktivityTimer;
    protected Logger logger;

    public SimulationEnvironmentBase() {
    }

    //=========================
    //new
    //=========================

    public void setSpatialModel(SpatialModel spatialModel) {
        this.spatialModel = spatialModel;
    }

    public void setEconomicSpace(EconomicSpace economicSpace) {
        this.economicSpace = economicSpace;
    }

    public void setAgentNetwork(AgentNetwork agentNetwork) {
        this.agentNetwork = agentNetwork;
    }

    public void setTimer(ResettableTimer aktivityTimer) {
        this.aktivityTimer = aktivityTimer;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    //=========================
    //SimulationEnvironment
    //=========================

    @Override
    public void setTimeMode(Timestamp.Mode mode) {
        this.mode = mode;
    }

    /*
    @Override
    public Identifier getIdentifier(String entitiyName) {
        return identifierMap.get(entitiyName);
    }
    */

    @Override
    public Timestamp getTimestamp() {
        return new Timestamp(
                mode,
                getSystemTime(),
                getSimulationTime(),
                getTick()
        );
    }

    /*
    @SuppressWarnings("unchecked")
    @Override
    public <T extends SimulationEntity> T getEntity(Identifier identifier) {
        return (T) entityMap.get(identifier);
    }

    @Override
    public String getName(Identifier identifier) {
        SimulationEntity entity = getEntity(identifier);
        return entity == null
                ? null
                : entity.getName();
    }

    @Override
    public synchronized void register(Identifier identifier, String name, SimulationEntity entity) {
        if(identifierMap.containsKey(name)) {
            throw new IllegalStateException("Identifier already exists: " + name);
        }
        if(entityMap.containsKey(identifier)) {
            throw new IllegalStateException("SimulationEntity already exists, name = " + name);
        }
        identifierMap.put(name, identifier);
        entityMap.put(identifier, entity);
        Set<SimulationEntity> subSet = entityTypeMap.computeIfAbsent(identifier.getClass(), _identifierType -> new HashSet<>());
        subSet.add(entity);
    }

    @Override
    public <T extends SimulationEntity> void forEach(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Consumer<T> consumer) {
        forEach(idType, entitiyType, null, consumer);
    }

    @Override
    public <T extends SimulationEntity> void forEach(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Predicate<T> filter,
            Consumer<T> consumer) {
        for(Map.Entry<Class<?>, Set<SimulationEntity>> entry: entityTypeMap.entrySet()) {
            Class<?> type = entry.getKey();
            if(idType.equals(type)) {
                Set<SimulationEntity> entities = entry.getValue();
                for(SimulationEntity entity: entities) {
                    T value = entitiyType.cast(entity);
                    if(filter == null || filter.test(value)) {
                        consumer.accept(value);
                    }
                }
            }
        }
    }

    @Override
    public <T extends SimulationEntity> Collection<T> select(
            Class<? extends Identifier> idType,
            Class<T> entitiyType) {
        return select(idType, entitiyType, null);
    }

    @Override
    public <T extends SimulationEntity> Collection<T> select(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Predicate<T> filter) {
        List<T> result = new ArrayList<>();
        for(Map.Entry<Class<?>, Set<SimulationEntity>> entry: entityTypeMap.entrySet()) {
            Class<?> type = entry.getKey();
            if(idType.equals(type)) {
                Set<SimulationEntity> entities = entry.getValue();
                for(SimulationEntity entity: entities) {
                    T value = entitiyType.cast(entity);
                    if(filter == null || filter.test(value)) {
                        result.add(value);
                    }
                }
            }
        }
        return result;
    }
    */

    @Override
    public SpatialModel getSpatialModel() {
        return spatialModel;
    }

    @Override
    public EconomicSpace getEconomicSpace() {
        return economicSpace;
    }

    @Override
    public AgentNetwork getAgentNetwork() {
        return agentNetwork;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void poke() {
        if(aktivityTimer != null) {
            aktivityTimer.reset();
        }
    }
}
