package de.unileipzig.irpact.io.input.product;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InProductGroup {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Gruppen")
                .setEdnPriority(0)
                .putCache("Gruppen_Product");

        res.newElementBuilder()
                .setEdnLabel("Produkt-Attribut-Mapping")
                .setEdnPriority(0)
                .putCache("Produkt-Attribut-Mapping");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InProductGroup.class,
                res.getCachedElement("Produkte"),
                res.getCachedElement("Gruppen_Product")
        );

        res.putPath(
                InProductGroup.class, "pgAttributes",
                res.getCachedElement("Produkte"),
                res.getCachedElement("Produkt-Attribut-Mapping")
        );
    }

    public String _name;

    @FieldDefinition
    public InProductGroupAttribute[] pgAttributes;

    @FieldDefinition
    public int placeholderProduct;

    public InProductGroup() {
    }

    public InProductGroup(String name, InProductGroupAttribute[] attributes) {
        this._name = name;
        this.pgAttributes = attributes;
    }

    public String getName() {
        return _name;
    }

    public InProductGroupAttribute[] getAttributes() {
        return pgAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InProductGroup)) return false;
        InProductGroup that = (InProductGroup) o;
        return Objects.equals(_name, that._name) && Arrays.equals(pgAttributes, that.pgAttributes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name);
        result = 31 * result + Arrays.hashCode(pgAttributes);
        return result;
    }

    @Override
    public String toString() {
        return "InProductGroup{" +
                "_name='" + _name + '\'' +
                ", pgAttributes=" + Arrays.toString(pgAttributes) +
                '}';
    }
}
