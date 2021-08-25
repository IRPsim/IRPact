package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.commons.util.ProgressCalculator;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.AtomicDouble;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irpact.core.simulation.ExecMode;
import de.unileipzig.irpact.core.simulation.IRPactAgentAPI;
import de.unileipzig.irpact.core.simulation.IRPactAgentFactory;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bdiv3.BDIAgentFactory;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@RequiredServices(
        @RequiredService(type = SimulationService.class)
)
public class JadexIRPactAgentManagerAgentBDI extends AbstractJadexAgentBDI implements IRPactAgentManagerAgent {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexIRPactAgentManagerAgentBDI.class);

    private static final double MINIMAL_PROGRESS = IRPact.MINIMAL_PROGRESS;

    protected ProxyIRPactAgentManagerAgent proxyAgent;
    protected SimulationService simulationService;
    protected Rnd rnd;
    protected ExecMode execMode;
    protected AtomicInteger createdAgentsCounter;
    protected AtomicDouble lastBroadcastedProgress;
    protected int totalNumberOfAgentsToCreate;
    protected int parallelism;
    protected boolean shuffle;
    protected ExecutorService exec;
    protected Map<IRPactAgentFactory, List<Map<String, Object>>> agentData = new LinkedHashMap<>();
    protected List<IRPactAgentAPI> agents = new ArrayList<>();

    public JadexIRPactAgentManagerAgentBDI() {
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    protected IRPactAgentManagerAgent getThisAgent() {
        return getProxyAgent();
    }

    @Override
    protected ProxyIRPactAgentManagerAgent getProxyAgent() {
        return proxyAgent;
    }

    @Override
    protected JadexIRPactAgentManagerAgentBDI getRealAgent() {
        return this;
    }

    //=========================
    //livecycle
    //=========================

    @Override
    protected void onInit() {
        initData();
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] init", getName());
        searchSimulationService();
    }

    @Override
    protected void onStart() {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] start", getName());
        scheduleFirstAction();
    }

    @Override
    protected void onEnd() {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] end", getName());
        runOnEnd();
    }

    //=========================
    //IRPactAgentManagerAgent
    //=========================

    @Override
    public Rnd getRnd() {
        return rnd;
    }

    @Override
    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    @Override
    public int getParallelism() {
        return parallelism;
    }

    @Override
    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    @Override
    public ExecMode getExecMode() {
        return execMode;
    }

    @Override
    public void setExecMode(ExecMode execMode) {
        this.execMode = execMode;
    }

    @Override
    public AtomicInteger getCreatedAgentsCounter() {
        return createdAgentsCounter;
    }

    @Override
    public void setCreatedAgentsCounter(AtomicInteger createdAgentsCounter) {
        this.createdAgentsCounter = createdAgentsCounter;
    }

    @Override
    public AtomicDouble getLastBroadcastedProgress() {
        return lastBroadcastedProgress;
    }

    @Override
    public void setLastBroadcastedProgress(AtomicDouble lastBroadcastedProgress) {
        this.lastBroadcastedProgress = lastBroadcastedProgress;
    }

    @Override
    public int getTotalNumberOfAgentsToCreate() {
        return totalNumberOfAgentsToCreate;
    }

    @Override
    public void setTotalNumberOfAgentsToCreate(int totalNumberOfAgentsToCreate) {
        this.totalNumberOfAgentsToCreate = totalNumberOfAgentsToCreate;
    }

    protected List<Map<String, Object>> getAgentDataList(IRPactAgentFactory factory) {
        return agentData.computeIfAbsent(factory, _factory -> new ArrayList<>());
    }

    @Override
    public void add(IRPactAgentFactory factory, String paramKey, Object param) {
        Map<String, Object> map = new HashMap<>();
        map.put(paramKey, param);
        getAgentDataList(factory).add(map);
    }

    @Override
    public void add(IRPactAgentFactory factory, Map<String, Object> params) {
        getAgentDataList(factory).add(params);
    }

    @Override
    public void addAll(IRPactAgentFactory factory, Collection<? extends Map<String, Object>> params) {
        getAgentDataList(factory).addAll(params);
    }

    @Override
    public Map<IRPactAgentFactory, List<Map<String, Object>>> getAgentData() {
        return agentData;
    }

    //=========================
    //IRPactAgentManagerAgent
    //=========================

    @Override
    public int getChecksum() {
        return Objects.hash(getName());
    }

    protected void searchSimulationService() {
        JadexUtil.searchPlatformServices(reqFeature, SimulationService.class, result -> {
            if(simulationService == null) {
                log().trace(IRPSection.INITIALIZATION_PLATFORM, "[{}] SimulationService found", getName());
                simulationService = result;
                setupAgent();
            }
        });
    }

    protected void setupAgent() {
        //HIER KOMMEN AUFGABEN HIN
        //node setzen
        simulationService.reportAgentCreated(getThisAgent());
        simulationService.registerAgentForFastTermination(agent);

        try {
            initAgents();
        } catch (Throwable t) {
            log().error("[{}] setupAgent failed", getName());
            getEnvironment().getLifeCycleControl().handleFatalError(t);
        }
    }

    protected ProxyIRPactAgentManagerAgent getProxy() {
        Object obj = resultsFeature.getArguments().get(IRPact.PROXY);
        if(obj instanceof ProxyIRPactAgentManagerAgent) {
            return (ProxyIRPactAgentManagerAgent) obj;
        } else {
            throw new IllegalStateException("ProxyIRPactAgentManagerAgent not found");
        }
    }

    protected void initData() {
        proxyAgent = getProxy();
        name = proxyAgent.getName();
        environment = (JadexSimulationEnvironment) proxyAgent.getEnvironment();

        shuffle = true;
        totalNumberOfAgentsToCreate = proxyAgent.getTotalNumberOfAgentsToCreate();
        createdAgentsCounter = proxyAgent.getCreatedAgentsCounter();
        lastBroadcastedProgress = proxyAgent.getLastBroadcastedProgress();
        rnd = proxyAgent.getRnd();
        parallelism = proxyAgent.getParallelism();
        execMode = proxyAgent.getExecMode();
        agentData.clear();
        for(Map.Entry<? extends IRPactAgentFactory, ? extends Collection<? extends Map<? extends String, ?>>> entry: proxyAgent.getAgentData().entrySet()) {
            IRPactAgentFactory factory = entry.getKey();
            Collection<? extends Map<? extends String, ?>> coll = entry.getValue();
            for(Map<? extends String, ?> params: coll) {
                Map<String, Object> paramsCopy = new LinkedHashMap<>(params);
                add(factory, paramsCopy);
            }
        }
        startExec();

        proxyAgent.sync(getRealAgent());
    }

    protected void startExec() {
        log().trace(IRPSection.INITIALIZATION_PARAMETER, "[{}] setup exec: mode={}, parallelism={}", getName(), getExecMode(), getParallelism());
        if(parallelism > 1 && execMode != null) {
            switch(execMode) {
                case WORKSTEALING:
                    exec = Executors.newWorkStealingPool(parallelism);
                    break;
                case FIXED:
                    exec = Executors.newFixedThreadPool(parallelism, IRPactAgentAPIThreadFactory.getInstance());
                    break;
                case CACHED:
                    exec = Executors.newCachedThreadPool(IRPactAgentAPIThreadFactory.getInstance());
                    break;

                default:
                    log().warn(IRPSection.INITIALIZATION_PARAMETER, "[{}] unsupported mode: {}", getName(), getExecMode());
                    break;
            }
        }
    }

    protected void shutdownExec() {
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] terminate exec ({})", getName(), exec != null);
        if(exec != null) {
            exec.shutdown();
        }
    }

    protected void runOnEnd() {
        //log().info("[{}] end", getName());
        shutdownExec();
        endAgents();
        proxyAgent.unsync(this);
    }

    @Override
    protected void firstAction() throws Throwable {
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] firstAction", getName());
        startAgents();
        scheduleLoop();
    }

    protected void initAgents() throws Throwable {
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] init agents", getName());

        agents.clear();
        //init
        for(Map.Entry<IRPactAgentFactory, List<Map<String, Object>>> entry: getAgentData().entrySet()) {
            IRPactAgentFactory factory = entry.getKey();
            for(Map<String, Object> param: entry.getValue()) {
                IRPactAgentAPI agent = factory.get();
                agents.add(agent);

                agent.initIRPactAgent(param, simulationService);

                broadcastAgentCreationProgress(
                        createdAgentsCounter.incrementAndGet(),
                        totalNumberOfAgentsToCreate
                );
            }
        }

        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] {} agents initalized", getName(), agents.size());
    }


    private void broadcastAgentCreationProgress(long created, long total) {
        double lastProgress = lastBroadcastedProgress.get();
        double progress = (double) created / (double) total;
        if(progress - lastProgress >= MINIMAL_PROGRESS) {
            if(lastBroadcastedProgress.compareAndSet(lastProgress, progress)) {
                ProgressCalculator calc = environment.getProgressCalculator();
                calc.lock();
                try {
                    calc.setProgress(IRPact.PROGRESS_PHASE_AGENT_CREATION, progress);
                    double irpactProgress = calc.getProgress();

                    long maxMem = Runtime.getRuntime().maxMemory();
                    long totalMem = Runtime.getRuntime().totalMemory();
                    long freeMem = Runtime.getRuntime().freeMemory();
                    long usedMem = totalMem - freeMem;

                    LOGGER.info(
                            IRPSection.INITIALIZATION_PLATFORM,
                            "created agents: {}% ({}/{}), IRPact: {}% (mem: max={}, total={}, free={}, used={})",
                            StringUtil.DF2_POINT.format(progress * 100.0),
                            created, total,
                            StringUtil.DF2_POINT.format(irpactProgress * 100.0),
                            maxMem, totalMem, freeMem, usedMem
                    );
                } finally {
                    calc.unlock();
                }
            }
        }
    }

    protected void startAgents() throws Throwable {
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] start agents", getName());
        for(IRPactAgentAPI agent: agents) {
            agent.startIRPactAgent();
        }
    }

    protected void endAgents() {
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] end agents", getName());
        for(IRPactAgentAPI agent: agents) {
            try {
                agent.endIRPactAgent();
            } catch (Throwable e) {
                log().error("[{}] ending agent '{}' failed", getName(), agent.getName());
            }
        }
    }

    protected boolean hasExec() {
        return exec != null;
    }

    protected <T> List<T> getShuffledCopy(List<T> in) {
        List<T> out = new ArrayList<>(in);
        rnd.shuffle(out);
        return out;
    }
    protected <T> List<T> shuffleIfRequired(List<T> in) {
        if(shuffle) {
            return getShuffledCopy(in);
        } else {
            return in;
        }
    }

    @Override
    protected void onLoopAction() throws Throwable {
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] start next action ({})", getName(), now());
        if(hasExec()) {
            scheduleOnExec(agents);
        } else {
            scheduleSelf(agents);
        }
    }

    protected void scheduleOnExec(List<IRPactAgentAPI> agents) throws Throwable {
        List<PostAction<?>> postActions = Collections.synchronizedList(new ArrayList<>());

        //start
        List<IRPactAgentAPI> shuffledAgents = shuffleIfRequired(agents);
        List<LoopTask> agentTasks = shuffledAgents.stream()
                .map(agent -> new LoopTask(agent, postActions))
                .collect(Collectors.toList());

        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] (exec) start {} tasks", getName(), agentTasks.size());
        List<Future<Void>> execFutures = exec.invokeAll(agentTasks);
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] (exec) {} tasks finished", getName(), execFutures.size());
        for(LoopTask task: agentTasks) {
            if(task.hasThrowable()) {
                log().error("[{}] task '{}' failed", getName(), task.getAgent().getName());
                throw task.getThrowable();
            }
        }

        //postaction
        List<PostAction<?>> shuffledPostActions = shuffleIfRequired(postActions);
        List<PostActionTask> postTasks = shuffledPostActions.stream()
                .map(PostActionTask::new)
                .collect(Collectors.toList());

        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] (exec) start {} postactions", getName(), postTasks.size());
        List<Future<Void>> postFutures = exec.invokeAll(postTasks);
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] (exec) {} postactions finished", getName(), postFutures.size());
        for(PostActionTask task: postTasks) {
            if(task.hasThrowable()) {
                log().error("[{}] (exec) postaction '{}' failed", getName(), task.getPostAction().getInputName());
                throw task.getThrowable();
            }
        }
    }

    protected void scheduleSelf(List<IRPactAgentAPI> agents) throws Throwable {
        List<PostAction<?>> postActions = new ArrayList<>();

        //start
        List<IRPactAgentAPI> shuffledAgents = shuffleIfRequired(agents);
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] (self) start {} tasks", getName(), shuffledAgents.size());
        for(IRPactAgentAPI agent: shuffledAgents) {
            try {
                agent.nextIRPactAgentLoopAction(postActions);
            } catch (Throwable t) {
                log().error("[{}] task '{}' failed", getName(), agent.getName());
                throw t;
            }
        }
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] (self) {} tasks finished", getName(), shuffledAgents.size());

        //postaction
        List<PostAction<?>> shuffledPostActions = shuffleIfRequired(postActions);
        log().trace(IRPSection.SIMULATION_PROCESS, "[{}] (self) start {} postactions", getName(), shuffledPostActions.size());
        for(PostAction<?> action: shuffledPostActions) {
            try {
                action.execute();
            } catch (Throwable t) {
                log().error("[{}] (self) postaction '{}' failed", getName(), action.getInputName());
                throw t;
            }
        }
    }

    //=========================
    //IRPactAgentAPILoopTask
    //=========================

    /**
     * @author Daniel Abitz
     */
    protected static class LoopTask implements Callable<Void> {

        protected List<PostAction<?>> postActions;
        protected IRPactAgentAPI agent;
        protected Throwable t;

        public LoopTask(IRPactAgentAPI agent, List<PostAction<?>> postActions) {
            this.agent = agent;
            this.postActions = postActions;
        }

        @Override
        public Void call() throws Exception {
            try {
                agent.nextIRPactAgentLoopAction(postActions);
            } catch (Throwable t) {
                this.t = t;
            }
            return null;
        }

        public void dispose() {
            agent = null;
            t = null;
        }

        public IRPactAgentAPI getAgent() {
            return agent;
        }

        public boolean hasThrowable() {
            return t != null;
        }

        public Throwable getThrowable() {
            return t;
        }
    }

    /**
     * @author Daniel Abitz
     */
    protected static class PostActionTask implements Callable<Void> {

        protected PostAction<?> postAction;
        protected Throwable t;

        public PostActionTask(PostAction<?> postAction) {
            this.postAction = postAction;
        }

        @Override
        public Void call() throws Exception {
            try {
                postAction.execute();
            } catch (Throwable t) {
                this.t = t;
            }
            return null;
        }

        public void dispose() {
            postAction = null;
            t = null;
        }

        public PostAction<?> getPostAction() {
            return postAction;
        }

        public boolean hasThrowable() {
            return t != null;
        }

        public Throwable getThrowable() {
            return t;
        }
    }
}
