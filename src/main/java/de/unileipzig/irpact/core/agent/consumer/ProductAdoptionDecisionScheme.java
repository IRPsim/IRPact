package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProductAdoptionDecisionScheme extends Scheme {

    Product decide(
            SimulationEnvironment environment,
            ConsumerAgent agent,
            Collection<? extends Product> potentialProducts);
}
