package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.misc.Initialization;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProcessModel extends Nameable, Initialization {

    ProcessPlan newPlan(Agent agent, Need need, Product product);
}
