package de.unileipzig.irpact.io.param.input.irpopt;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "ii",
        gams = @Gams(
                description = "Simulationshorizont",
                identifier = "SH",
                hidden = Constants.TRUE1,
                type = Constants.GAMS_TIMESERIES
        )
)
public class Ii {
}
