package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_G",
        gams = @Gams(
                description = "Gassektor",
                identifier = "Gassektor",
                hidden = Constants.TRUE1,
                defaultValue = "G"
        )
)
public class SectorG extends Sector {

    public SectorG() {
    }

    public SectorG(String name) {
        _name = name;
    }
}
