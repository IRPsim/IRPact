package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.preference.ValueConfiguration;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.core.simulation.BasicSimulationConfiguration;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEntity;
import jadex.bridge.IExternalAccess;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@ToDo("testen, ob rwlock benoetigt wird")
public class BasicJadexSimulationConfiguration extends BasicSimulationConfiguration
        implements JadexSimulationConfiguration {

    protected Map<String, IExternalAccess> accessMap;

    public BasicJadexSimulationConfiguration(
            ConsumerAgentGroupAffinitiesMapping affinitiesMapping,
            ValueConfiguration<ProductGroupAttribute> productValues,
            Map<String, ProductGroup> productGroups,
            Map<String, SimulationEntity> entitiyMap,
            Map<EntityType, Set<SimulationEntity>> partitionedEntitiyMap,
            Set<Product> historicalProducts,
            Map<String, IExternalAccess> accessMap) {
        super(affinitiesMapping, productValues, productGroups, entitiyMap, partitionedEntitiyMap, historicalProducts);
        this.accessMap = accessMap;
    }

    //=========================
    //Entities
    //=========================

    @Override
    public boolean hasAccess(IExternalAccess access) {
        String name = access.getId().getLocalName();
        return accessMap.get(name) == access;
    }

    @Override
    public Collection<? extends IExternalAccess> getAccesses() {
        return accessMap.values();
    }

    @Override
    public IExternalAccess getAccess(String name) {
        return accessMap.get(name);
    }

    //=========================
    //util
    //=========================

    @Override
    public boolean register(IExternalAccess access) {
        String name = access.getId()
                .getLocalName();
        if(accessMap.containsKey(name)) {
            return false;
        }
        accessMap.put(name, access);
        return true;
    }

    @Override
    public boolean register(IExternalAccess access, SimulationEntity entity) {
        String accessName = access.getId()
                .getLocalName();
        String entityName = entity.getName();
        if(!Objects.equals(accessName, entityName)) {
            throw new IllegalArgumentException("Name missmatch: " + accessName + " != " + entityName);
        }
        if(entitiyMap.containsKey(entityName) || accessMap.containsKey(entityName)) {
            return false;
        }
        entitiyMap.put(entityName, entity);
        accessMap.put(entityName, access);
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
