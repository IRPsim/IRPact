package de.unileipzig.irpact.v2.jadex.agents.simulation;

import de.unileipzig.irpact.v2.jadex.agents.AbstractJadexAgentBDI;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.Service;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Abitz
 */
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
        initData();
        log().trace("[{}] init", getName());
    }

    @Override
    protected void onEnd() {
        initData();
        log().trace("[{}] init", getName());
    }
}
