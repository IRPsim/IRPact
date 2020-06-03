package de.unileipzig.irpact.start.hardcodeddemo.def.in;

import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Allgemeine Einstellungen",
                description = "allgemeine Einstellungen"
        ),
        global = true
)
public class GlobalScalars {

    @FieldDefinition(
            gams = @GamsParameter(
                    description = "Skalar mit zwei Tags.",
                    identifier = "Zufallswert",
                    unit = "[x]"
            )
    )
    public long seed_two;

    public GlobalScalars() {
    }

    public GlobalScalars(long seed) {
        this.seed_two = seed;
    }

    public long getSeed() {
        return seed_two;
    }

    @Override
    public String toString() {
        return "GlobalScalars{" +
                ", seed_two=" + seed_two +
                '}';
    }
}
