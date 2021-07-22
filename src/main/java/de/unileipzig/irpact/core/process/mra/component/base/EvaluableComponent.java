package de.unileipzig.irpact.core.process.mra.component.base;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.mra.AgentData;
import de.unileipzig.irpact.core.process.mra.component.generic.Component;

/**
 * @author Daniel Abitz
 */
public interface EvaluableComponent extends Component {

    ProcessPlanResult evaluate(Agent agent, AgentData data);
}
