package de.unileipzig.irpact.start.irpact.input.distribution;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "RandomBoundedDistribution",
                path = "Distribution/RandomBoundedDistribution"
        )
)
public class RandomBoundedDistribution implements UnivariateDistribution {

    public String _name;

    @FieldDefinition
    public long seed;

    @FieldDefinition
    public double lowerBound;

    @FieldDefinition
    public double upperBound;

    public RandomBoundedDistribution() {
    }

    public RandomBoundedDistribution(String name, long seed, double lowerBound, double upperBound) {
        this._name = name;
        this.seed = seed;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    //=========================
    //helper
    //=========================

    public de.unileipzig.irpact.commons.distribution.RandomBoundedDistribution createInstance() {
        return new de.unileipzig.irpact.commons.distribution.RandomBoundedDistribution(
                _name,
                seed,
                lowerBound,
                upperBound
        );
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RandomBoundedDistribution that = (RandomBoundedDistribution) o;
        return seed == that.seed &&
                Double.compare(that.lowerBound, lowerBound) == 0 &&
                Double.compare(that.upperBound, upperBound) == 0 &&
                Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, seed, lowerBound, upperBound);
    }

    @Override
    public String toString() {
        return "RandomBoundedDistribution{" +
                "_name='" + _name + '\'' +
                ", seed=" + seed +
                ", lowerBound=" + lowerBound +
                ", upperBound=" + upperBound +
                '}';
    }
}
