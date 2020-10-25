package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "tech_DESTO",
        gams = @Gams(
                description = "Speichertechnologie",
                identifier = "Speichertechnologie",
                hidden = Constants.TRUE1
        )
)
public class TechDESTO extends Pss {
}
