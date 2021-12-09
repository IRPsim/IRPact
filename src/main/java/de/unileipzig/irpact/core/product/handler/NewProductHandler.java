package de.unileipzig.irpact.core.product.handler;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Comparator;

/**
 * @author Daniel Abitz
 */
public interface NewProductHandler extends Nameable {

    Comparator<? super NewProductHandler> COMPARE_PRIORITY = Comparator.comparingInt(NewProductHandler::getPriority);

    int getPriority();

    void handleProduct(SimulationEnvironment environment, Product product);
}
