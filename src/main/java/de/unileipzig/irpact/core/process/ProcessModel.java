package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProcessModel {

    void onNewSimulationPeriod();

    ProcessPlan newPlan(Agent agent, Need need, Product product);
}
