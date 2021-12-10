package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel_R_2 extends ToyModel_R_1_3 {

    public static final int REVISION = 0;

    public ToyModel_R_2(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, 2000, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void initThisCustom() {
        simulationLength = 20;
    }

    @Override
    protected void initTestData() {
        testData.setUseOriginal(true);
    }
}
