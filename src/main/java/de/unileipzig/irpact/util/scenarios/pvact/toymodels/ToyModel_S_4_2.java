package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_4_2 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_4_2(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_S_4_2");
    }

    @Override
    protected void initTestData() {
        testData.setGlobalModifier(row -> {
            setA1(row, 1);
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });

        testData.setSizeAndModifier(
                "A",
                10,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "K",
                10,
                DataModifier.DO_NOTHING
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA2(dirac1);
            cag.setA3(dirac1);
            cag.setA4(dirac1);
            cag.setA8(dirac0);

            cag.setD1(dirac0);
            cag.setD2(dirac1);
            cag.setD3(dirac0);
            cag.setD4(dirac0);
            cag.setD5(dirac0);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "A",
                cag -> {
                    cag.setA8(dirac01);
                }
        );

        cagManager.register(
                "K",
                cag -> {
                    cag.setA8(dirac0);
                }
        );
    }

    @Override
    protected void initThisCustom() {
        simulationLength = 10;
    }

    @Override
    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.getNpvWeightModule().setScalar(0.125);
        mpm.getPpWeightModule().setScalar(0.125);
        mpm.getLocalWeightModule().setScalar(0.125);
        mpm.getSocialWeightModule().setScalar(0.125);
        mpm.getEnvWeightModule().setScalar(0.25);
        mpm.getNovWeightModule().setScalar(0.25);
    }
}
