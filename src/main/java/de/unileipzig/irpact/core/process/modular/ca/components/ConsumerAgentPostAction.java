package de.unileipzig.irpact.core.process.modular.ca.components;

import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.PostAction;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentPostAction extends PostAction<ConsumerAgentData> {

    AdoptionResult evaluate() throws Throwable;
}
