package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.LoggingPart;
import de.unileipzig.irpact.core.log.LoggingType;
import de.unileipzig.irpact.jadex.JadexConstants;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.jadex.simulation.JadexLiveCycleControl;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.jadex.time.JadexTimestamp;
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

    protected JadexLiveCycleControl control;

    public JadexSimulationAgentBDI() {
    }

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
        control.registerSimulationAgentAccess(agent);
        log().trace(LoggingType.CORE, LoggingPart.AGENT, "[{}] init", getName());
    }

    @Override
    protected void onStart() {
        log().trace(LoggingType.CORE, LoggingPart.AGENT, "[{}] init", getName());
        waitUntilEnd();
        reportAgentCreated(this);
    }

    @Override
    protected void onEnd() {
        log().trace(LoggingType.CORE, LoggingPart.AGENT, "[{}] init", getName());
    }

    //=========================
    //SimulationAgent
    //=========================

    protected JadexSimulationAgentInitializationData getData() {
        return (JadexSimulationAgentInitializationData) resultsFeature.getArguments().get(JadexConstants.DATA);
    }

    protected void initData() {
        JadexSimulationAgentInitializationData data = getData();
        name = data.getName();
        environment = data.getEnvironment();
        control = data.getControl();
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
        control.reportAgentCreated(agent);
    }

    @Override
    public void reportFatalException(Exception e) {
        control.terminateWithError(e);
    }
}
