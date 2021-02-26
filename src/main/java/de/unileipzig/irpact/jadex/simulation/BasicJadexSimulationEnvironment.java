package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.persistence.PersistenceModul;
import de.unileipzig.irpact.core.process.BasicProcessModelManager;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.simulation.*;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.jadex.persistance.JadexPersistenceModul;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexSimulationEnvironment implements JadexSimulationEnvironment {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexSimulationEnvironment.class);

    protected InitializationData initializationData;
    protected AgentManager agentManager;
    protected SocialNetwork socialNetwork;
    protected ProcessModelManager processModelManager;
    protected ProductManager productManager;
    protected SpatialModel spatialModel;
    protected JadexTimeModel timeModel;
    protected JadexLifeCycleControl lifeCycleControl;
    protected ResourceLoader resourceLoader;
    protected BinaryTaskManager taskManager;
    protected PersistenceModul persistenceModul;
    protected Rnd rnd;

    public BasicJadexSimulationEnvironment() {
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                agentManager.getHashCode(),
                socialNetwork.getHashCode(),
                processModelManager.getHashCode(),
                productManager.getHashCode(),
                spatialModel.getHashCode(),
                timeModel.getHashCode(),
                lifeCycleControl.getHashCode(),
                rnd.getHashCode()
        );
    }

    public void initDefault() {
        BasicInitializationData initData = new BasicInitializationData();
        BasicAgentManager agentManager = new BasicAgentManager();
        BasicSocialNetwork socialNetwork = new BasicSocialNetwork();
        BasicProcessModelManager processModelManager = new BasicProcessModelManager();
        BasicProductManager productManager = new BasicProductManager();
        BasicJadexLifeCycleControl lifeCycleControl = new BasicJadexLifeCycleControl();
        BasicBinaryTaskManager taskManager = new BasicBinaryTaskManager();
        JadexPersistenceModul persistenceModul = new JadexPersistenceModul();

        setInitializationData(initData);
        setResourceLoader(resourceLoader);

        setAgentManager(agentManager);
        agentManager.setEnvironment(this);

        setSocialNetwork(socialNetwork);
        socialNetwork.setEnvironment(this);

        setProductManager(productManager);
        productManager.setEnvironment(this);

        setProcessModels(processModelManager);
        processModelManager.setEnvironment(this);

        setLifeCycleControl(lifeCycleControl);
        lifeCycleControl.setEnvironment(this);

        setTaskManager(taskManager);
        taskManager.setEnvironment(this);

        setPersistenceModul(persistenceModul);
        persistenceModul.setEnvironment(this);
    }

    @Override
    public InitializationData getInitializationData() {
        return initializationData;
    }

    public void setInitializationData(InitializationData initializationData) {
        this.initializationData = initializationData;
    }

    public void setAgentManager(AgentManager agentManager) {
        this.agentManager = agentManager;
    }

    @Override
    public AgentManager getAgents() {
        return agentManager;
    }

    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    @Override
    public SocialNetwork getNetwork() {
        return socialNetwork;
    }

    public void setProcessModels(ProcessModelManager processModelManager) {
        this.processModelManager = processModelManager;
    }

    @Override
    public ProcessModelManager getProcessModels() {
        return processModelManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    @Override
    public ProductManager getProducts() {
        return productManager;
    }

    public void setSpatialModel(SpatialModel spatialModel) {
        this.spatialModel = spatialModel;
    }

    @Override
    public SpatialModel getSpatialModel() {
        return spatialModel;
    }

    public void setTimeModel(JadexTimeModel timeModel) {
        this.timeModel = timeModel;
    }

    @Override
    public JadexTimeModel getTimeModel() {
        return timeModel;
    }

    public void setLifeCycleControl(JadexLifeCycleControl lifeCycleControl) {
        this.lifeCycleControl = lifeCycleControl;
    }

    @Override
    public JadexLifeCycleControl getLiveCycleControl() {
        return lifeCycleControl;
    }

    @Override
    public Version getVersion() {
        return IRPact.VERSION;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setSimulationRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    @Override
    public BinaryTaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(BinaryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public PersistenceModul getPersistenceModul() {
        return persistenceModul;
    }

    public void setPersistenceModul(PersistenceModul persistenceModul) {
        this.persistenceModul = persistenceModul;
    }

    @Override
    public Rnd getSimulationRandom() {
        return rnd;
    }

    @Override
    public void initialize() {
        agentManager.initialize();
        socialNetwork.initialize();
        productManager.initialize();
        processModelManager.initialize();
        spatialModel.initialize();
        timeModel.initialize();
        lifeCycleControl.initialize();
    }

    @Override
    public void validate() throws ValidationException {
        agentManager.validate();
        socialNetwork.validate();
        productManager.validate();
        processModelManager.validate();
        spatialModel.validate();
        timeModel.validate();
        lifeCycleControl.validate();
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        agentManager.preAgentCreation();
        socialNetwork.preAgentCreation();
        productManager.preAgentCreation();
        processModelManager.preAgentCreation();
        spatialModel.preAgentCreation();
        timeModel.preAgentCreation();
        lifeCycleControl.preAgentCreation();
    }

    @Override
    public void postAgentCreation() throws MissingDataException {
        agentManager.postAgentCreation();
        socialNetwork.postAgentCreation();
        productManager.postAgentCreation();
        processModelManager.postAgentCreation();
        spatialModel.postAgentCreation();
        timeModel.postAgentCreation();
        lifeCycleControl.postAgentCreation();
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        agentManager.preSimulationStart();
        socialNetwork.preSimulationStart();
        productManager.preSimulationStart();
        processModelManager.preSimulationStart();
        spatialModel.preSimulationStart();
        timeModel.preSimulationStart();
        lifeCycleControl.preSimulationStart();
    }
}
