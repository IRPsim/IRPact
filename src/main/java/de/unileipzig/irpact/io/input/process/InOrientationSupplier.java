package de.unileipzig.irpact.io.input.process;

import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.input.network.InNumberOfTies;
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
                .setEdnDescription("Zieht die Orietierungsdaten basierend auf der verwendeten Verteilungsfunktion.")
                .putCache("Orientierungsdaten");

        res.newElementBuilder()
                .setEdnLabel("Verteilungs-Mapping")
                .setEdnPriority(0)
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
                InOrientationSupplier.class, "distInOrientation",
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Orientierungsdaten"),
                res.getCachedElement("Verteilungs-Mapping")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Verteilungsfunktion für die Orientierung")
                .setGamsDescription("Verteilungsfunktion")
                .store(InOrientationSupplier.class, "distInOrientation");
    }

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution distInOrientation;

    public InOrientationSupplier() {
    }

    public InOrientationSupplier(String name, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.distInOrientation = distribution;
    }

    public String getName() {
        return _name;
    }

    public InUnivariateDoubleDistribution getDistribution() {
        return distInOrientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InOrientationSupplier)) return false;
        InOrientationSupplier that = (InOrientationSupplier) o;
        return Objects.equals(_name, that._name) && Objects.equals(distInOrientation, that.distInOrientation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, distInOrientation);
    }

    @Override
    public String toString() {
        return "InOrientationSupplier{" +
                "_name='" + _name + '\'' +
                ", distInOrientation=" + distInOrientation +
                '}';
    }
}
