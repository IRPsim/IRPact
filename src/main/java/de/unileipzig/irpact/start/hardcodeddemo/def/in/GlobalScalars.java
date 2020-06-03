package de.unileipzig.irpact.start.hardcodeddemo.def.in;

import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Allgemeine Einstellungen",
                description = "F\u00fcr allgemeine Einstellungen"
        ),
        global = true
)
public class GlobalScalars {

    @FieldDefinition(
            gams = @GamsParameter(
                    unit = "[x]"
            ),
            edn = @EdnParameter(
                    description = "Seed für Zufallszahlen."
            )
    )
    public long seed;

    @FieldDefinition(
            gams = @GamsParameter(
                    unit = "[x]"
            ),
            edn = @EdnParameter(
                    description = "Skalar mit zwei Tags."
            )
    )
    public long seed_two;

    public GlobalScalars() {
    }

    public GlobalScalars(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "GlobalScalars{" +
                "seed=" + seed +
                ", seed_two=" + seed_two +
                '}';
    }
}
