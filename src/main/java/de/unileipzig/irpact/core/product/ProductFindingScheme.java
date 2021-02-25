package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.need.Need;

/**
 * @author Daniel Abitz
 */
public interface ProductFindingScheme extends Nameable {

    Product findProduct(Agent agent, Need need);
}
