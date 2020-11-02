package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "side_fares",
        gams = @Gams(
                description = "Tarifteilnehmer",
                identifier = "Tarifteilnehmer",
                hidden = Constants.TRUE1
        ),
        edn = @Edn(
                path = "Sets/Tarifteilnehmer"
        )
)
public class SideFares extends Side {

    public SideFares() {
    }

    public SideFares(String name) {
        _name = name;
    }
}
