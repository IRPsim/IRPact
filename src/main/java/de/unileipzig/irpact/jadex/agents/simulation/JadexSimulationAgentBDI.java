package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.Service;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@Service
@ProvidedServices({
        @ProvidedService(type = SimulationService.class, scope = ServiceScope.NETWORK)
})
public class JadexSimulationAgentBDI extends AbstractJadexAgentBDI implements SimulationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JadexSimulationAgentBDI.class);

    public JadexSimulationAgentBDI() {
    }

    @Override
    protected JadexSimulationAgentInitializationData getData() {
        return (JadexSimulationAgentInitializationData) data;
    }

    @Override
    protected Logger log() {
        return LOGGER;
    }

    @Override
    protected void onInit() {
        initData();
        log().trace("[{}] init", getName());
    }

    @Override
    protected void onStart() {
        log().trace("[{}] init", getName());
        getEnvironment().getSimulationControl().reportAgentCreation();
    }

    @Override
    protected void onEnd() {
        log().trace("[{}] init", getName());
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return getData().getEnvironment();
    }
}
