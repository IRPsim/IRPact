package de.unileipzig.irpact.v2.io.input.time;

import de.unileipzig.irpact.v2.ForTests;
import de.unileipzig.irpact.v2.jadex.time.DiscreteTimeModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = {"TimeModel/Discrete"}
        )
)
public class IDiscreteTimeModel implements ITimeModel {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = "1"
            )
    )
    public long delta;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = "900000"
            )
    )
    public long timePerTickInMs;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = "86400000"
            )
    )
    @ForTests
    public long discDelay;

    public IDiscreteTimeModel() {
    }

    public IDiscreteTimeModel(String name, long delta, long timePerTickInMs, long discDelay) {
        this._name = name;
        this.delta = delta;
        this.timePerTickInMs = timePerTickInMs;
        this.discDelay = discDelay;
    }

    public String getName() {
        return _name;
    }

    @Override
    public DiscreteTimeModel createInstance() {
        DiscreteTimeModel model = new DiscreteTimeModel();
        model.setStoredDelta(delta);
        model.setStoredTimePerTickInMs(timePerTickInMs);
        model.setStoredDelay(discDelay);
        return model;
    }
}
