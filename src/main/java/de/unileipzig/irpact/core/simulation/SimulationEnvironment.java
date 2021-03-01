package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.misc.Initialization;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.persistence.PersistenceModul;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.core.time.TimeModel;

/**
 * @author Daniel Abitz
 */
public interface SimulationEnvironment extends Nameable, Initialization {

    //=========================
    //general
    //=========================

    //=========================
    //main components
    //=========================

    InitializationData getInitializationData();

    AgentManager getAgents();

    SocialNetwork getNetwork();

    ProcessModelManager getProcessModels();

    ProductManager getProducts();

    SpatialModel getSpatialModel();

    TimeModel getTimeModel();

    LifeCycleControl getLiveCycleControl();

    //=========================
    //util
    //=========================

    Version getVersion();

    ResourceLoader getResourceLoader();

    BinaryTaskManager getTaskManager();

    PersistenceModul getPersistenceModul();

    Rnd getSimulationRandom();
}
