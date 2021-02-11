package de.unileipzig.irpact.io.input.process;

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
public class InOrientationSupplier {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Orientierungsdaten")
                .setEdnPriority(0)
                .putCache("Orientierungsdaten");

        res.newElementBuilder()
                .setEdnLabel("Namen-Mapping")
                .setEdnPriority(0)
                .putCache("Namen-Mapping");

        res.newElementBuilder()
                .setEdnLabel("Verteilungs-Mapping")
                .setEdnPriority(1)
                .putCache("Verteilungs-Mapping");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InOrientationSupplier.class,
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Orientierungsdaten")
        );

        res.putPath(
                InOrientationSupplier.class, "attrNameInOrientation",
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Orientierungsdaten"),
                res.getCachedElement("Namen-Mapping")
        );

        res.putPath(
                InOrientationSupplier.class, "distInOrientation",
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Orientierungsdaten"),
                res.getCachedElement("Verteilungs-Mapping")
        );
    }

    public String _name;

    @FieldDefinition
    public InAttributeName attrNameInOrientation;

    @FieldDefinition
    public InUnivariateDoubleDistribution distInOrientation;

    public InOrientationSupplier() {
    }

    public InOrientationSupplier(String name, InAttributeName attributeName, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.attrNameInOrientation = attributeName;
        this.distInOrientation = distribution;
    }

    public InAttributeName getAttributeName() {
        return attrNameInOrientation;
    }

    public InUnivariateDoubleDistribution getCagAttrDistribution() {
        return distInOrientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InOrientationSupplier)) return false;
        InOrientationSupplier that = (InOrientationSupplier) o;
        return Objects.equals(_name, that._name) && Objects.equals(attrNameInOrientation, that.attrNameInOrientation) && Objects.equals(distInOrientation, that.distInOrientation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, attrNameInOrientation, distInOrientation);
    }

    @Override
    public String toString() {
        return "InOrientationSupplier{" +
                "_name='" + _name + '\'' +
                ", attrNameInOrientation=" + attrNameInOrientation +
                ", distInOrientation=" + distInOrientation +
                '}';
    }
}
