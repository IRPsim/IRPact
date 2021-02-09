package de.unileipzig.irpact.io.input.product;

import de.unileipzig.irpact.io.input.InAttributeName;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Produkte", "Attribute"},
                priorities = {"-1", "1"}
        )
)
public class InProductGroupAttribute {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Produkte", "Attribute", "Namen-Mapping"},
                    priorities = {"-1", "1", "0"}
            )
    )
    public InAttributeName attrName;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Produkte", "Attribute", "Verteilungs-Mapping"},
                    priorities = {"-1", "1", "1"}
            )
    )
    public InUnivariateDoubleDistribution attrDistribution;

    public InProductGroupAttribute() {
    }

    public InProductGroupAttribute(String name, InAttributeName attributeName, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.attrName = attributeName;
        this.attrDistribution = distribution;
    }

    public String getName() {
        return _name;
    }

    public InAttributeName getAttrName() {
        return attrName;
    }

    public InUnivariateDoubleDistribution getAttrDistribution() {
        return attrDistribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InProductGroupAttribute)) return false;
        InProductGroupAttribute that = (InProductGroupAttribute) o;
        return Objects.equals(_name, that._name) && Objects.equals(attrName, that.attrName) && Objects.equals(attrDistribution, that.attrDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, attrName, attrDistribution);
    }

    @Override
    public String toString() {
        return "InProductGroupAttribute{" +
                "_name='" + _name + '\'' +
                ", attrName=" + attrName +
                ", attrDistribution=" + attrDistribution +
                '}';
    }
}
