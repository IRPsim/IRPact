package de.unileipzig.irpact.v2.io.input;

import de.unileipzig.irpact.v2.core.misc.DebugLevel;
import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true,
        edn = @Edn(
                label = {"Allgemeine Einstellungen"}
        )
)
public class IGeneralSettings {

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = DebugLevel.DEFAULT_ID
            )
    )
    public int debugLevel;

    public IGeneralSettings() {
    }

    public int getDebugLevel() {
        return debugLevel;
    }
}
