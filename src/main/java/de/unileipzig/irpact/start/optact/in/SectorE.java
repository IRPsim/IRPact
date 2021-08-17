package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_E",
        gams = @Gams(
                description = "Stromsektor",
                identifier = "Stromsektor",
                hidden = Constants.TRUE1,
                defaultValue = "E"
        )
)
public class SectorE extends Sector {

    public SectorE() {
    }

    public SectorE(String name) {
        _name = name;
    }
}
