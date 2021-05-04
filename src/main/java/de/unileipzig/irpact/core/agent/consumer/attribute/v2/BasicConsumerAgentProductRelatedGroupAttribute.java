package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.DirectDerivable;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.develop.AddToPersist;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class BasicConsumerAgentProductRelatedGroupAttribute
        extends AbstractConsumerAgentGroupAttribute
        implements ConsumerAgentProductRelatedGroupAttribute {

    protected MapSupplier mapSupplier;
    protected Map<ProductGroup, ConsumerAgentGroupAttribute> yearMapping;

    public BasicConsumerAgentProductRelatedGroupAttribute() {
        this(MapSupplier.getDefault());
    }

    public BasicConsumerAgentProductRelatedGroupAttribute(MapSupplier mapSupplier) {
        this(mapSupplier, mapSupplier.newMap());
    }

    public BasicConsumerAgentProductRelatedGroupAttribute(
            MapSupplier mapSupplier,
            Map<ProductGroup, ConsumerAgentGroupAttribute> yearMapping) {
        this.mapSupplier = mapSupplier;
        this.yearMapping = yearMapping;
    }

    @Override
    public AbstractConsumerAgentGroupAttribute copy() {
        BasicConsumerAgentProductRelatedGroupAttribute copy = new BasicConsumerAgentProductRelatedGroupAttribute(
                getMapSupplier(),
                getMapSupplier().copyMap(getMapping())
        );
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        return copy;
    }

    public ConsumerAgentAttribute deriveProduct(ProductGroup productGroup) {
        DirectDerivable<? extends ConsumerAgentAttribute> derivable = yearMapping.get(productGroup);
        if(derivable == null) {
            throw new NoSuchElementException("product group '" + productGroup.getName() + "' missing");
        }
        return derivable.derive();
    }

    protected BasicConsumerAgentProductRelatedAttribute newAttribute() {
        BasicConsumerAgentProductRelatedAttribute annualAttr = new BasicConsumerAgentProductRelatedAttribute();
        annualAttr.setName(getName());
        annualAttr.setGroup(this);
        annualAttr.setArtificial(isArtificial());
        return annualAttr;
    }

    public ConsumerAgentGroupAttribute put(ProductGroup productGroup, ConsumerAgentGroupAttribute derivable) {
        return yearMapping.put(productGroup, derivable);
    }

    public ConsumerAgentGroupAttribute remove(ProductGroup productGroup) {
        return yearMapping.remove(productGroup);
    }

    @Override
    public boolean hasAttribute(ProductGroup productGroup) {
        return yearMapping.containsKey(productGroup);
    }

    @Override
    public ConsumerAgentGroupAttribute getAttribute(ProductGroup productGroup) {
        return yearMapping.get(productGroup);
    }

    @Override
    public BasicConsumerAgentProductRelatedAttribute derive() {
        return newAttribute();
    }

    @Override
    public BasicConsumerAgentProductRelatedAttribute derive(Product input) {
        BasicConsumerAgentProductRelatedAttribute newAttr = newAttribute();
        deriveUpdate(input, newAttr);
        return newAttr;
    }

    @Override
    public void deriveUpdate(Product input, ConsumerAgentProductRelatedAttribute target) {
        if(target.hasAttribute(input)) {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' already has product '{}'", target.getName(), input.getName());
        }

        ProductGroup pg = input.getGroup();
        ConsumerAgentAttribute attr = deriveProduct(pg);
        ((BasicConsumerAgentProductRelatedAttribute) target).put(input, attr);
    }

    public MapSupplier getMapSupplier() {
        return mapSupplier;
    }

    public Map<ProductGroup, ConsumerAgentGroupAttribute> getMapping() {
        return yearMapping;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                ChecksumComparable.getNamedMapChecksum(getMapping())
        );
    }
}
