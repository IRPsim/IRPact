package de.unileipzig.irpact.start.hardcodeddemo.def.in;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Allgemeine Einstellungen",
                icon = "fa fa-map-marker",
                description = "Allgemeine Einstellungen f\u00fcr die Simulation."
        ),
        global = true
)
public class InputScalars {

    @FieldDefinition(
            gams = @GamsParameter(
                    description = "Seed f\u00fcr den Zufallsgenerator",
                    identifier = "Zufallswert",
                    unit = "[x]"
            )
    )
    public long seed;

    public InputScalars() {
    }

    public InputScalars(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "GlobalScalars{" +
                ", seed=" + seed +
                '}';
    }
}
