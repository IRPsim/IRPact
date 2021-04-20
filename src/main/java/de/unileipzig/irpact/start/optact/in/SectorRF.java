package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector_RF",
        gams = @Gams(
                description = "Abfallsektor",
                identifier = "Abfallsektor",
                hidden = Constants.TRUE1,
                defaultValue = "RF"
        )
)
public class SectorRF extends Sector {

    public SectorRF() {
    }

    public SectorRF(String name) {
        _name = name;
    }
}
