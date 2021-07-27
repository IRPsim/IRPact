package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_C",
        gams = @Gams(
                description = "Kältesektor",
                identifier = "Kältesektor",
                hidden = Constants.TRUE1,
                defaultValue = "C"
        )
)
public class SectorC extends Sector {

    public SectorC() {
    }

    public SectorC(String name) {
        _name = name;
    }
}
