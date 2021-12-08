package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_3_5_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_3_5_1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_S_3_5_1");
    }

    @Override
    protected void initTestData() {
        testData.setGlobalModifier(row -> {
            setA1(row, 100);
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });

        testData.setSizeAndModifier(
                "S",
                10,
                row -> {
                    setOrientation(row, 45);
                    setSlope(row, 45);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "A",
                10,
                row -> {
                    setOrientation(row, 15);
                    setSlope(row, 20);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "K",
                10,
                row -> {
                    setOrientation(row, 15);
                    setSlope(row, 20);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "H",
                10,
                row -> {
                    setOrientation(row, 15);
                    setSlope(row, 20);
                    return row;
                }
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA2(dirac1);
            cag.setA3(dirac1);

            cag.setD1(dirac1);
            cag.setD2(dirac1);
            cag.setD3(dirac03);
            cag.setD4(dirac085);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "S",
                0,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setD5(dirac1);
                }
        );

        cagManager.register(
                "A",
                0,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "K",
                0,
                darr(0, 0, 1, 0),
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "H",
                0,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setD5(dirac0);
                }
        );
    }

    @Override
    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.25);
        mpm.getPpWeightModule().setScalar(0.25);
        mpm.getLocalWeightModule().setScalar(0.25);
        mpm.getSocialWeightModule().setScalar(0.25);
    }
}
