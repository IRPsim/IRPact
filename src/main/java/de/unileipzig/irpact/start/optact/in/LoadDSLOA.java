package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "load_DSLOA",
        gams = @Gams(
                description = "Verbrauchertechnologie",
                identifier = "Verbrauchertechnologie",
                hidden = Constants.TRUE1
        )
)
public class LoadDSLOA extends LoadDS {
}
