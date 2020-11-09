package de.unileipzig.irpact.start.irpact.input.distribution;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Distribution/RandomDistribution"
        )
)
public class RandomDistribution implements UnivariateDistribution {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    identifier = "seedRandomDistribution"
            )
    )
    public long seed;

    public RandomDistribution() {
    }

    public RandomDistribution(String name, long seed) {
        this._name = name;
        this.seed = seed;
    }

    //=========================
    //helper
    //=========================

    public de.unileipzig.irpact.commons.distribution.RandomDistribution createInstance() {
        return new de.unileipzig.irpact.commons.distribution.RandomDistribution(
                _name,
                seed
        );
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RandomDistribution that = (RandomDistribution) o;
        return seed == that.seed &&
                Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, seed);
    }

    @Override
    public String toString() {
        return "RandomBoundedDistribution{" +
                "_name='" + _name + '\'' +
                ", seed=" + seed +
                '}';
    }
}
