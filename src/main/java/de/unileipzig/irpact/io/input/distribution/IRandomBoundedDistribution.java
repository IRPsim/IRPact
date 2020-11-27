package de.unileipzig.irpact.io.input.distribution;

import de.unileipzig.irpact.commons.distribution.RandomBoundedDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Distribution/RandomBoundedDistribution"}
        )
)
public class IRandomBoundedDistribution implements IUnivariateDoubleDistribution {

    public String _name;

    @FieldDefinition
    public double rndDistLowerBound;

    @FieldDefinition
    public double rndDistUpperBound;

    @FieldDefinition
    public long rndDistSeed;

    public IRandomBoundedDistribution() {
    }

    public IRandomBoundedDistribution(String name, double rndDistLowerBound, double rndDistUpperBound, long rndDistSeed) {
        this._name = name;
        this.rndDistLowerBound = rndDistLowerBound;
        this.rndDistUpperBound = rndDistUpperBound;
        this.rndDistSeed = rndDistSeed;
    }

    public String geName() {
        return _name;
    }

    public double getRndDistLowerBound() {
        return rndDistLowerBound;
    }

    public double getRndDistUpperBound() {
        return rndDistUpperBound;
    }

    public long getRndDistSeed() {
        return rndDistSeed;
    }

    @Override
    public RandomBoundedDoubleDistribution createInstance() {
        return new RandomBoundedDoubleDistribution(geName(), getRndDistLowerBound(), getRndDistUpperBound(), getRndDistSeed());
    }
}
