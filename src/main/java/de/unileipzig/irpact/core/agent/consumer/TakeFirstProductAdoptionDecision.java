package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class TakeFirstProductAdoptionDecision implements ProductAdoptionDecisionScheme {

    public static final String NAME = TakeFirstProductAdoptionDecision.class.getSimpleName();
    public static final TakeFirstProductAdoptionDecision INSTANCE = new TakeFirstProductAdoptionDecision();

    @Override
    public Product decide(SimulationEnvironment environment, ConsumerAgent agent, Collection<? extends Product> potentialProducts) {
        for(Product p: potentialProducts) {
            return p;
        }
        throw new IllegalStateException();
    }
}
