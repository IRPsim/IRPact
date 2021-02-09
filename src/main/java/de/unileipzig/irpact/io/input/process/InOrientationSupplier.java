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
                label = {"Prozessmodell", "Relative Agreement", "Orientierungsdaten"},
                priorities = {"-4", "0", "0"}
        )
)
public class InOrientationSupplier {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Prozessmodell", "Relative Agreement", "Orientierungsdaten", "Namen-Mapping"},
                    priorities = {"-4", "0", "0", "0"}
            )
    )
    public InAttributeName attrNameInOrientation;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Prozessmodell", "Relative Agreement", "Orientierungsdaten", "Verteilungs-Mapping"},
                    priorities = {"-4", "0", "0", "1"}
            )
    )
    public InUnivariateDoubleDistribution distInOrientation;

    public InOrientationSupplier() {
    }

    public InOrientationSupplier(String name, InAttributeName attributeName, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.attrNameInOrientation = attributeName;
        this.distInOrientation = distribution;
    }

    public InAttributeName getAttributeName() {
        return attrNameInOrientation;
    }

    public InUnivariateDoubleDistribution getCagAttrDistribution() {
        return distInOrientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InOrientationSupplier)) return false;
        InOrientationSupplier that = (InOrientationSupplier) o;
        return Objects.equals(_name, that._name) && Objects.equals(attrNameInOrientation, that.attrNameInOrientation) && Objects.equals(distInOrientation, that.distInOrientation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, attrNameInOrientation, distInOrientation);
    }

    @Override
    public String toString() {
        return "InOrientationSupplier{" +
                "_name='" + _name + '\'' +
                ", attrNameInOrientation=" + attrNameInOrientation +
                ", distInOrientation=" + distInOrientation +
                '}';
    }
}
