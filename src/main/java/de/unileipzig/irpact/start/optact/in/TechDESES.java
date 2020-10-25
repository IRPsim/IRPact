package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "tech_DES_ES",
        gams = @Gams(
                description = "Stromspeicher",
                identifier = "Stromspeicher"
        ),
        edn = @Edn(
                path = "Sets/Stromspeicher"
        )
)
public class TechDESES extends Pss {

        @FieldDefinition(
                name = "Inc_PS_ES",
                gams = @GamsParameter(
                        description = "Bitte tragen Sie hier die spezifische Förderung der öffentlichen Hand für Stromspeicher ein",
                        identifier = "Förderung für Stromspeicher durch Politik",
                        unit = "[EUR / MWh]",
                        domain = "[0,)",
                        defaultValue = "0"
                )
        )
        public double foerderung;

        public TechDESES() {
        }

        public TechDESES(String name) {
                _name = name;
        }
}
