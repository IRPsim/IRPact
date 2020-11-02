package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "tech_DES_PV",
        gams = @Gams(
                description = "PV-Anlage",
                identifier = "PV-Anlage"
        ),
        edn = @Edn(
                path = "Sets/PV",
                description = {"", "Hier sind PV"}
        )
)
public class TechDESPV extends TechDEGEN {

        @FieldDefinition(
                name = "A_DES_PV",
                gams = @GamsParameter(
                        description = "Bitte geben Sie hier die gesamte Modulfläche der PV-Anlage an",
                        identifier = "Modulfläche PV-Anlage",
                        unit = "[m2]",
                        domain = "[0,)",
                        defaultValue = "0"
                )
        )
        public double a;

        public TechDESPV() {
        }

        public TechDESPV(String name) {
                _name = name;
        }
}
