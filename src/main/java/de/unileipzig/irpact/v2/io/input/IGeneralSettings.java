package de.unileipzig.irpact.v2.io.input;

import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true,
        edn = @Edn(
                path = "Allgemeine Einstellungen"
        )
)
public class IGeneralSettings {

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = DebugL
            )
    )
    public int debugLevel;

    public IGeneralSettings() {

    }

    public int getDebugLevel() {
        return debugLevel;
    }
}
