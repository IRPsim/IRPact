package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductManager;
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

    protected AgentManager agentManager = new BasicAgentManager();
    protected SocialNetwork socialNetwork = new BasicSocialNetwork();
    protected ProductManager productManager = new BasicProductManager();
    protected SpatialModel spatialModel;
    protected JadexTimeModel timeModel;
    protected Rnd rnd;

    public BasicJadexSimulationEnvironment() {
        ((BasicAgentManager) agentManager).setEnvironment(this);
        ((BasicSocialNetwork) socialNetwork).setEnvironment(this);
        ((BasicProductManager) productManager).setEnvironment(this);
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
    }

    @Override
    public void validate() throws ValidationException {
        agentManager.validate();
        socialNetwork.validate();
        productManager.validate();
        spatialModel.validate();
        timeModel.validate();
    }

    @Override
    public void setup() {
        socialNetwork.getConfiguration().getGraphTopologyScheme().initalize(
                this,
                socialNetwork.getGraph()
        );
    }
}
