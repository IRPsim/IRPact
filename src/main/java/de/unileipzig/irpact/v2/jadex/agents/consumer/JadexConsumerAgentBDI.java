package de.unileipzig.irpact.v2.jadex.agents.consumer;

import de.unileipzig.irpact.v2.commons.awareness.Awareness;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentInitializationData;
import de.unileipzig.irpact.v2.core.product.Product;
import de.unileipzig.irpact.v2.core.spatial.SpatialInformation;
import de.unileipzig.irpact.v2.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.v2.jadex.agents.simulation.SimulationService;
import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.v2.jadex.util.JadexUtil2;
import jadex.bdiv3.BDIAgentFactory;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@RequiredServices(
        @RequiredService(type = SimulationService.class)
)
public class JadexConsumerAgentBDI extends AbstractJadexAgentBDI implements ConsumerAgent {

    protected static final Logger LOGGER = LoggerFactory.getLogger(JadexConsumerAgentBDI.class);

    protected SimulationService simulationService;
    protected Set<ConsumerAgentAttribute> attributes = new HashSet<>();
    protected double informationAuthority;
    protected Awareness<Product> productAwareness;

    public JadexConsumerAgentBDI() {
    }

    @Override
    public JadexConsumerAgentInitializationData getData() {
        return (JadexConsumerAgentInitializationData) data;
    }

    @Override
    protected Logger log() {
        return LOGGER;
    }

    @Override
    protected void initData() {
        super.initData();
        init(getData());
    }

    protected void searchSimulationService() {
        JadexUtil2.searchPlatformServices(reqFeature, SimulationService.class, result -> {
            if(simulationService == null) {
                log().trace("[{}] SimulationService found", getName());
                simulationService = result;
                setupAgent();
            }
        });
    }

    protected void setupAgent() {
        //HIER KOMMEN AUFGABEN HIN
        simulationService.getEnvironment().getSimulationControl().reportAgentCreation();
    }

    //=========================
    //livecycle
    //=========================

    @Override
    protected void onInit() {
        initData();
        searchSimulationService();
        log().trace("[{}] init", getName());
    }

    @Override
    protected void onStart() {
        log().trace("[{}] start", getName());
    }

    @Override
    protected void onEnd() {
        log().trace("[{}] end", getName());
    }

    //=========================
    //ConsumerAgent
    //=========================

    @Override
    public void init(ConsumerAgentInitializationData data) {
        JadexConsumerAgentInitializationData jdata = (JadexConsumerAgentInitializationData) data;
        this.data = jdata;
        attributes.addAll(jdata.getAttributes());
        productAwareness = jdata.getProductAwareness();
    }

    @Override
    public ConsumerAgentGroup getGroup() {
        return getData().getGroup();
    }

    @Override
    public Set<ConsumerAgentAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public Awareness<Product> getProductAwareness() {
        return productAwareness;
    }

    @Override
    public double getInformationAuthority() {
        return getData().getInformationAuthority();
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return getData().getEnvironment();
    }

    @Override
    public boolean hasName(String input) {
        return Objects.equals(getName(), input);
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return getData().getSpatialInformation();
    }
}
