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
public class ToyModel_S_3_4_4 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_3_4_4(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void initTestData() {
        testData.setGlobalModifier(row -> {
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });

        testData.setSizeAndModifier(
                "A",
                5,
                row -> {
                    setA1(row, 105);
                    setOrientation(row, 45);
                    setSlope(row, 45);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "K",
                5,
                row -> {
                    setA1(row, 95);
                    setOrientation(row, 45);
                    setSlope(row, 45);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "H",
                10,
                row -> {
                    setA1(row, 100);
                    setOrientation(row, 0);
                    setSlope(row, 0);
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
            cag.setD3(dirac100);
            cag.setD4(dirac05);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "A",
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "K",
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "H",
                cag -> {
                    cag.setD5(dirac0);
                }
        );
    }

    @Override
    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.5);
        mpm.getPpWeightModule().setScalar(0.5);
    }
}
