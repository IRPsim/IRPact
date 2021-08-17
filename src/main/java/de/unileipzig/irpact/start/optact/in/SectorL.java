package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_L",
        gams = @Gams(
                description = "Braunkohlesektor",
                identifier = "Braunkohlesektor",
                hidden = Constants.TRUE1,
                defaultValue = "L"
        )
)
public class SectorL extends Sector {

    public SectorL() {
    }

    public SectorL(String name) {
        _name = name;
    }
}
