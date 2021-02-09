package de.unileipzig.irpact.experimental.deprecated.input.distribution;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Distribution/Util/MassPoints"}
        )
)
public class IMassPoint {

    public String _name;

    @FieldDefinition
    public double mpValue;

    @FieldDefinition
    public double mpWeight;

    public IMassPoint() {
    }

    public IMassPoint(String name, double mpValue, double mpWeight) {
        this._name = name;
        this.mpValue = mpValue;
        this.mpWeight = mpWeight;
    }

    public String getName() {
        return _name;
    }

    public double getMpValue() {
        return mpValue;
    }

    public double getMpWeight() {
        return mpWeight;
    }
}
