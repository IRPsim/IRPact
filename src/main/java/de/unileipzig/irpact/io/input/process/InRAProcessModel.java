package de.unileipzig.irpact.io.input.process;

import de.unileipzig.irpact.io.spec.SpecificationConstants;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InRAProcessModel implements InProcessModel {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Relative Agreement")
                .setEdnPriority(0)
                .putCache("Relative Agreement");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InRAProcessModel.class,
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement")
        );
    }

    public String _name;

    @FieldDefinition
    public double a;

    @FieldDefinition
    public double b;

    @FieldDefinition
    public double c;

    @FieldDefinition
    public double d;

    @FieldDefinition
    public int adopterPoints = 3;

    @FieldDefinition
    public int interestedPoints = 2;

    @FieldDefinition
    public int awarePoints = 1;

    @FieldDefinition
    public int unknownPoints = 0;

    public InRAProcessModel() {
    }

    public InRAProcessModel(
            String name,
            double a, double b, double c, double d,
            int adopterPoints, int interestedPoints, int awarePoints, int unknownPoints) {
        this._name = name;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.adopterPoints = adopterPoints;
        this.interestedPoints = interestedPoints;
        this.awarePoints = awarePoints;
        this.unknownPoints = unknownPoints;
    }

    public String getName() {
        return _name;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public int getAdopterPoints() {
        return adopterPoints;
    }

    public int getInterestedPoints() {
        return interestedPoints;
    }

    public int getAwarePoints() {
        return awarePoints;
    }

    public int getUnknownPoints() {
        return unknownPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InRAProcessModel)) return false;
        InRAProcessModel that = (InRAProcessModel) o;
        return Double.compare(that.a, a) == 0 && Double.compare(that.b, b) == 0 && Double.compare(that.c, c) == 0 && Double.compare(that.d, d) == 0 && adopterPoints == that.adopterPoints && interestedPoints == that.interestedPoints && awarePoints == that.awarePoints && unknownPoints == that.unknownPoints && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, a, b, c, d, adopterPoints, interestedPoints, awarePoints, unknownPoints);
    }

    @Override
    public String toString() {
        return "InRAProcessModel{" +
                "_name='" + _name + '\'' +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", adopterPoints=" + adopterPoints +
                ", interesetedPoints=" + interestedPoints +
                ", awarePoints=" + awarePoints +
                ", unknownPoints=" + unknownPoints +
                '}';
    }
}
