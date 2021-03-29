package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.util.AddToPersist;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@AddToPersist("+ aktualisierung in wo auch immer")
public class BasicProductRelatedConsumerAgentGroupAttribute extends NameableBase implements ProductRelatedConsumerAgentGroupAttribute {

    protected Map<ProductGroup, ConsumerAgentGroupAttribute> mapping;

    public BasicProductRelatedConsumerAgentGroupAttribute() {
        this(CollectionUtil.newMap());
    }

    public BasicProductRelatedConsumerAgentGroupAttribute(String name) {
        this(CollectionUtil.newMap());
        setName(name);
    }

    public BasicProductRelatedConsumerAgentGroupAttribute(Map<ProductGroup, ConsumerAgentGroupAttribute> mapping) {
        this.mapping = mapping;
    }

    @Override
    public Collection<ProductGroup> getProductGroups() {
        return mapping.keySet();
    }

    @Override
    public boolean hasAttribute(ProductGroup productGroup) {
        return mapping.containsKey(productGroup);
    }

    @Override
    public ConsumerAgentGroupAttribute getAttribute(ProductGroup productGroup) {
        return mapping.get(productGroup);
    }

    @Override
    public void set(ProductGroup productGroup, ConsumerAgentGroupAttribute groupAttribute) {
        if(!Objects.equals(getName(), groupAttribute.getName())) {
            throw ExceptionUtil.create(IllegalArgumentException::new, "name mismatch, this name '{}' != input name '{}'", getName(), groupAttribute.getName());
        }
        if(hasAttribute(productGroup)) {
            ConsumerAgentGroupAttribute oldAttr = getAttribute(productGroup);
            throw ExceptionUtil.create(IllegalArgumentException::new, "product group '{}' already has attribute '{}', try to set '{}'", productGroup.getName(), oldAttr.getName(), groupAttribute.getName());
        }
        mapping.put(productGroup, groupAttribute);
    }

    @Override
    public ConsumerAgentAttribute derive(Product product) {
        ProductGroup productGroup = product.getGroup();
        ConsumerAgentGroupAttribute groupAttribute = getAttribute(productGroup);
        if(groupAttribute == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "product group '{}' not found", productGroup.getName());
        }
        return groupAttribute.derive();
    }
}
