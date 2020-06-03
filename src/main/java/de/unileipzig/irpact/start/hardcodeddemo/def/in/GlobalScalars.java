package de.unileipzig.irpact.start.hardcodeddemo.def.in;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Allgemeine Einstellungen",
                description = "Für allgemeine Einstellungen"
        ),
        global = true
)
public class GlobalScalars {

    @FieldDefinition(
            edn = @EdnParameter(
                    description = "Seed für Zufallszahlen."
            )
    )
    public long seed;

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
                '}';
    }
}
