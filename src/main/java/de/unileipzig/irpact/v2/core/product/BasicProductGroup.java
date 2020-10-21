package de.unileipzig.irpact.v2.core.product;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntityBase;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroup extends SimulationEntityBase implements ProductGroup {

    protected Set<ProductGroupAttribute> attributes;

    public void setAttributes(Set<ProductGroupAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<ProductGroupAttribute> getAttributes() {
        return attributes;
    }

    public String deriveName() {
        return getName();
    }

    public Set<ProductAttribute> deriveAttributes() {
        Set<ProductAttribute> paSet = new HashSet<>();
        for(ProductGroupAttribute pga: getAttributes()) {
            ProductAttribute pa = pga.derive();
            paSet.add(pa);
        }
        return paSet;
    }

    @Override
    public Product derive() {
        return new BasicProduct(
                deriveName(),
                this,
                deriveAttributes()
        );
    }
}
