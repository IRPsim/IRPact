package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.AtomicDouble;
import de.unileipzig.irpact.core.agent.SystemAgent;
import de.unileipzig.irpact.core.simulation.ExecMode;
import de.unileipzig.irpact.core.simulation.IRPactAgentFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Daniel Abitz
 */
public interface IRPactAgentManagerAgent extends SystemAgent {

    void setCreatedAgentsCounter(AtomicInteger createdAgentsCounter);

    AtomicInteger getCreatedAgentsCounter();

    void setLastBroadcastedProgress(AtomicDouble lastBroadcastedProgress);

    AtomicDouble getLastBroadcastedProgress();

    void setTotalNumberOfAgentsToCreate(int totalNumberOfAgentsToCreate);

    int getTotalNumberOfAgentsToCreate();

    void setParallelism(int parallelism);

    int getParallelism();

    void setRnd(Rnd rnd);

    Rnd getRnd();

    void setExecMode(ExecMode execMode);

    ExecMode getExecMode();

    void add(IRPactAgentFactory factory, String paramKey, Object param);

    void add(IRPactAgentFactory factory, Map<String, Object> params);

    void addAll(IRPactAgentFactory factory, Collection<? extends Map<String, Object>> params);

    Map<? extends IRPactAgentFactory, ? extends Collection<? extends Map<? extends String, ?>>> getAgentData();
}
