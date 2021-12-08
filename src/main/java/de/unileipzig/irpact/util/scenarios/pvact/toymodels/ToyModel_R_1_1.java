package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
public class ToyModel_R_1_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_R_1_1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_R_1_1");
    }

    @Override
    protected void initTestData() {
        testData.setGlobalModifier(row -> {
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA2(dirac0);
            cag.setA3(dirac0);
            cag.setA4(dirac0);
            cag.setA7(dirac0);
            cag.setA8(dirac0);

            cag.setB6(dirac0);

            cag.setC1(dirac0);

            cag.setD1(dirac1);
            cag.setD2(dirac1);
            cag.setD3(dirac05);
            cag.setD4(dirac05);
            cag.setD5(dirac0);
        });

        cagManager.useDefaultMilieus();
    }

    @Override
    protected void initThisCustom() {
        simulationStartYear = 2005;
        simulationLength = 15;
    }

    @Override
    protected void createTopology(InRoot root, String name) {
        createFreeTopology(root, name);
    }

    @Override
    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.5);
        mpm.getPpWeightModule().setScalar(0.5);

        mpm.getCommunicationModule().setAdopterPoints(1);
        mpm.getCommunicationModule().setInterestedPoints(1);
        mpm.getCommunicationModule().setAwarePoints(1);
    }
}
