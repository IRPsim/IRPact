package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.preference.ValueConfiguration;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicSimulationConfiguration implements SimulationConfiguration {

    protected ConsumerAgentGroupAffinitiesMapping affinitiesMapping;
    protected ValueConfiguration<ProductGroupAttribute> productValues;
    protected Map<String, ProductGroup> productGroups;
    protected Map<String, SimulationEntity> entitiyMap;
    protected Map<EntityType, Set<SimulationEntity>> partitionedEntitiyMap;

    public BasicSimulationConfiguration(
            ConsumerAgentGroupAffinitiesMapping affinitiesMapping,
            ValueConfiguration<ProductGroupAttribute> productValues,
            Map<String, ProductGroup> productGroups,
            Map<String, SimulationEntity> entitiyMap,
            Map<EntityType, Set<SimulationEntity>> partitionedEntitiyMap) {
        this.affinitiesMapping = Check.requireNonNull(affinitiesMapping, "affinitiesMapping");
        this.productValues = Check.requireNonNull(productValues, "productValues");
        this.productGroups = Check.requireNonNull(productGroups, "productGroups");
        this.entitiyMap = Check.requireNonNull(entitiyMap, "entitiyMap");
        this.partitionedEntitiyMap = Check.requireNonNull(partitionedEntitiyMap, "partitionedEntitiyMap");
    }

    //=========================
    //General
    //=========================

    @Override
    public ConsumerAgentGroupAffinitiesMapping getAffinitiesMapping() {
        return affinitiesMapping;
    }

    @Override
    public ValueConfiguration<ProductGroupAttribute> getProductValues() {
        return productValues;
    }

    //=========================
    //Products
    //=========================

    public boolean add(ProductGroup group) {
        if(productGroups.containsKey(group.getName())) {
            return false;
        }
        productGroups.put(group.getName(), group);
        return true;
    }

    @Override
    public Collection< ? extends ProductGroup> getProductGroups() {
        return productGroups.values();
    }

    @Override
    public ProductGroup getProductGroup(String name) {
        return productGroups.get(name);
    }

    //=========================
    //Entities
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
    //util
    //=========================

    public boolean isPartition(EntityType type) {
        return partitionedEntitiyMap.containsKey(type);
    }

    public boolean registerType(SimulationEntity entity) {
        boolean changed = false;
        for(Map.Entry<EntityType, Set<SimulationEntity>> entry: partitionedEntitiyMap.entrySet()) {
            if(entity.is(entry.getKey())) {
                entry.getValue().add(entity);
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

    @Override
    public boolean register(SimulationEntity entity) {
        String name = entity.getName();
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
}
