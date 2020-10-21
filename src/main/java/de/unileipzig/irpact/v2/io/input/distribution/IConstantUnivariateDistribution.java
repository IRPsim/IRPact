package de.unileipzig.irpact.v2.io.input.distribution;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = "Distribution/ConstantUnivariateDistribution"
        )
)
public class IConstantUnivariateDistribution implements IUnivariateDistribution {

    public String _name;

    @FieldDefinition
    public double value;

    public IConstantUnivariateDistribution() {
    }

    public String getName() {
        return _name;
    }

    public double getValue() {
        return value;
    }
}
