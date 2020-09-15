package de.unileipzig.irpact.start.irpact.input.product;

import de.unileipzig.irpact.core.product.BasicProduct;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.start.irpact.input.SingletonHelper;
import de.unileipzig.irptools.defstructure.annotation.*;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Product/FixedProduct"
        )
)
public class FixedProduct {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "Link/FixedProduct-ProductGroup"
            )
    )
    public ProductGroup group;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "Link/FixedProduct-ProductGroupAttribute"
            )
    )
    @MapInfo(key = ProductGroupAttribute.class, value = double.class)
    public Map<ProductGroupAttribute, Double> specifiedAttributes;

    public FixedProduct() {
    }

    public FixedProduct(String name, ProductGroup group, Map<ProductGroupAttribute, Double> specifiedAttributes) {
        this._name = name;
        this.group = group;
        this.specifiedAttributes = specifiedAttributes;
    }

    //=========================
    //helper
    //=========================

    public BasicProduct createInstance(SimulationEnvironment env, SingletonHelper helper) {
        BasicProductGroup group0 = helper.computeIfAbsent(group, _group -> _group.createInstance(env));
        Set<ProductAttribute> attrSet0 = new LinkedHashSet<>();
        if(specifiedAttributes != null) {
            for(Map.Entry<ProductGroupAttribute, Double> entry: specifiedAttributes.entrySet()) {
                BasicProductGroupAttribute grpAttr0 = helper.computeIfAbsent(
                        entry.getKey(),
                        ProductGroupAttribute::createInstance
                );
                ProductAttribute attr0 = grpAttr0.derive(entry.getValue());
                attrSet0.add(attr0);
            }
        }
        return new BasicProduct(env, _name, group0, attrSet0);
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixedProduct that = (FixedProduct) o;
        return Objects.equals(_name, that._name) &&
                Objects.equals(group, that.group) &&
                Objects.equals(specifiedAttributes, that.specifiedAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, group, specifiedAttributes);
    }

    @Override
    public String toString() {
        return "FixedProduct{" +
                "_name='" + _name + '\'' +
                ", group=" + group +
                ", specifiedAttributes=" + specifiedAttributes +
                '}';
    }
}
