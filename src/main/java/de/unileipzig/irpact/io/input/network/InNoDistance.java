package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Netzwerk", "Abstandsfunktion", "Invers"},
                priorities = {"-5", "1", "0"}
        )
)
public class InNoDistance implements InDistanceEvaluator {

    public String _name;

    @FieldDefinition
    public int placeholderNoDistance;

    public InNoDistance() {
    }

    public InNoDistance(String name) {
        this._name = name;
    }

    @Override
    public DistanceEvaluator getInstance() {
        return null; //todo haesslich, aendern!
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InNoDistance)) return false;
        InNoDistance that = (InNoDistance) o;
        return placeholderNoDistance == that.placeholderNoDistance && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, placeholderNoDistance);
    }

    @Override
    public String toString() {
        return "InNoDistance{" +
                "_name='" + _name + '\'' +
                ", placeholderNoDistance=" + placeholderNoDistance +
                '}';
    }
}
