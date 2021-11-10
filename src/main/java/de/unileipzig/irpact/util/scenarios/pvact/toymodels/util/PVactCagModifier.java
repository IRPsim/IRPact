package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;

/**
 * @author Daniel Abitz
 */
public interface PVactCagModifier {

    PVactCagModifier DO_NOTHING = cag -> {};

    void modify(InPVactConsumerAgentGroup cag);
}
