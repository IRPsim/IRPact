package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.DoubleTimeSeries;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "load_DS_E",
        gams = @Gams(
                description = "Strom-Verbrauchertechnologie",
                identifier = "Strom-Verbrauchertechnologie"
        ),
        edn = @Edn(
                label = "Sets/Strom-Verbrauchertechnologie"
        )
)
public class LoadDSE extends LoadDSLOA {

        @FieldDefinition(
                name = "L_DS_E",
                timeSeries = Ii.class,
                gams = @GamsParameter(
                        description = "Bitte geben Sie hier ein gewünschtes elektrische Lastprofil ein",
                        identifier = "Elektrisches Lastprofil",
                        unit = "[MWh]",
                        domain = "[0,)"
                )
        )
        public DoubleTimeSeries ldse;

        public LoadDSE() {
        }

        public LoadDSE(String name) {
                _name = name;
        }
}
