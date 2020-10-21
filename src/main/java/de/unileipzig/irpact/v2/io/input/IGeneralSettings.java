package de.unileipzig.irpact.v2.io.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

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

    @FieldDefinition
    public int debugLevel;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "TimeModel/Tick"
            )
    )
    public long timePerTickInMs;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "TimeModel/Continuous"
            )
    )
    public double timeDilation;

    public IGeneralSettings() {
    }

    public int getDebugLevel() {
        return debugLevel;
    }

    public long getTimePerTickInMs() {
        return timePerTickInMs;
    }

    public double getTimeDilation() {
        return timeDilation;
    }
}
