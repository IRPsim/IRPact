package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_NR",
        gams = @Gams(
                description = "Reservesektor",
                identifier = "Reservesektor",
                hidden = Constants.TRUE1,
                defaultValue = "NR"
        )
)
public class SectorNR extends Sector {

    public SectorNR() {
    }

    public SectorNR(String name) {
        _name = name;
    }
}
