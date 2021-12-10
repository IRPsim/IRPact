package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.CircularPositionModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_3_3_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    protected int dist = 1;

    public ToyModel_S_3_3_1(
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

        CircularPositionModifier pmK = new CircularPositionModifier(24);
        pmK.setDist(dist * 0.5, 0);
        pmK.setReference(pmSA.nextPoint(4 * dist, 3 * dist));

        testData.setGlobalModifier(row -> {
            setA1(row, 1);
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });

        testData.setSizeAndModifier(
                "S",
                10,
                row -> {
                    setXY(row, pmSA.getReference().getX(), pmSA.getReference().getY());
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "A",
                11,
                pmSA
        );

        testData.setSizeAndModifier(
                "K",
                11,
                pmK
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
            cag.setD4(dirac047);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "S",
                11,
                darr(0, 1, 0),
                cag -> {
                    cag.setD5(dirac1);
                }
        );

        cagManager.register(
                "A",
                10,
                darr(0, 1, 0),
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "K",
                10,
                darr(0, 0, 1),
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
    protected InNodeDistanceFilterScheme createNodeFilter() {
        return createNodeFilterScheme(dist);
    }

    @Override
    protected void createTopology(InRoot root, String name) {
        createFreeTopology(root, name);
    }

    @Override
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getLocalWeightModule().setScalar(0.5);
        mpm.getSocialWeightModule().setScalar(0.5);
    }
}
