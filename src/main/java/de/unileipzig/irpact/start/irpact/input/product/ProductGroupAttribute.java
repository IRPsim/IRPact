package de.unileipzig.irpact.start.irpact.input.product;

import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.start.irpact.input.distribution.UnivariateDistribution;
import de.unileipzig.irptools.defstructure.annotation.*;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = "Product/ProductGroupAttribute"
        )
)
public class ProductGroupAttribute {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    identifier = "distributionProductGroupAttribute"
            ),
            edn = @EdnParameter(
                    path = "Link/ProductGroupAttribute-UnivariateDistribution"
            )
    )
    public UnivariateDistribution distribution;

    public ProductGroupAttribute() {
    }

    public ProductGroupAttribute(String name, UnivariateDistribution distribution) {
        this._name = name;
        this.distribution = distribution;
    }

    //=========================
    //helper
    //=========================

    public BasicProductGroupAttribute createInstance() {
        return new BasicProductGroupAttribute(_name, distribution.createInstance());
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductGroupAttribute that = (ProductGroupAttribute) o;
        return Objects.equals(_name, that._name) &&
                Objects.equals(distribution, that.distribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, distribution);
    }

    @Override
    public String toString() {
        return "ProductGroupAttribute{" +
                "_name='" + _name + '\'' +
                ", distribution=" + distribution +
                '}';
    }
}
