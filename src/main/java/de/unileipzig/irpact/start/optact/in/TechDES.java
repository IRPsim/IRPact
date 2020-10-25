package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "tech_DES",
        gams = @Gams(
                description = "Dezentrale Energietechnologie",
                identifier = "Dezentrale Energietechnologie",
                hidden = Constants.TRUE1
        )
)
public class TechDES extends Pss {
}
