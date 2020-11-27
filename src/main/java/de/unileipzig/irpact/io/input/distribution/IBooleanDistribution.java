package de.unileipzig.irpact.io.input.distribution;

import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Distribution/BooleanDistribution"}
        )
)
public class IBooleanDistribution implements IUnivariateDoubleDistribution {

    public String _name;

    @FieldDefinition
    public long boolDistSeed;

    public IBooleanDistribution() {
    }

    public IBooleanDistribution(String name, long boolDistSeed) {
        this._name = name;
        this.boolDistSeed = boolDistSeed;
    }

    public String getName() {
        return _name;
    }

    public long getBoolDistSeed() {
        return boolDistSeed;
    }

    @Override
    public BooleanDistribution createInstance() {
        return new BooleanDistribution(getName(), getBoolDistSeed());
    }
}
