package de.unileipzig.irpact.io.input.process;

import de.unileipzig.irpact.io.input.InAttributeName;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
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
                .putCache("Neigungsdaten");

        res.newElementBuilder()
                .setEdnLabel("Namen-Mapping")
                .setEdnPriority(0)
                .putCache("Namen-Mapping_Slope");

        res.newElementBuilder()
                .setEdnLabel("Verteilungs-Mapping")
                .setEdnPriority(1)
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
                InSlopeSupplier.class, "attrNameSlope",
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Neigungsdaten"),
                res.getCachedElement("Namen-Mapping_Slope")
        );

        res.putPath(
                InSlopeSupplier.class, "distSlope",
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Neigungsdaten"),
                res.getCachedElement("Verteilungs-Mapping_Slope")
        );
    }

    public String _name;

    @FieldDefinition
    public InAttributeName attrNameSlope;

    @FieldDefinition
    public InUnivariateDoubleDistribution distSlope;

    public InSlopeSupplier() {
    }

    public InSlopeSupplier(String name, InAttributeName attributeName, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.attrNameSlope = attributeName;
        this.distSlope = distribution;
    }

    public InAttributeName getAttributeName() {
        return attrNameSlope;
    }

    public InUnivariateDoubleDistribution getCagAttrDistribution() {
        return distSlope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSlopeSupplier)) return false;
        InSlopeSupplier that = (InSlopeSupplier) o;
        return Objects.equals(_name, that._name) && Objects.equals(attrNameSlope, that.attrNameSlope) && Objects.equals(distSlope, that.distSlope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, attrNameSlope, distSlope);
    }

    @Override
    public String toString() {
        return "InSlopeSupplier{" +
                "_name='" + _name + '\'' +
                ", attrNameSlope=" + attrNameSlope +
                ", distSlope=" + distSlope +
                '}';
    }
}
