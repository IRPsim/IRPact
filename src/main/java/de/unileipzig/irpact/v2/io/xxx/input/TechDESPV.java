package de.unileipzig.irpact.v2.io.xxx.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "set_tech_DEGEN",
        gams = @Gams(
                hidden = "1"
        )
)
public class TechDESPV implements TechDEGEN {

    public String _name;

    public TechDESPV() {
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }
}
