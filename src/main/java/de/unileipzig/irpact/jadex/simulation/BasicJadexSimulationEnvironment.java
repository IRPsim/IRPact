package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.simulation.InitializationData;
import de.unileipzig.irpact.core.simulation.Version;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexSimulationEnvironment implements JadexSimulationEnvironment {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexSimulationEnvironment.class);

    protected final Lock REPLACE_LOCK = new ReentrantLock();

    protected InitializationData initializationData;
    protected AgentManager agentManager;
    protected SocialNetwork socialNetwork;
    protected ProcessModelManager processModelManager;
    protected ProductManager productManager;
    protected SpatialModel spatialModel;
    protected JadexTimeModel timeModel;
    protected JadexLifeCycleControl lifeCycleControl;
    protected Version version;
    protected Rnd rnd;

    public BasicJadexSimulationEnvironment() {
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
        return null;
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

    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    public void setSimulationRandom(Rnd rnd) {
        this.rnd = rnd;
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
    public void setup() {
        socialNetwork.getConfiguration().getGraphTopologyScheme().initalize(
                this,
                socialNetwork.getGraph()
        );
        processModelManager.setup();
    }

    @Override
    public void replace(ConsumerAgent placeholder, ConsumerAgent real) throws IllegalStateException {
        REPLACE_LOCK.lock();
        try {
            ConsumerAgentGroup cag = placeholder.getGroup();
            cag.replace(placeholder, real);

            getNetwork().getGraph().replace(placeholder, real);
        } finally {
            REPLACE_LOCK.unlock();
        }
    }
}
