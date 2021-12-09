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
public class ToyModel_S_3_5_3 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_3_5_3(
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

        FixValues<Double> sValues = new FixValues<>(108.15, 96.39, 101.45, 99.89, 99.89, 108.15, 96.39, 101.45, 99.89, 99.89);
        testData.setSizeAndModifier(
                "S",
                10,
                row -> {
                    setA1(row, sValues.next());
                    setOrientation(row, 45);
                    setSlope(row, 45);
                    return row;
                }
        );

        FixValues<Double> aValues = new FixValues<>(108.15, 96.39, 101.45, 99.89, 99.89, 108.15, 96.39, 101.45, 99.89, 99.89);
        testData.setSizeAndModifier(
                "A",
                10,
                row -> {
                    setA1(row, aValues.next());
                    setOrientation(row, 45);
                    setSlope(row, 45);
                    return row;
                }
        );

        FixValues<Double> kValues = new FixValues<>(79.29, 79.29, 83.65, 79.29, 81.43, 79.29, 79.29, 83.65, 79.29, 81.43);
        testData.setSizeAndModifier(
                "K",
                10,
                row -> {
                    setA1(row, kValues.next());
                    setOrientation(row, 15);
                    setSlope(row, 20);
                    return row;
                }
        );

        FixValues<Double> hValues = new FixValues<>(108.15, 96.39, 101.45, 99.89, 99.89, 108.15, 96.39, 101.45, 99.89, 99.89);
        testData.setSizeAndModifier(
                "H",
                10,
                row -> {
                    setA1(row, hValues.next());
                    setOrientation(row, 45);
                    setSlope(row, 45);
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
            cag.setD3(dirac03);
            cag.setD4(dirac085);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "S",
                0,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setA4(dirac1);
                }
        );

        cagManager.register(
                "A",
                0,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setA4(dirac1);
                }
        );

        cagManager.register(
                "K",
                0,
                darr(0, 0, 1, 0),
                cag -> {
                    cag.setA4(dirac1);
                }
        );

        cagManager.register(
                "H",
                0,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setA4(dirac0);
                }
        );
    }

    @Override
    protected void createToyModelAffinities(InRoot root, String name) {
        root.setAffinities(cagManager.createAffinities("Affinities"));
    }

    @Override
    protected void createTopology(InRoot root, String name) {
        createFreeTopology(root, name);
    }

    @Override
    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.25);
        mpm.getPpWeightModule().setScalar(0.25);
        mpm.getEnvWeightModule().setScalar(0.5);
    }
}
