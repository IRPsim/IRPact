package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "load_DS",
        gams = @Gams(
                description = "Lastgang",
                identifier = "Lastgang",
                hidden = Constants.TRUE1
        )
)
public class LoadDS extends Pss {
}
