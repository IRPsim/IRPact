package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.CircularPositionModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.FixValues;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_3_5_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    protected int dist = 1;

    public ToyModel_S_3_5_1(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void initTestData() {
        CircularPositionModifier pmSAK = new CircularPositionModifier(42);
        pmSAK.setDist(dist * 0.5, 0);

        CircularPositionModifier pmH = new CircularPositionModifier(24);
        pmH.setDist(dist * 0.5, 0);
        pmH.setReference(pmSAK.nextPoint(4 * dist, 3 * dist));

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
                    setXY(row, pmSAK.getReference());
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
                    setXY(row, pmSAK.nextPoint());
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
                    setXY(row, pmSAK.nextPoint());
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
                    setXY(row, pmH.nextPoint());
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
            cag.setD2(dirac1);
            cag.setD3(dirac0);
            cag.setD4(dirac053);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "S",
                10,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setD5(dirac1);
                }
        );

        cagManager.register(
                "A",
                9,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "K",
                9,
                darr(0, 0, 1, 0),
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "H",
                10,
                darr(0, 1, 0, 0),
                cag -> {
                    cag.setD5(dirac0);
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
    protected InNodeDistanceFilterScheme createNodeFilter() {
        return createNodeFilterScheme(dist);
    }

    @Override
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.25);
        mpm.getPpWeightModule().setScalar(0.25);
        mpm.getLocalWeightModule().setScalar(0.25);
        mpm.getSocialWeightModule().setScalar(0.25);
    }
}