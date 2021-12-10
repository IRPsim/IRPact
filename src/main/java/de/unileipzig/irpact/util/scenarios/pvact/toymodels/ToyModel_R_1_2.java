package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel_R_1_2 extends ToyModel_R_1_1 {

    public static final int REVISION = 0;

    public ToyModel_R_1_2(
            String name,
            String creator,
            String description,
            int simulationYear,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, simulationYear, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    public ToyModel_R_1_2 withYear(int year) {
        return new ToyModel_R_1_2(
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
}
