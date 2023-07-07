package de.unileipzig.irpact.core.process2.modular;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.ProcessModel2;
import de.unileipzig.irpact.core.process2.ProcessPlanResult2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.product.Product;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface ModularProcessModel2 extends ProcessModel2 {

    @Override
    ModularProcessPlan2 newPlan2(Agent agent, Need need, Product product);

    ProcessPlanResult2 execute(ConsumerAgentData2 data, List<PostAction2> actions) throws Throwable;

    void remove(ModularProcessPlan2 plan);
}
