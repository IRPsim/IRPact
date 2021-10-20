package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationService;

import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public interface IRPactAgentAPI extends Nameable {

    void initIRPactAgent(
            Map<String, Object> param,
            SimulationService simulationService) throws Throwable;

    void startIRPactAgent() throws Throwable;

    Map<String, Object> endIRPactAgent() throws Throwable;

    void nextIRPactAgentLoopAction(List<PostAction> postActions) throws Throwable;
}
