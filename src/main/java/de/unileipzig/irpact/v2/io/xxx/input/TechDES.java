package de.unileipzig.irpact.v2.io.xxx.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "set_tech_DES",
        gams = @Gams(
                hidden = "1"
        )
)
public interface TechDES extends PSS {
}
