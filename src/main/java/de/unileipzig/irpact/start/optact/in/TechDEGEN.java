package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "tech_DEGEN",
        gams = @Gams(
                description = "Erzeugertechnologie",
                identifier = "Erzeugertechnologie",
                hidden = Constants.TRUE1
        )
)
public class TechDEGEN extends TechDES {
}
