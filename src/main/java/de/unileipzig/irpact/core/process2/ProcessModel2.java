package de.unileipzig.irpact.core.process2;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface ProcessModel2 extends SimulationEntity, InitalizablePart {

    void handleNewProduct(Product product);

    ProcessPlan2 newPlan(Agent agent, Need need, Product product);
}
