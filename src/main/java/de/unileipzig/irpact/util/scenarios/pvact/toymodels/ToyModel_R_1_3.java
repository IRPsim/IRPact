package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel_R_1_3 extends ToyModel_R_1_2 {

    public static final int REVISION = 0;

    public ToyModel_R_1_3(
            String name,
            String creator,
            String description,
            int simulationYear,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, simulationYear, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    public ToyModel_R_1_3 withYear(int year) {
        return new ToyModel_R_1_3(
                getName() + "_" + year,
                getCreator(),
                getDescription(),
                year,
                resultConsumer)
                .withPvDataName(getPVDataName())
                .withRealAdoptionDataName(getRealAdoptionDataName())
                .withSpatialDataName(getSpatialFileName())
                .castTo(AbstractToyModel.class).callInit()
                .autoCast();
    }

    @Override
    protected void setupTemplate(ToyModeltModularProcessModelTemplate mpm) {
        mpm.useRealDataBasedInitialAdopter();
    }
}
