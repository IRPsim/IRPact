package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irpact.commons.eval.Inverse;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
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
                priorities = {"-5", "1", "1"}
        )
)
public class InInverse implements InDistanceEvaluator {

    public String _name;

    @FieldDefinition
    public int placeholderInverse;

    private BasicDistanceEvaluator instance = new BasicDistanceEvaluator(new Inverse());

    public InInverse() {
    }

    @Override
    public DistanceEvaluator getInstance() {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InInverse)) return false;
        InInverse inInverse = (InInverse) o;
        return placeholderInverse == inInverse.placeholderInverse && Objects.equals(_name, inInverse._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, placeholderInverse);
    }

    @Override
    public String toString() {
        return "InInverse{" +
                "_name='" + _name + '\'' +
                ", placeholderInverse=" + placeholderInverse +
                '}';
    }
}
