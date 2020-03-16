package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.annotation.Experimental;
import de.unileipzig.irpact.core.message.MessageSystem;
import de.unileipzig.irpact.core.network.AgentNetwork;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public interface SimulationEnvironment {

    SpatialModel getSpatialModel();

    MessageSystem getMessageSystem();

    AgentNetwork getAgentNetwork();

    EconomicSpace getEconomicSpace();

    Logger getLogger();

    //=========================
    //time
    //=========================

    void setTimeMode(Timestamp.Mode mode);

    long getStarttime();

    default long getSimulationTimeSinceStart() {
        return getSimulationTime() - getStarttime();
    }

    long getSimulationTime();

    double getTick();

    long getSystemTime();

    Timestamp getTimestamp();

    //=========================
    //cache
    //=========================

    Identifier getIdentifier(String entitiyName);

    <T extends SimulationEntity> T getEntity(Identifier identifier);

    String getName(Identifier identifier);

    void register(Identifier identifier, String name, SimulationEntity entity);

    <T extends SimulationEntity> void forEach(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Consumer<T> consumer);

    <T extends SimulationEntity> void forEach(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Predicate<T> filter,
            Consumer<T> consumer);

    <T extends SimulationEntity> Collection<T> select(
            Class<? extends Identifier> idType,
            Class<T> entitiyType);

    <T extends SimulationEntity> Collection<T> select(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Predicate<T> filter);

    //=========================
    //util
    //=========================

    @Experimental
    void poke();
}
