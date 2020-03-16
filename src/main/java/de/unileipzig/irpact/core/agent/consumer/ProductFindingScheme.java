package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProductFindingScheme {

    Collection<? extends Product> searchPotentialProducts(
            SimulationEnvironment environment,
            ConsumerAgent agent,
            Need need);
}
