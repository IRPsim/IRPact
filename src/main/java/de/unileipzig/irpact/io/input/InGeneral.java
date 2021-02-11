package de.unileipzig.irpact.io.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true
)
public class InGeneral {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InGeneral.class,
                res.getCachedElement("Allgemeine Einstellungen")
        );
    }

    public static final String OPT_ACT_PAR_NAME = "sca_InGeneral_runOptActDemo";

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
