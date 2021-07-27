package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_HC",
        gams = @Gams(
                description = "Steinkohlesektor",
                identifier = "Steinkohlesektor",
                hidden = Constants.TRUE1,
                defaultValue = "HC"
        )
)
public class SectorHC extends Sector {

    public SectorHC() {
    }

    public SectorHC(String name) {
        _name = name;
    }
}
