package de.unileipzig.irpact.io.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true,
        edn = @Edn(
                label = {"Allgemeine Einstellungen"},
                priorities = {"-9"}
        )
)
public class InGeneral {

    @FieldDefinition
    public long seed;

    @FieldDefinition
    public boolean runOptActDemo;

    @FieldDefinition
    public boolean logGraphCreation;

    @FieldDefinition
    public boolean logAgentCreation;

    public InGeneral() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InGeneral)) return false;
        InGeneral general = (InGeneral) o;
        return seed == general.seed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seed);
    }

    @Override
    public String toString() {
        return "InGeneral{" +
                "seed=" + seed +
                '}';
    }
}
