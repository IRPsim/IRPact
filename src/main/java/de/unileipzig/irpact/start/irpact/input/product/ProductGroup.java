package de.unileipzig.irpact.start.irpact.input.product;

import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Product/ProductGroup"
        )
)
public class ProductGroup {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "Links/ProductGroup-ProductGroupAttribute"
            )
    )
    public ProductGroupAttribute[] attributes;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "Links/ProductGroup-Need"
            )
    )
    public Need[] needsSatisfied;

    public ProductGroup() {
    }

    public ProductGroup(String name, ProductGroupAttribute[] attributes, Need[] needsSatisfied) {
        this._name = name;
        this.attributes = attributes;
        this.needsSatisfied = needsSatisfied;
    }

    //=========================
    //helper
    //=========================

    public BasicProductGroup createInstance(SimulationEnvironment env) {
        Set<de.unileipzig.irpact.core.product.ProductGroupAttribute> attributes0 = new LinkedHashSet<>();
        if(attributes != null) {
            for(ProductGroupAttribute attr: attributes) {
                BasicProductGroupAttribute attr0 = attr.createInstance();
                attributes0.add(attr0);
            }
        }
        Set<de.unileipzig.irpact.core.need.Need> needsSatisfied0 = new LinkedHashSet<>();
        if(needsSatisfied != null) {
            for(Need need: needsSatisfied) {
                BasicNeed need0 = need.createInstance();
                needsSatisfied0.add(need0);
            }
        }
        return new BasicProductGroup(env, new LinkedHashSet<>(), _name, attributes0, needsSatisfied0);
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductGroup that = (ProductGroup) o;
        return Objects.equals(_name, that._name) &&
                Arrays.equals(attributes, that.attributes) &&
                Arrays.equals(needsSatisfied, that.needsSatisfied);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name);
        result = 31 * result + Arrays.hashCode(attributes);
        result = 31 * result + Arrays.hashCode(needsSatisfied);
        return result;
    }

    @Override
    public String toString() {
        return "ProductGroup{" +
                "_name='" + _name + '\'' +
                ", attributes=" + Arrays.toString(attributes) +
                ", needsSatisfied=" + Arrays.toString(needsSatisfied) +
                '}';
    }
}
