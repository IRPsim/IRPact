package de.unileipzig.irpact.jadex.agent.consumer;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ProductFindingScheme;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class JadexUseKnownProducts implements ProductFindingScheme {

    public static final String NAME = "JadexUseKnownProducts";
    public static final JadexUseKnownProducts INSTANCE = new JadexUseKnownProducts();

    public JadexUseKnownProducts() {
    }

    @Override
    public Collection<Product> searchPotentialProducts(
            SimulationEnvironment environment,
            ConsumerAgent agent,
            Need need) {
        Set<Product> knownProducts = agent.getKnownProducts();
        Set<Product> potentialProducts = new HashSet<>();
        for(Product product: knownProducts) {
            if(product.satisfy(need)) {
                potentialProducts.add(product);
            }
        }
        return potentialProducts;
    }
}
