package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_O",
        gams = @Gams(
                description = "Ölsektor",
                identifier = "Ölsektor",
                hidden = Constants.TRUE1,
                defaultValue = "O"
        )
)
public class SectorO extends Sector {

    public SectorO() {
    }

    public SectorO(String name) {
        _name = name;
    }
}
