package de.unileipzig.irpact.core.process2.modular;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process2.ProcessModel2;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ModularProcessModel2 extends ProcessModel2 {

    @Override
    ModularProcessPlan2 newPlan(Agent agent, Need need, Product product);
}
