package de.unileipzig.irpact.core.process.modularra.component.base;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.process.modularra.AgentData;
import de.unileipzig.irpact.core.process.modularra.component.generic.Component;

/**
 * @author Daniel Abitz
 */
public interface ValueComponent extends Component {

    double evaluate(Agent agent, AgentData data);
}
