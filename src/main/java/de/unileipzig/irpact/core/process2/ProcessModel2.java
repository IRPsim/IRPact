package de.unileipzig.irpact.core.process2;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface ProcessModel2 extends SimulationEntity, InitalizablePart, ProcessModel {

    //legacy
    @Override
    default ProcessPlan newPlan(Agent agent, Need need, Product product) {
        return newPlan2(agent, need, product);
    }

    //legacy
    @Override
    default void handleNewProduct(Product newProduct) {
        handleNewProduct2(newProduct);
    }

    void handleNewProduct2(Product product);

    ProcessPlan2 newPlan2(Agent agent, Need need, Product product);
}
