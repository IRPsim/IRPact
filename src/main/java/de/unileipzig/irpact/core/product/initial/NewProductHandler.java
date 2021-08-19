package de.unileipzig.irpact.core.product.initial;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface NewProductHandler extends Nameable {

    void handleProduct(SimulationEnvironment environment, Product product);
}
