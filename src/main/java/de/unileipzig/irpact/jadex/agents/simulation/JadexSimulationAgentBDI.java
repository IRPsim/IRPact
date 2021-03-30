package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
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

import java.util.Objects;

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

    protected ProxySimulationAgent proxyAgent;

    public JadexSimulationAgentBDI() {
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    protected SimulationAgent getThisAgent() {
        return getProxyAgent();
    }

    @Override
    protected ProxySimulationAgent getProxyAgent() {
        return proxyAgent;
    }

    @Override
    protected JadexSimulationAgentBDI getRealAgent() {
        return this;
    }

    //=========================
    //livecycle
    //=========================

    @Override
    protected void onInit() {
        initData();
        environment.getLiveCycleControl().registerSimulationAgentAccess(getThisAgent(), agent);
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] init", getName());
    }

    @Override
    protected void onStart() {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] start", getName());
        waitUntilEnd();
        reportAgentCreated(getThisAgent());
        scheduleFirstAction();
    }

    @Override
    protected void onEnd() {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] end", getName());
    }

    //=========================
    //SimulationAgent
    //=========================

    @Override
    public int getChecksum() {
        return Objects.hash(getName());
    }

    protected ProxySimulationAgent getProxy() {
        Object obj = resultsFeature.getArguments().get(IRPact.PROXY);
        if(obj instanceof ProxySimulationAgent) {
            return (ProxySimulationAgent) obj;
        } else {
            throw new IllegalStateException("ProxySimulationAgent not found");
        }
    }

    protected void initData() {
        proxyAgent = getProxy();
        name = proxyAgent.getName();
        environment = (JadexSimulationEnvironment) proxyAgent.getEnvironment();

        proxyAgent.sync(getRealAgent());
    }

    protected void waitUntilEnd() {
        JadexTimeModel timeModel = environment.getTimeModel();
        JadexTimestamp end = timeModel.endTime();
        log().trace("waitUntilEnd: {} -> {} ({})", timeModel.now(), end, timeModel.endTimeReached());
        timeModel.waitUntil(execFeature, end, agent, this::onEnd);
    }

    protected IFuture<Void> onEnd(IInternalAccess ia) {
        JadexTimeModel timeModel = environment.getTimeModel();
        log().trace("onEnd: {} ({})", timeModel.now(), timeModel.endTimeReached());
        return timeModel.scheduleImmediately(execFeature, agent, this::afterEnd);
    }

    protected IFuture<Void> afterEnd(IInternalAccess ia) {
        JadexTimeModel timeModel = environment.getTimeModel();
        log().trace("afterEnd: {} ({})", timeModel.now(), timeModel.endTimeReached());
        environment.getLiveCycleControl().terminate();
        return IFuture.DONE;
    }

    @Override
    protected void firstAction() {
        log().trace("[{}] firstAction", getName());
    }

    @Override
    protected void onLoopAction() {
    }

    @Override
    public void reportAgentCreated(de.unileipzig.irpact.core.agent.Agent agent) {
        environment.getLiveCycleControl().reportAgentCreated(agent);
    }
}
