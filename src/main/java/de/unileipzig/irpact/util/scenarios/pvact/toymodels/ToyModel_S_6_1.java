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
public class ToyModel_S_6_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_6_1(
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
                100,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "K",
                100,
                DataModifier.DO_NOTHING
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA2(dirac1);
            cag.setA4(dirac1);

            cag.setB6(dirac0);

            cag.setC1(dirac005);

            cag.setD2(dirac1);
            cag.setD3(dirac01);
            cag.setD4(dirac07);
            cag.setD6(dirac0);
        });

        cagManager.register(
                "A",
                99,
                darr(1, 0),
                cag -> {
                    cag.setD1(dirac02);
                }
        );

        cagManager.register(
                "K",
                99,
                darr(0, 1),
                cag -> {
                    cag.setD1(dirac0);
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
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getEnvWeightModule().setScalar(1);
        //mpm.getCommunicationModule().setRaEnabled(true);
    }
}
