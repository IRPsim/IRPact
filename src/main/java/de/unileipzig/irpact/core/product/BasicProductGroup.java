package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.AbstractGroup;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroup extends AbstractGroup<Product> implements ProductGroup {

    protected Set<ProductGroupAttribute> attributes;
    protected Set<Need> needsSatisfied;

    public BasicProductGroup(
            SimulationEnvironment environment,
            Set<Product> derivedProducts,
            String name,
            Set<ProductGroupAttribute> attributes,
            Set<Need> needsSatisfied) {
        super(environment, name, derivedProducts);
        this.name = Check.requireNonNull(name, "name");
        this.attributes = Check.requireNonNull(attributes, "attributes");
        this.needsSatisfied = Check.requireNonNull(needsSatisfied, "needsSatisfied");
    }

    @Override
    public Set<ProductGroupAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public Set<Need> getNeedsSatisfied() {
        return needsSatisfied;
    }

    @Override
    public boolean addEntity(Product entitiy) {
        if(entitiy.getGroup() != this) {
            return false;
        }
        return entities.add(entitiy);
    }

    protected int productId = 0;
    protected synchronized String deriveName() {
        String name = getName() + "#" + productId;
        productId++;
        return name;
    }

    protected Set<ProductAttribute> deriveAttributes() {
        Set<ProductAttribute> attributes = new HashSet<>();
        for(ProductGroupAttribute groupAttribute: getAttributes()) {
            double attributeValue = groupAttribute.getDistribution().drawValue();
            BasicProductAttribute attribute = new BasicProductAttribute(groupAttribute, attributeValue);
            attributes.add(attribute);
        }
        return attributes;
    }

    public Product deriveProduct() {
        String derivedName = deriveName();
        Set<ProductAttribute> derivedAttributes = deriveAttributes();
        return new BasicProduct(this, derivedName, derivedAttributes);
    }
}
