package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_PR",
        gams = @Gams(
                description = "Reservesektor",
                identifier = "Reservesektor",
                hidden = Constants.TRUE1,
                defaultValue = "PR"
        )
)
public class SectorPR extends Sector {

    public SectorPR() {
    }

    public SectorPR(String name) {
        _name = name;
    }
}
