package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgent;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.preference.ProductGroupAttributValueConfiguration;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface SimulationConfiguration {

    //=========================
    //General
    //=========================

    ConsumerAgentGroupAffinitiesMapping getAffinitiesMapping();

    ProductGroupAttributValueConfiguration getProductValues();

    //=========================
    //Products
    //=========================

    Collection<? extends ProductGroup> getProductGroups();

    ProductGroup getProductGroup(String name);

    Collection<? extends Product> getHistroicalProducts();

    boolean addHistoricalProduct(Product product);

    //=========================
    //Entities
    //=========================

    Collection<? extends SimulationEntity> getEntities();

    Collection<? extends SimulationEntity> getPartitionedEntities(EntityType type);

    @SuppressWarnings("unchecked")
    default Collection<? extends ConsumerAgent> getConsumerAgents() {
        return (Collection<? extends ConsumerAgent>) getPartitionedEntities(EntityType.CONSUMER_AGENT);
    }

    @SuppressWarnings("unchecked")
    default Collection<? extends PointOfSaleAgent> getPointOfSaleAgents() {
        return (Collection<? extends PointOfSaleAgent>) getPartitionedEntities(EntityType.POINT_OF_SALE_AGENT);
    }

    <T extends SimulationEntity> T getEntity(String entitiyName);

    default <T extends SimulationEntity> T findEntity(String entitiyName) throws NoSuchElementException {
        T entity = getEntity(entitiyName);
        if(entity == null) {
            throw new NoSuchElementException(entitiyName);
        }
        return entity;
    }

    Stream<SimulationEntity> streamEntities();

    Stream<SimulationEntity> streamPartitionedEntities(EntityType type);

    //=========================
    //util
    //=========================

    boolean register(SimulationEntity entity);

    boolean unregister(String name);
}
