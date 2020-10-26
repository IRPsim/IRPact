package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "side",
        gams = @Gams(
                description = "Marktteilnehmer",
                identifier = "MT",
                hidden = Constants.TRUE1
        )
)
public class Side {

        public String _name;
}
