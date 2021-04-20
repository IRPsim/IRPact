package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_W",
        gams = @Gams(
                description = "Wärmesektor",
                identifier = "Wärmesektor",
                hidden = Constants.TRUE1,
                defaultValue = "W"
        )
)
public class SectorW extends Sector {

    public SectorW() {
    }

    public SectorW(String name) {
        _name = name;
    }
}
