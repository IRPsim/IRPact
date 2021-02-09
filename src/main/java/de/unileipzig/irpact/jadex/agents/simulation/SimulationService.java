package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.core.agent.Agent;
import jadex.bridge.service.annotation.Reference;

/**
 *
 *
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface SimulationService {

    void reportAgentCreated(Agent agent);

    void reportFatalException(Exception e);
}
