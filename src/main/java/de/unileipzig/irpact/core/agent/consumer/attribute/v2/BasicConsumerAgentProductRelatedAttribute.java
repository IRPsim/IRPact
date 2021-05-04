package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.develop.AddToPersist;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class BasicConsumerAgentProductRelatedAttribute
        extends AbstractConsumerAgentAttribute
        implements ConsumerAgentProductRelatedAttribute {

    protected MapSupplier mapSupplier;
    protected Map<Product, ConsumerAgentAttribute> productMapping;

    public BasicConsumerAgentProductRelatedAttribute() {
        this(MapSupplier.getDefault());
    }

    public BasicConsumerAgentProductRelatedAttribute(MapSupplier mapSupplier) {
        this(mapSupplier, mapSupplier.newMap());
    }

    public BasicConsumerAgentProductRelatedAttribute(MapSupplier mapSupplier, Map<Product, ConsumerAgentAttribute> productMapping) {
        this.mapSupplier = mapSupplier;
        this.productMapping = productMapping;
    }

    @Override
    public BasicConsumerAgentProductRelatedAttribute copy() {
        BasicConsumerAgentProductRelatedAttribute copy = new BasicConsumerAgentProductRelatedAttribute(
                getMapSupplier(),
                deepCopy(getMapSupplier())
        );
        copy.setGroup(getGroup());
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        return copy;
    }

    protected Map<Product, ConsumerAgentAttribute> deepCopy(MapSupplier mapSupplier) {
        Map<Product, ConsumerAgentAttribute> copy = mapSupplier.newMap();
        for(Map.Entry<Product, ConsumerAgentAttribute> entry: getMapping().entrySet()) {
            copy.put(
                    entry.getKey(),
                    entry.getValue().copy()
            );
        }
        return copy;
    }

    @Override
    public void setGroup(ConsumerAgentGroupAttribute group) {
        if(group instanceof ConsumerAgentProductRelatedGroupAttribute) {
            this.group = group;
        } else {
            throw new IllegalArgumentException("requries: ConsumerAgentProductRelatedGroupAttribute");
        }
    }

    @Override
    public ConsumerAgentProductRelatedGroupAttribute getGroup() {
        return (ConsumerAgentProductRelatedGroupAttribute) group;
    }

    public MapSupplier getMapSupplier() {
        return mapSupplier;
    }

    public Map<Product, ConsumerAgentAttribute> getMapping() {
        return productMapping;
    }

    public void put(Product product, ConsumerAgentAttribute attr) {
        productMapping.put(product, attr);
    }

    @Override
    public boolean hasAttribute(Product year) {
        return productMapping.containsKey(year);
    }

    @Override
    public ConsumerAgentAttribute getAttribute(Product year) {
        return productMapping.get(year);
    }

    @Override
    public ConsumerAgentAttribute removeAttribute(Product year) {
        return productMapping.remove(year);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                ChecksumComparable.getNameChecksum(getGroup()),
                ChecksumComparable.getMapChecksumWithMapping(
                        getMapping(),
                        i -> i,
                        Nameable::getName)
        );
    }
}
