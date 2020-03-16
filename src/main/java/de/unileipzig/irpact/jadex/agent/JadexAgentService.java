package de.unileipzig.irpact.jadex.agent;

import de.unileipzig.irpact.core.agent.Agent;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public interface JadexAgentService {

    Agent getAgentSyn();

    IFuture<? extends Agent> getAgentAsyn();
}
