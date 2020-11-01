package de.unileipzig.irpact.v2.io.input.time;

import de.unileipzig.irpact.v2.ForTests;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = "TimeModel/Continuous"
        )
)
public class IContinuousTimeModel implements ITimeModel {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = "86400"
            )
    )
    public double dilation;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = "86400000"
            )
    )
    @ForTests
    public long delay;

    public IContinuousTimeModel() {
    }

    public String getName() {
        return _name;
    }
}
