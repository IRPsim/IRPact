package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_A",
        gams = @Gams(
                description = "Wassersektor",
                identifier = "Wassersektor",
                hidden = Constants.TRUE1,
                defaultValue = "A"
        )
)
public class SectorA extends Sector {

    public SectorA() {
    }

    public SectorA(String name) {
        _name = name;
    }
}
