package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicProduct extends SimulationEntityBase implements Product {

    private ProductGroup group;
    private Set<ProductAttribute> attributes;

    public BasicProduct(
            SimulationEnvironment environment,
            String name,
            ProductGroup group,
            Set<ProductAttribute> attributes) {
        super(environment, name);
        this.group = Check.requireNonNull(group, "group");
        this.attributes = Check.requireNonNull(attributes, "attributes");
    }

    @Override
    public ProductGroup getGroup() {
        return group;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<ProductAttribute> getAttributes() {
        return attributes;
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public boolean is(EntityType type) {
        switch (type) {
            case PRODUCT:
                return true;

            default:
                return false;
        }
    }
}
