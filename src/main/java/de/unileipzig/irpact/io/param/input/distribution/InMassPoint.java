package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.util.data.weighted.WeightedDouble;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DISTRIBUTIONS_MP_MP;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DISTRIBUTIONS_MP_MP)
public class InMassPoint implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public double mpValue;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public double mpWeight;

    public InMassPoint() {
    }

    public InMassPoint(String name, double value, double weight) {
        this.name = name;
        this.mpValue = value;
        this.mpWeight = weight;
    }

    @Override
    public InMassPoint copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InMassPoint newCopy(CopyCache cache) {
        InMassPoint copy = new InMassPoint();
        copy.name = name;
        copy.mpValue = mpValue;
        copy.mpWeight = mpWeight;
        return copy;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InMassPoint)) return false;
        InMassPoint that = (InMassPoint) o;
        return Double.compare(that.mpValue, mpValue) == 0
                && Double.compare(that.mpWeight, mpWeight) == 0
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mpValue, mpWeight);
    }
}
