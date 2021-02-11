package de.unileipzig.irpact.io.input.product;

import de.unileipzig.irpact.io.input.InAttributeName;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InProductGroupAttribute {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Attribute")
                .setEdnPriority(1)
                .putCache("Attribute_Product");

        res.newElementBuilder()
                .setEdnLabel("Attribut-Namen-Mapping")
                .setEdnPriority(0)
                .putCache("Namen-Mapping_Product");

        res.newElementBuilder()
                .setEdnLabel("Attribut-Verteilungs-Mapping")
                .setEdnPriority(1)
                .putCache("Verteilungs-Mapping_Product");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InProductGroupAttribute.class,
                res.getCachedElement("Produkte"),
                res.getCachedElement("Attribute_Product")
        );

        res.putPath(
                InProductGroupAttribute.class, "attrName",
                res.getCachedElement("Produkte"),
                res.getCachedElement("Namen-Mapping_Product")
        );

        res.putPath(
                InProductGroupAttribute.class, "attrDistribution",
                res.getCachedElement("Produkte"),
                res.getCachedElement("Verteilungs-Mapping_Product")
        );
    }

    public String _name;

    @FieldDefinition
    public InAttributeName attrName;

    @FieldDefinition
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
