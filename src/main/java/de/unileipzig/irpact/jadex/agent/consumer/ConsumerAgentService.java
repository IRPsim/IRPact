package de.unileipzig.irpact.jadex.agent.consumer;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentService {

    ConsumerAgent getConsumerAgentSyn();

    IFuture<? extends ConsumerAgent> getConsumerAgentAsyn();
}
