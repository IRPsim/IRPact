package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_T",
        gams = @Gams(
                description = "Holzsektor",
                identifier = "Holzsektor",
                hidden = Constants.TRUE1,
                defaultValue = "T"
        )
)
public class SectorT extends Sector {

    public SectorT() {
    }

    public SectorT(String name) {
        _name = name;
    }
}
