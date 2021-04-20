package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_H",
        gams = @Gams(
                description = "Wasserstoffsektor",
                identifier = "Wasserstoffsektor",
                hidden = Constants.TRUE1,
                defaultValue = "H"
        )
)
public class SectorH extends Sector {

    public SectorH() {
    }

    public SectorH(String name) {
        _name = name;
    }
}
