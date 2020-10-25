package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "sector",
        gams = @Gams(
                description = "Energiesektor",
                identifier = "Energiesektor",
                hidden = Constants.TRUE1
        )
)
public class Sector {

    public String _name;

    public Sector() {
    }

    public Sector(String name) {
        _name = name;
    }
}
