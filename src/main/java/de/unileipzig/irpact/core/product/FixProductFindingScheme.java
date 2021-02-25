package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.need.Need;

/**
 * @author Daniel Abitz
 */
public class FixProductFindingScheme extends NameableBase implements ProductFindingScheme {

    protected Product product;

    public FixProductFindingScheme() {
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public Product findProduct(Agent agent, Need need) {
        return product;
    }
}
