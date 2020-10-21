package de.unileipzig.irpact.v2.jadex.agents.consumer;

import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentInitializationData;
import de.unileipzig.irpact.v2.def.ToDo;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class JadexConsumerAgentInitializationData implements ConsumerAgentInitializationData {

    @ToDo("abstract stuff einbauen")
    @Override
    public String getName() {
        return null;
    }
}
