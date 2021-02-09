package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProcessFindingScheme {

    ProcessModel findModel(Product product);
}
