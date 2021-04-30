package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.develop.AddToPersist;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@AddToPersist("+ aktualisierung in wo auch immer")
public class BasicProductRelatedConsumerAgentAttribute extends NameableBase implements ProductRelatedConsumerAgentAttribute {

    protected ProductRelatedConsumerAgentGroupAttribute group;
    protected Map<Product, ConsumerAgentValueAttribute> mapping;

    public BasicProductRelatedConsumerAgentAttribute() {
        this(CollectionUtil.newMap());
    }

    public BasicProductRelatedConsumerAgentAttribute(String name) {
        this(CollectionUtil.newMap());
        setName(name);
    }

    public BasicProductRelatedConsumerAgentAttribute(Map<Product, ConsumerAgentValueAttribute> mapping) {
        this.mapping = mapping;
    }

    @Override
    public Collection<Product> getProducts() {
        return mapping.keySet();
    }

    @Override
    public boolean hasAttribute(Product product) {
        return mapping.containsKey(product);
    }

    @Override
    public ConsumerAgentValueAttribute getAttribute(Product product) {
        return mapping.get(product);
    }

    @Override
    public void set(Product product, ConsumerAgentValueAttribute attribute) {
        if(!Objects.equals(getName(), attribute.getName())) {
            throw ExceptionUtil.create(IllegalArgumentException::new, "name mismatch, this name '{}' != input name '{}'", getName(), attribute.getName());
        }
        if(hasAttribute(product)) {
            ConsumerAgentValueAttribute oldAttr = getAttribute(product);
            throw ExceptionUtil.create(IllegalArgumentException::new, "product '{}' already has attribute '{}', try to set '{}'", product.getName(), oldAttr.getName(), attribute.getName());
        }
        mapping.put(product, attribute);
    }

    public void setGroup(ProductRelatedConsumerAgentGroupAttribute group) {
        this.group = group;
    }

    @Override
    public ProductRelatedConsumerAgentGroupAttribute getGroup() {
        return group;
    }
}
