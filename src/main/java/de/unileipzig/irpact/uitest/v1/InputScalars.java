package de.unileipzig.irpact.uitest.v1;

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
                description = "Allgemeine Einstellungen fuer die Simulation."
        ),
        global = true
)
public class InputScalars {

    @FieldDefinition(
            gams = @GamsParameter(
                    description = "Seed fuer den Zufallsgenerator",
                    identifier = "Zufallswert",
                    unit = "[x]"
            )
    )
    public long seed;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = "Baumwert fuer den Zufallsgenerator",
                    identifier = "Baumwert",
                    unit = "[x]"
            )
    )
    public long baum;

    public InputScalars() {
    }

    public InputScalars(long seed, long baum) {
        this.seed = seed;
        this.baum = baum;
    }

    public static InputScalars x() {
        return new InputScalars(123, 321);
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
