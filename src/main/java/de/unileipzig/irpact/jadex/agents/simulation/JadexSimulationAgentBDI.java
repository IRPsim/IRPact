package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.jadex.time.JadexTimestamp;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@Service
@ProvidedServices({
        @ProvidedService(type = SimulationService.class, scope = ServiceScope.NETWORK)
})
public class JadexSimulationAgentBDI extends AbstractJadexAgentBDI implements SimulationService, SimulationAgent {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexSimulationAgentBDI.class);

    public JadexSimulationAgentBDI() {
    }
//TODO diesen agenten so konfigurieren, dass er x,y,z macht
//            + halbzeit + endzeit noch einrichten
    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //livecycle
    //=========================

    @Override
    protected void onInit() {
        initData();
        environment.getLiveCycleControl().registerSimulationAgentAccess(this, agent);
        log().trace(IRPSection.INITIALIZATION_AGENT, "[{}] init", getName());
    }

    @Override
    protected void onStart() {
        log().trace(IRPSection.INITIALIZATION_AGENT, "[{}] init", getName());
        waitUntilEnd();
        reportAgentCreated(this);
    }

    @Override
    protected void onEnd() {
        log().trace(IRPSection.INITIALIZATION_AGENT, "[{}] init", getName());
    }

    //=========================
    //SimulationAgent
    //=========================

    protected SimulationAgentInitializationData getData() {
        return (SimulationAgentInitializationData) resultsFeature.getArguments().get(IRPact.DATA);
    }

    protected void initData() {
        SimulationAgentInitializationData data = getData();
        name = data.getName();
        environment = data.getEnvironment();
    }

    protected void waitUntilEnd() {
        JadexTimeModel timeModel = environment.getTimeModel();
        JadexTimestamp end = timeModel.endTime();
        timeModel.waitUntil(execFeature, end, this::onEnd);
    }

    protected IFuture<Void> onEnd(IInternalAccess ia) {
        JadexTimeModel timeModel = environment.getTimeModel();
        return timeModel.wait(execFeature, 1L, this::afterEnd);
    }

    protected IFuture<Void> afterEnd(IInternalAccess ia) {
        return IFuture.DONE;
    }

    @Override
    public void reportAgentCreated(de.unileipzig.irpact.core.agent.Agent agent) {
        environment.getLiveCycleControl().reportAgentCreated(agent);
    }

    @Override
    public void reportFatalException(Exception e) {
        environment.getLiveCycleControl().terminateWithError(e);
    }
}
