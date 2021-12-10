package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.FixValues;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_3_4_3 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_3_4_3(
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

        FixValues<Double> aValues = new FixValues<>(108.15, 96.39, 101.45, 99.89, 99.89);
        testData.setSizeAndModifier(
                "A",
                5,
                row -> {
                    setA1(row, aValues.next());
                    setOrientation(row, 45);
                    setSlope(row, 45);
                    return row;
                }
        );

        FixValues<Double> k1Values = new FixValues<>(79.29, 79.29, 83.65, 79.29, 81.43);
        testData.setSizeAndModifier(
                "K1",
                5,
                row -> {
                    setA1(row, k1Values.next());
                    setOrientation(row, 45);
                    setSlope(row, 45);
                    return row;
                }
        );

        FixValues<Double> k2Values = new FixValues<>(108.15, 96.39, 101.45, 99.89, 99.89);
        testData.setSizeAndModifier(
                "K2",
                5,
                row -> {
                    setA1(row, k2Values.next());
                    setOrientation(row, 15);
                    setSlope(row, 20);
                    return row;
                }
        );

        FixValues<Double> k3Values = new FixValues<>(79.29, 79.29, 83.65, 79.29, 81.43);
        testData.setSizeAndModifier(
                "K3",
                5,
                row -> {
                    setA1(row, k3Values.next());
                    setOrientation(row, 15);
                    setSlope(row, 20);
                    return row;
                }
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA3(dirac1);

            cag.setD1(dirac1);
            cag.setD2(dirac1);
            cag.setD3(dirac03);
            cag.setD4(dirac072);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "A",
                cag -> {
                    cag.setA2(dirac0);
                }
        );

        cagManager.register(
                "K1",
                cag -> {
                    cag.setA2(dirac0);
                }
        );

        cagManager.register(
                "K2",
                cag -> {
                    cag.setA2(dirac0);
                }
        );

        cagManager.register(
                "K3",
                cag -> {
                    cag.setA2(dirac0);
                }
        );
    }

    @Override
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.5);
        mpm.getPpWeightModule().setScalar(0.5);
    }
}
