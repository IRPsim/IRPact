package de.unileipzig.irpact.core.process.modular;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.modular.components.core.Module;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ModularProcessModel extends ProcessModel {

    Module searchModule(String name);

    @Override
    ModularProcessPlan newPlan(Agent agent, Need need, Product product);
}
