package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "pss",
        gams = @Gams(
                description = "Prosumstorer",
                identifier = "Prosumstorer",
                hidden = Constants.TRUE1
        )
)
public class Pss {

    public String _name;
}
