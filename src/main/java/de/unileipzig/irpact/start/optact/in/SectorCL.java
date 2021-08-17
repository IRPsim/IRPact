package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_CL",
        gams = @Gams(
                description = "Chlorgassektor",
                identifier = "Chlorgassektor",
                hidden = Constants.TRUE1,
                defaultValue = "CL"
        )
)
public class SectorCL extends Sector {

    public SectorCL() {
    }

    public SectorCL(String name) {
        _name = name;
    }
}
