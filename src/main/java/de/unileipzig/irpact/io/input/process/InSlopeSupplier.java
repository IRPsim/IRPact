package de.unileipzig.irpact.io.input.process;

import de.unileipzig.irpact.io.input.InAttributeName;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Prozessmodell", "Relative Agreement", "Neigungsdaten"},
                priorities = {"-4", "0", "1"}
        )
)
public class InSlopeSupplier {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Prozessmodell", "Relative Agreement", "Neigungsdaten", "Namen-Mapping"},
                    priorities = {"-4", "0", "1", "0"}
            )
    )
    public InAttributeName attrNameSlope;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Prozessmodell", "Relative Agreement", "Neigungsdaten", "Verteilungs-Mapping"},
                    priorities = {"-4", "0", "1", "1"}
            )
    )
    public InUnivariateDoubleDistribution distSlope;

    public InSlopeSupplier() {
    }

    public InSlopeSupplier(String name, InAttributeName attributeName, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.attrNameSlope = attributeName;
        this.distSlope = distribution;
    }

    public InAttributeName getAttributeName() {
        return attrNameSlope;
    }

    public InUnivariateDoubleDistribution getCagAttrDistribution() {
        return distSlope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSlopeSupplier)) return false;
        InSlopeSupplier that = (InSlopeSupplier) o;
        return Objects.equals(_name, that._name) && Objects.equals(attrNameSlope, that.attrNameSlope) && Objects.equals(distSlope, that.distSlope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, attrNameSlope, distSlope);
    }

    @Override
    public String toString() {
        return "InSlopeSupplier{" +
                "_name='" + _name + '\'' +
                ", attrNameSlope=" + attrNameSlope +
                ", distSlope=" + distSlope +
                '}';
    }
}
