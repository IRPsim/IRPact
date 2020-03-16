package de.unileipzig.irpact.jadex.agent.policy;

import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public interface PolicyAgentService {

    PolicyAgent getPolicyAgentSyn();

    IFuture<? extends PolicyAgent> getPolicyAgentAsyn();
}
