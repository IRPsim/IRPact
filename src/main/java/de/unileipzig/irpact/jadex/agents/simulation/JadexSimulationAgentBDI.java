package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

import java.util.ArrayList;
import java.util.List;
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

    private static final double MINIMAL_PROGRESS = IRPact.MINIMAL_PROGRESS;

    protected ProxySimulationAgent proxyAgent;
    protected double lastBroadcastedProgress = 0.0;
    protected List<IExternalAccess> agents = new ArrayList<>();


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
        environment.getLifeCycleControl().registerSimulationAgentAccess(getThisAgent(), agent);
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
        lastBroadcastedProgress = 0;

        proxyAgent.sync(getRealAgent());
    }

    protected void waitUntilEnd() {
        JadexTimeModel timeModel = environment.getTimeModel();
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] wait until end: {}", getName(), timeModel.endTime());
        timeModel.waitUntilEnd(execFeature, agent, this::doOnEnd);
    }

    protected IFuture<Void> doOnEnd(IInternalAccess ia) {
        JadexTimeModel timeModel = environment.getTimeModel();
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] onEnd: {} ({})", getName(), timeModel.now(), timeModel.endTimeReached());
        environment.getLifeCycleControl().waitForYearChangeIfRequired(this);
        environment.getLifeCycleControl().prepareTermination();
        killAgents();
        environment.getLifeCycleControl().terminate();
        return IFuture.DONE;
    }

    @Override
    protected void firstAction() {
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] firstAction", getName());
        scheduleLoop();
    }

    @Override
    protected void onLoopAction() {
        double newProgress = getTimeModel().getTimeProgress();
        broadcastProgress(newProgress);
    }

    protected void broadcastProgress(double newProgress) {
        if(newProgress - lastBroadcastedProgress > MINIMAL_PROGRESS) {
            LOGGER.info(
                    IRPSection.SIMULATION_PROCESS,
                    "time progress: {}%",
                    StringUtil.DF2_POINT.format(newProgress * 100.0)
            );
            lastBroadcastedProgress = newProgress;
        }
    }

    @Override
    public void reportAgentCreated(de.unileipzig.irpact.core.agent.Agent agent) {
        environment.getLifeCycleControl().reportAgentCreated(agent);
    }

    @Override
    public void registerAgentForFastTermination(IExternalAccess access) {
        addAgent(access);
    }

    protected synchronized void addAgent(IExternalAccess access) {
        agents.add(access);
    }

    protected void killAgents() {
        int killed = 0;
        double lastBroadcastedProgress = 0;
        for(IExternalAccess access: agents) {
            access.killComponent().get();
            lastBroadcastedProgress = broadcastAgentKillProgress(
                    ++killed,
                    agents.size(),
                    lastBroadcastedProgress,
                    MINIMAL_PROGRESS
            );
        }
        agents.clear();
    }

    private double broadcastAgentKillProgress(
            long killed,
            long total,
            double lastBroadcastedProgress,
            double minDiff) {
        double progress = (double) killed / (double) total;
        if((progress - lastBroadcastedProgress >= minDiff || killed == total)) {
            LOGGER.info(
                    IRPSection.SIMULATION_PROCESS,
                    "killend agents: {}% ({}/{})",
                    StringUtil.DF2_POINT.format(progress * 100.0),
                    killed, total
            );
            return progress;
        } else {
            return lastBroadcastedProgress;
        }
    }
}
