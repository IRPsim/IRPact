package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProcessFindingScheme extends Nameable {

    ProcessModel findModel(Product product);
}
