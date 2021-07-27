package de.unileipzig.irpact.core.process.modular;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ModularProcessModel extends ProcessModel {

    @Override
    ModulePlan newPlan(Agent agent, Need need, Product product);
}
