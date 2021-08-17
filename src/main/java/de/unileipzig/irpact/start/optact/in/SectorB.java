package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_B",
        gams = @Gams(
                description = "Biomassesektor",
                identifier = "Biomassesektor",
                hidden = Constants.TRUE1,
                defaultValue = "B"
        )
)
public class SectorB extends Sector {

    public SectorB() {
    }

    public SectorB(String name) {
        _name = name;
    }
}
