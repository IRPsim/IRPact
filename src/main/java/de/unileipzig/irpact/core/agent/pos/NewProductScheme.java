package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface NewProductScheme extends Scheme {

    void handle(PointOfSaleAgent agent, Product newProduct);
}
