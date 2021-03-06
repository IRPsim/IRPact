package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.util.ProgressCalculator;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.data.DataStore;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.persistence.PersistenceModul;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.core.time.TimeModel;
import de.unileipzig.irpact.core.util.AttributeHelper;
import de.unileipzig.irpact.core.util.MetaData;

/**
 * @author Daniel Abitz
 */
public interface SimulationEnvironment extends Nameable, InitalizablePart {

    //=========================
    //general
    //=========================

    boolean isRestored();

    void register(CloseableSimulationEntity entity);

    boolean isRegistered(CloseableSimulationEntity entity);

    default boolean registerIfNotRegistered(CloseableSimulationEntity entity) {
        if(isRegistered(entity)) {
            return false;
        } else {
            register(entity);
            return true;
        }
    }

    boolean unregister(CloseableSimulationEntity entity);

    void closeEntities();

    //=========================
    //InitalizablePart extra
    //=========================

    void createAgents() throws InitializationException;

    //=========================
    //main components
    //=========================

    MetaData getMetaData();

    Settings getSettings();

    AgentManager getAgents();

    SocialNetwork getNetwork();

    ProcessModelManager getProcessModels();

    ProductManager getProducts();

    SpatialModel getSpatialModel();

    TimeModel getTimeModel();

    LifeCycleControl getLifeCycleControl();

    //=========================
    //util
    //=========================

    Version getVersion();

    ResourceLoader getResourceLoader();

    BinaryTaskManager getTaskManager();

    PersistenceModul getPersistenceModul();

    Rnd getSimulationRandom();

    AttributeHelper getAttributeHelper();

    DataStore getGlobalData();

    ProgressCalculator getProgressCalculator();

    DataAnalyser getDataAnalyser();
}
