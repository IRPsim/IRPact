package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.process.ra.InDisabledNodeFilterDistanceScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
public class ToyModel_D_S1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_D_S1(
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
                "P",
                500,
                row -> {
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "M",
                500,
                row -> {
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "L",
                500,
                row -> {
                    return row;
                }
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA2(dirac1);
            cag.setA3(dirac0);
            cag.setA4(dirac0);
            cag.setA7(dirac0);
            cag.setA8(dirac0);

            cag.setB6(dirac0);

            cag.setC1(dirac05);

            cag.setD2(dirac1);
            cag.setD3(dirac05);
            cag.setD4(dirac05);
            cag.setD5(dirac0);
            cag.setD6(dirac0);
        });

        cagManager.register(
                "P",
                20,
                darr(0.8, 0.17, 0.03),
                cag -> {
                    cag.setD1(bernoulli03);
                    cag.setD5(bernoulli03);
                }
        );

        cagManager.register(
                "M",
                20,
                darr(0.2, 0.6, 0.2),
                cag -> {
                    cag.setD1(bernoulli01);
                    cag.setD5(bernoulli01);
                }
        );

        cagManager.register(
                "L",
                20,
                darr(0.01, 0.74, 0.25),
                cag -> {
                    cag.setD1(bernoulli0);
                    cag.setD5(bernoulli0);
                }
        );
    }

    @Override
    protected void initThisCustom() {
        simulationLength = 10;
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
        mpm.getNpvWeightModule().setScalar(0.1);
        mpm.getPpWeightModule().setScalar(0.1);
        mpm.getSocialWeightModule().setScalar(0.8);

//        mpm.getCommunicationModule().setAdopterPoints(1);
//        mpm.getCommunicationModule().setInterestedPoints(1);
//        mpm.getCommunicationModule().setAwarePoints(1);
    }
}
