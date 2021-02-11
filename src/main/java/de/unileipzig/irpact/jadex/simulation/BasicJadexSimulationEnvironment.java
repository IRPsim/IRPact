package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.simulation.InitializationData;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexSimulationEnvironment implements JadexSimulationEnvironment {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexSimulationEnvironment.class);

    protected InitializationData initializationData;
    protected BasicAgentManager agentManager = new BasicAgentManager();
    protected BasicSocialNetwork socialNetwork = new BasicSocialNetwork();
    protected BasicProductManager productManager = new BasicProductManager();
    protected SpatialModel spatialModel;
    protected JadexTimeModel timeModel;
    protected BasicJadexLifeCycleControl lifeCycleControl = new BasicJadexLifeCycleControl();
    protected Rnd rnd;

    public BasicJadexSimulationEnvironment() {
        agentManager.setEnvironment(this);
        socialNetwork.setEnvironment(this);
        productManager.setEnvironment(this);
    }

    @Override
    public InitializationData getInitializationData() {
        return initializationData;
    }

    public void setInitializationData(InitializationData initializationData) {
        this.initializationData = initializationData;
    }

    @Override
    public AgentManager getAgents() {
        return agentManager;
    }

    @Override
    public SocialNetwork getNetwork() {
        return socialNetwork;
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

    @Override
    public JadexLifeCycleControl getLiveCycleControl() {
        return lifeCycleControl;
    }

    @Override
    public Rnd getSimulationRandom() {
        return rnd;
    }

    public void setSimulationRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    @Override
    public void initialize() {
        agentManager.initialize();
        socialNetwork.initialize();
        productManager.initialize();
        spatialModel.initialize();
        timeModel.initialize();
        lifeCycleControl.initialize();
    }

    @Override
    public void validate() throws ValidationException {
        agentManager.validate();
        socialNetwork.validate();
        productManager.validate();
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
    }

    @Override
    public void replace(ConsumerAgent placeholder, ConsumerAgent real) throws IllegalStateException {
        ConsumerAgentGroup cag = placeholder.getGroup();
        cag.replace(placeholder, real);

        getNetwork().getGraph().replace(placeholder, real);
    }
}
