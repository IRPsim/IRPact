package de.unileipzig.irpact.io.input.time;

import de.unileipzig.irpact.jadex.time.ContinuousTimeModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"TimeModel/Continuous"}
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
    public long contDelay;

    public IContinuousTimeModel() {
    }

    public String getName() {
        return _name;
    }

    @Override
    public ContinuousTimeModel createInstance() {
        ContinuousTimeModel model = new ContinuousTimeModel();
        model.setStoredDilation(dilation);
        model.setStoredDelay(contDelay);
        return model;
    }
}
