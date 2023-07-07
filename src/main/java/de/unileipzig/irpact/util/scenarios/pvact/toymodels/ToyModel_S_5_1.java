package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.process.ra.InDisabledNodeFilterDistanceScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_5_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_5_1(
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
            setA1(row, 1);
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });

        testData.setSizeAndModifier(
                "A",
                50,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "S",
                50,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "K",
                50,
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

            cag.setB6(dirac1);

            cag.setC1(dirac0);

            cag.setD1(dirac1);
            cag.setD2(dirac1);
            cag.setD3(dirac0);
            cag.setD4(dirac08);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "A",
                5,
                darr(0.5, 0.5, 0),
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "S",
                5,
                darr(0.5, 0.5, 0),
                cag -> {
                    cag.setD5(dirac1);
                }
        );

        cagManager.register(
                "K",
                0,
                darr(0, 0, 1),
                cag -> {
                    cag.setD5(dirac0);
                }
        );
    }

    @Override
    protected void initThisCustom() {
        simulationLength = 10;
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
        return new InDisabledNodeFilterDistanceScheme("DisabledNodeFilter");
    }

    @Override
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getSocialWeightModule().setScalar(1);
    }
}
