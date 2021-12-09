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
public class ToyModel_S_3_5_4 extends AbstractToyModel {

    public static final int REVISION = 0;

    protected int dist = 1;

    public ToyModel_S_3_5_4(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void initTestData() {
        CircularPositionModifier pmSA = new CircularPositionModifier(42);
        pmSA.setDist(dist * 0.5, 0);

        CircularPositionModifier pmKH = new CircularPositionModifier(24);
        pmKH.setDist(dist * 0.5, 0);
        pmKH.setReference(pmSA.nextPoint(4 * dist, 3 * dist));

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
                    setXY(row, pmSA.getReference());
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
                    setXY(row, pmSA.nextPoint());
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
                    setXY(row, pmKH.nextPoint());
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
                    setXY(row, pmKH.nextPoint());
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
            cag.setA4(dirac1);

            cag.setD1(dirac1);
            cag.setD3(dirac0);
            cag.setD4(dirac06);
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
    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(1.0/6.0);
        mpm.getPpWeightModule().setScalar(1.0/6.0);
        mpm.getLocalWeightModule().setScalar(1.0/6.0);
        mpm.getSocialWeightModule().setScalar(1.0/6.0);
        mpm.getEnvWeightModule().setScalar(1.0/3.0);
    }
}
