package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.misc.DebugLevel;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class BasicJadexSimulationEnvironment implements JadexSimulationEnvironment {

    protected AgentManager agentManager = new BasicAgentManager();
    protected SocialNetwork socialNetwork = new BasicSocialNetwork();
    protected ProductManager productManager = new BasicProductManager();
    protected SpatialModel spatialModel;
    protected JadexTimeModel timeModel;
    protected JadexSimulationControl simulationControl;

    protected DebugLevel debugLevel = DebugLevel.DEFAULT;

    public BasicJadexSimulationEnvironment() {
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

    public void setSimulationControl(JadexSimulationControl simulationControl) {
        this.simulationControl = simulationControl;
    }

    @Override
    public JadexSimulationControl getSimulationControl() {
        return simulationControl;
    }

    public void setDebugLevel(DebugLevel debugLevel) {
        this.debugLevel = debugLevel;
    }

    @Override
    public DebugLevel getDebugLevel() {
        return debugLevel;
    }
}
