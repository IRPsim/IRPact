package de.unileipzig.irpact.commons.attribute.v2.simple;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.core.product.Product;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicProductRelatedAttribute2
        extends AbstractMapContainerAttribute2<Product>
        implements ProductRelatedAttribute2 {

    protected Map<Product, Attribute2> map;

    public BasicProductRelatedAttribute2(String name, Map<Product, Attribute2> map) {
        setName(name);
        this.map = map;
    }

    @Override
    public BasicProductRelatedAttribute2 copy() {
        BasicProductRelatedAttribute2 copy = new BasicProductRelatedAttribute2(getName(), CollectionUtil.newInstance(map()));
        for(Map.Entry<Product, Attribute2> entry: map().entrySet()) {
            copy.set(entry.getKey(), entry.getValue().copy());
        }
        return copy;
    }

    @Override
    protected Map<Product, Attribute2> map() {
        return map;
    }
}
