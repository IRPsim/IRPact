package de.unileipzig.irpact.core.process.mra.component.base;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.process.mra.AgentData;
import de.unileipzig.irpact.core.process.mra.component.generic.Component;

/**
 * @author Daniel Abitz
 */
public interface ValueComponent extends Component {

    double calculate(Agent agent, AgentData data);
}
