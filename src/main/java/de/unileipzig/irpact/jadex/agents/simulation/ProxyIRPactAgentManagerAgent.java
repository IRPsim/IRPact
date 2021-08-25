package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.AtomicDouble;
import de.unileipzig.irpact.core.agent.ProxyAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.simulation.ExecMode;
import de.unileipzig.irpact.core.simulation.IRPactAgentFactory;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class ProxyIRPactAgentManagerAgent implements IRPactAgentManagerAgent, ProxyAgent<IRPactAgentManagerAgent> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProxyIRPactAgentManagerAgent.class);

    protected IRPactAgentManagerAgent realAgent;

    protected String name;
    protected SimulationEnvironment environment;
    protected Rnd rnd;
    protected ExecMode execMode;
    protected int parallelism;
    protected AtomicInteger createdAgentsCounter;
    protected AtomicDouble lastBroadcastedProgress;
    protected int totalNumberOfAgentsToCreate;
    protected Map<IRPactAgentFactory, List<Map<String, Object>>> agentData = new LinkedHashMap<>();

    public ProxyIRPactAgentManagerAgent() {
    }

    @Override
    public boolean isSynced() {
        return realAgent != null;
    }

    public boolean isNotSynced() {
        return realAgent == null;
    }

    public void sync(IRPactAgentManagerAgent realAgent) {
        if(isSynced()) {
            throw new IllegalStateException("already synced");
        }
        this.realAgent = realAgent;
    }

    public void unsync(IRPactAgentManagerAgent realAgent) {
        if(isNotSynced()) {
            throw new IllegalStateException("not synced");
        }
        if(this.realAgent != realAgent) {
            throw new IllegalArgumentException("synced to another agent");
        }
        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] unsync", realAgent.getName());
        this.realAgent = null;
        reset(realAgent);
    }

    protected void reset(IRPactAgentManagerAgent realAgent) {
        totalNumberOfAgentsToCreate = realAgent.getTotalNumberOfAgentsToCreate();
        createdAgentsCounter = realAgent.getCreatedAgentsCounter();
        lastBroadcastedProgress = realAgent.getLastBroadcastedProgress();
        parallelism = realAgent.getParallelism();
        rnd = realAgent.getRnd();
        execMode = realAgent.getExecMode();
        agentData.clear();
        for(Map.Entry<? extends IRPactAgentFactory, ? extends Collection<? extends Map<? extends String, ?>>> entry: realAgent.getAgentData().entrySet()) {
            IRPactAgentFactory factory = entry.getKey();
            Collection<? extends Map<? extends String, ?>> coll = entry.getValue();
            for(Map<? extends String, ?> params: coll) {
                Map<String, Object> paramsCopy = new LinkedHashMap<>(params);
                add(factory, paramsCopy);
            }
        }
    }

    @Override
    public IRPactAgentManagerAgent getRealAgent() {
        return realAgent;
    }

    protected void checkSynced() {
        if(isNotSynced()) {
            throw new IllegalStateException("not synced");
        }
    }

    protected void checkNotSynced() {
        if(isSynced()) {
            throw new IllegalStateException("synced");
        }
    }

    @Override
    public String getName() {
        if(isSynced()) {
            return getRealAgent().getName();
        } else {
            return name;
        }
    }

    public void setName(String name) {
        checkNotSynced();
        this.name = name;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        if(isSynced()) {
            return getRealAgent().getEnvironment();
        } else {
            return environment;
        }
    }

    public void setEnvironment(SimulationEnvironment environment) {
        checkNotSynced();
        this.environment = environment;
    }

    @Override
    public int getChecksum() {
        if(isSynced()) {
            return getRealAgent().getChecksum();
        } else {
            return Objects.hash(getName());
        }
    }

    @Override
    public void setParallelism(int parallelism) {
        if(isSynced()) {
            getRealAgent().setParallelism(parallelism);
        } else {
            this.parallelism = parallelism;
        }
    }

    @Override
    public int getParallelism() {
        if(isSynced()) {
            return getRealAgent().getParallelism();
        } else {
            return parallelism;
        }
    }

    @Override
    public void setExecMode(ExecMode execMode) {
        if(isSynced()) {
            getRealAgent().setExecMode(execMode);
        } else {
            this.execMode = execMode;
        }
    }

    @Override
    public ExecMode getExecMode() {
        if(isSynced()) {
            return getRealAgent().getExecMode();
        } else {
            return execMode;
        }
    }

    @Override
    public void setRnd(Rnd rnd) {
        if(isSynced()) {
            getRealAgent().setRnd(rnd);
        } else {
            this.rnd = rnd;
        }
    }

    @Override
    public Rnd getRnd() {
        if(isSynced()) {
            return getRealAgent().getRnd();
        } else {
            return rnd;
        }
    }

    @Override
    public void setTotalNumberOfAgentsToCreate(int totalNumberOfAgentsToCreate) {
        if(isSynced()) {
            getRealAgent().setTotalNumberOfAgentsToCreate(totalNumberOfAgentsToCreate);
        } else {
            this.totalNumberOfAgentsToCreate = totalNumberOfAgentsToCreate;
        }
    }

    @Override
    public int getTotalNumberOfAgentsToCreate() {
        if(isSynced()) {
            return getRealAgent().getTotalNumberOfAgentsToCreate();
        } else {
            return totalNumberOfAgentsToCreate;
        }
    }

    @Override
    public void setCreatedAgentsCounter(AtomicInteger createdAgentsCounter) {
        if(isSynced()) {
            getRealAgent().setCreatedAgentsCounter(createdAgentsCounter);
        } else {
            this.createdAgentsCounter = createdAgentsCounter;
        }
    }

    @Override
    public AtomicInteger getCreatedAgentsCounter() {
        if(isSynced()) {
            return getRealAgent().getCreatedAgentsCounter();
        } else {
            return createdAgentsCounter;
        }
    }

    @Override
    public void setLastBroadcastedProgress(AtomicDouble lastBroadcastedProgress) {
        if(isSynced()) {
            getRealAgent().setLastBroadcastedProgress(lastBroadcastedProgress);
        } else {
            this.lastBroadcastedProgress = lastBroadcastedProgress;
        }
    }

    @Override
    public AtomicDouble getLastBroadcastedProgress() {
        if(isSynced()) {
            return getRealAgent().getLastBroadcastedProgress();
        } else {
            return lastBroadcastedProgress;
        }
    }

    protected List<Map<String, Object>> getAgentDataList(IRPactAgentFactory factory) {
        return agentData.computeIfAbsent(factory, _factory -> new ArrayList<>());
    }

    @Override
    public void add(IRPactAgentFactory factory, String paramKey, Object param) {
        if(isSynced()) {
            getRealAgent().add(factory, paramKey, param);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(paramKey, param);
            getAgentDataList(factory).add(map);
        }
    }

    @Override
    public void add(IRPactAgentFactory factory, Map<String, Object> params) {
        if(isSynced()) {
            getRealAgent().add(factory, params);
        } else {
            getAgentDataList(factory).add(params);
        }
    }

    @Override
    public void addAll(IRPactAgentFactory factory, Collection<? extends Map<String, Object>> params) {
        if(isSynced()) {
            getRealAgent().addAll(factory, params);
        } else {
            getAgentDataList(factory).addAll(params);
        }
    }

    @Override
    public Map<? extends IRPactAgentFactory, ? extends Collection<? extends Map<? extends String, ?>>> getAgentData() {
        if(isSynced()) {
            return getRealAgent().getAgentData();
        } else {
            return agentData;
        }
    }
}
