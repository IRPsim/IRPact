package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.commons.util.ProgressCalculator;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
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

    protected boolean callGc = true;
    protected ProxySimulationAgent proxyAgent;
    protected double lastBroadcastedProgress = 0.0;
    protected List<IExternalAccess> agents = new ArrayList<>();

    protected int debugTask = 0;

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
        debugTask = ((BasicJadexSimulationEnvironment) environment).debugTask;

        proxyAgent.sync(getRealAgent());
    }

    protected void waitUntilEnd() {
        JadexTimeModel timeModel = environment.getTimeModel();
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] wait until end: {}", getName(), timeModel.endTime());
        timeModel.waitUntilEnd(execFeature, agent, this::doOnEnd);
    }

    protected IFuture<Void> doOnEnd(IInternalAccess ia) {
        try {
            broadcastProgress(1.0);
            JadexTimeModel timeModel = environment.getTimeModel();
            log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] onEnd: {} ({})", getName(), timeModel.now(), timeModel.endTimeReached());
            environment.getLifeCycleControl().waitForYearChangeIfRequired(this);
            environment.getLifeCycleControl().prepareTermination();
            killAgents();
            environment.getLifeCycleControl().terminate();
        } catch (Throwable t) {
            environment.getLifeCycleControl().handleFatalError(t);
        }
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
        if(callGc) {
            System.gc();
        }
        if(debugTask != 0) {
            runDebugTask();
        }
    }

    protected int xxx = 0;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    protected void runDebugTask() {
        if(xxx++ == 10) {
            if(debugTask == 1) {
                //mem error
                List<int[]> arrList = new ArrayList<>();
                for(int i = 1; i < Integer.MAX_VALUE; i++) {
                    int[] arr = new int[i];
                    arrList.add(arr);
                }
            }
            if(debugTask == 2) {
                createStackoverflow();
            }
        }
    }
    @SuppressWarnings("InfiniteRecursion")
    protected void createStackoverflow() {
        createStackoverflow();
    }

    protected void broadcastProgress(double newProgress) {
        if(newProgress - lastBroadcastedProgress > MINIMAL_PROGRESS || newProgress == 1.0) {
            ProgressCalculator calc = environment.getProgressCalculator();
            calc.setProgress(IRPact.PROGRESS_PHASE_SIMULATION, newProgress);

            long maxMem = Runtime.getRuntime().maxMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory();
            long usedMem = totalMem - freeMem;

            LOGGER.info(
                    IRPSection.SIMULATION_PROCESS,
                    "simulation progress: {}%, IRPact: {}% (mem: max={}, total={}, free={}, used={}) (simulation time: {})",
                    StringUtil.DF2_POINT.format(newProgress * 100.0),
                    StringUtil.DF2_POINT.format(calc.getProgress() * 100.0),
                    maxMem, totalMem, freeMem, usedMem,
                    now()
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
                    lastBroadcastedProgress
            );
        }
        agents.clear();
    }

    private double broadcastAgentKillProgress(
            long killed,
            long total,
            double lastBroadcastedProgress) {
        double progress = (double) killed / (double) total;
        if((progress - lastBroadcastedProgress >= MINIMAL_PROGRESS || killed == total)) {
            ProgressCalculator calc = environment.getProgressCalculator();
            calc.setProgress(IRPact.PROGRESS_PHASE_AGENT_KILL, progress);
            double irpactProgress = calc.getProgress();

            long maxMem = Runtime.getRuntime().maxMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory();
            long usedMem = totalMem - freeMem;

            LOGGER.info(
                    IRPSection.SIMULATION_PROCESS,
                    "killed agents: {}% ({}/{}), IRPact: {}% (mem: max={}, total={}, free={}, used={})",
                    StringUtil.DF2_POINT.format(progress * 100.0),
                    killed, total,
                    StringUtil.DF2_POINT.format(irpactProgress * 100.0),
                    maxMem, totalMem, freeMem, usedMem
            );
            return progress;
        } else {
            return lastBroadcastedProgress;
        }
    }
}
