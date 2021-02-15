package de.unileipzig.irpact.io.input.process;

import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSlopeSupplier {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Neigungsdaten")
                .setEdnPriority(1)
                .setEdnDescription("Zieht die Neigungsdaten basierend auf der verwendeten Verteilungsfunktion.")
                .putCache("Neigungsdaten");

        res.newElementBuilder()
                .setEdnLabel("Verteilungs-Mapping")
                .setEdnPriority(0)
                .putCache("Verteilungs-Mapping_Slope");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InSlopeSupplier.class,
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Neigungsdaten")
        );

        res.putPath(
                InSlopeSupplier.class, "distSlope",
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Neigungsdaten"),
                res.getCachedElement("Verteilungs-Mapping_Slope")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Verteilungsfunktion für die Neigung")
                .setGamsDescription("Verteilungsfunktion")
                .store(InSlopeSupplier.class, "distSlope");
    }

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution distSlope;

    public InSlopeSupplier() {
    }

    public InSlopeSupplier(String name, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.distSlope = distribution;
    }

    public String getName() {
        return _name;
    }

    public InUnivariateDoubleDistribution getDistribution() {
        return distSlope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSlopeSupplier)) return false;
        InSlopeSupplier that = (InSlopeSupplier) o;
        return Objects.equals(_name, that._name) && Objects.equals(distSlope, that.distSlope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, distSlope);
    }

    @Override
    public String toString() {
        return "InSlopeSupplier{" +
                "_name='" + _name + '\'' +
                ", distSlope=" + distSlope +
                '}';
    }
}
