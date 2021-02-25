package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.WeightedDouble;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
@Todo("ref einfuegen")
public class InMassPoint implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InMassPoint.class,
                res.getCachedElement("Verteilungsfunktionen"),
                res.getCachedElement("FiniteMassPointsDiscreteDistribution"),
                res.getCachedElement("Massepunkt")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Wert des Punktes")
                .setGamsDescription("Wert des Punktes")
                .store(InMassPoint.class, "mpValue");
        res.newEntryBuilder()
                .setGamsIdentifier("Wichtun des Punktesg")
                .setGamsDescription("Wichtung des Punktes")
                .store(InMassPoint.class, "mpWeight");
    }

    public String _name;

    @FieldDefinition
    public double mpValue;

    @FieldDefinition
    public double mpWeight;

    public InMassPoint() {
    }

    public InMassPoint(String name, double value, double weight) {
        this._name = name;
        this.mpValue = value;
        this.mpWeight = weight;
    }

    public String getName() {
        return _name;
    }

    public double getValue() {
        return mpValue;
    }

    public double getWeight() {
        return mpWeight;
    }

    public WeightedDouble toWeightedDouble() {
        return new WeightedDouble(getValue(), getWeight());
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InMassPoint)) return false;
        InMassPoint that = (InMassPoint) o;
        return Double.compare(that.mpValue, mpValue) == 0
                && Double.compare(that.mpWeight, mpWeight) == 0
                && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, mpValue, mpWeight);
    }
}
