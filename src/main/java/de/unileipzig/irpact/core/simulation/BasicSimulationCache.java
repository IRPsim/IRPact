package de.unileipzig.irpact.core.simulation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicSimulationCache implements SimulationCache {

    protected Map<String, SimulationEntity> entitiyMap;
    protected Map<EntityType, Set<SimulationEntity>> partitionedEntitiyMap;

    public BasicSimulationCache(
            Map<String, SimulationEntity> entitiyMap,
            Map<EntityType, Set<SimulationEntity>> partitionedEntitiyMap) {
        this.entitiyMap = entitiyMap;
        this.partitionedEntitiyMap = partitionedEntitiyMap;
    }

    //=========================
    //extra
    //=========================

    public boolean isPartition(EntityType type) {
        return partitionedEntitiyMap.containsKey(type);
    }

    public boolean registerType(SimulationEntity entity) {
        boolean changed = false;
        for(EntityType type: partitionedEntitiyMap.keySet()) {
            if(entity.is(type)) {
                partitionedEntitiyMap.get(type).add(entity);
                changed = true;
            }
        }
        return changed;
    }

    public boolean unregisterType(SimulationEntity entity) {
        boolean changed = false;
        for(Set<SimulationEntity> set: partitionedEntitiyMap.values()) {
            changed = set.remove(entity);
        }
        return changed;
    }

    //=========================
    //get
    //=========================

    @Override
    public Collection<? extends SimulationEntity> getEntities() {
        return entitiyMap.values();
    }

    @Override
    public Collection<? extends SimulationEntity> getPartitionedEntities(EntityType type) {
        return partitionedEntitiyMap.get(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends SimulationEntity> T getEntity(String entitiyName) {
        return (T) entitiyMap.get(entitiyName);
    }

    //=========================
    //find
    //=========================

    //=========================
    //util
    //=========================

    @Override
    public boolean register(String name, SimulationEntity entity) {
        if(entitiyMap.containsKey(name)) {
            return false;
        }
        entitiyMap.put(name, entity);
        return true;
    }

    @Override
    public boolean unregister(String name) {
        SimulationEntity entity = entitiyMap.remove(name);
        if(entity == null) {
            return false;
        }
        unregisterType(entity);
        return true;
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
}
