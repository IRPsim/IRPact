package de.unileipzig.irpact.io.input.spatial;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Raeumliche Modell", "Space2D"},
                priorities = {"-3", "0"}
        )
)
public class InSpace2D implements InSpatialModel {

    public String _name;

    @FieldDefinition
    public boolean useEuclid;

    public InSpace2D() {
    }

    public InSpace2D(String name) {
        this._name = name;
        useEuclid = true;
    }

    public String getName() {
        return _name;
    }

    public boolean useEuclid() {
        return useEuclid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSpace2D)) return false;
        InSpace2D space2D = (InSpace2D) o;
        return useEuclid == space2D.useEuclid && Objects.equals(_name, space2D._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, useEuclid);
    }

    @Override
    public String toString() {
        return "InSpace2D{" +
                "_name='" + _name + '\'' +
                ", useEuclid=" + useEuclid +
                '}';
    }
}