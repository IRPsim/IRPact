package de.unileipzig.irpact.experimental.deprecated.input.distribution;

import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Distribution/ConstantUnivariateDistribution"}
        )
)
public class IConstantUnivariateDistribution implements IUnivariateDoubleDistribution {

    public String _name;

    @FieldDefinition
    public double constDistValue;

    public IConstantUnivariateDistribution() {
    }

    public IConstantUnivariateDistribution(String name, double constDistValue) {
        this._name = name;
        this.constDistValue = constDistValue;
    }

    public String getName() {
        return _name;
    }

    public double getConstDistValue() {
        return constDistValue;
    }

    @Override
    public ConstantUnivariateDoubleDistribution createInstance() {
        return new ConstantUnivariateDoubleDistribution(getName(), getConstDistValue());
    }
}
