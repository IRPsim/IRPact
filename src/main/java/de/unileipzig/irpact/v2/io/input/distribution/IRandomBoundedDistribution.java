package de.unileipzig.irpact.v2.io.input.distribution;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = "Distribution/RandomBoundedDistribution"
        )
)
public class IRandomBoundedDistribution {

    public String _name;

    @FieldDefinition
    public double lowerBound;

    @FieldDefinition
    public double upperBound;

    @FieldDefinition
    public long seed;

    public IRandomBoundedDistribution() {
    }

    public String geName() {
        return _name;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public long getSeed() {
        return seed;
    }
}
