package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
public class ToyModel_D_B1 extends AbstractToyModel {

    public static final int REVISION = 0;

    protected String c1Name;
    protected double c1Value;
    protected InUnivariateDoubleDistribution c1dist;

    public ToyModel_D_B1(
            String name,
            String creator,
            String description,
            String c1Name,
            double c1Value,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        this.c1Name = c1Name;
        this.c1Value = c1Value;
        setRevision(REVISION);
    }

    public ToyModel_D_B1 withMultiplier(double c1Multiplier) {
        return new ToyModel_D_B1(
                getName(),
                getCreator(),
                getDescription(),
                c1Name,
                c1Multiplier,
                resultConsumer
        );
    }

    protected InUnivariateDoubleDistribution getC1Distribution() {
        if(c1dist == null) {
            InDiracUnivariateDistribution dist = new InDiracUnivariateDistribution();
            dist.setName(c1Name);
            dist.setValue(c1Value);
            c1dist = dist;
        }
        return c1dist;
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
                100,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "P",
                300,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "M",
                1600,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "L",
                500,
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
            cag.setA3(dirac0);
            cag.setA4(dirac0);
            cag.setA7(dirac0);
            cag.setA8(dirac0);

            cag.setB6(dirac0);

            cag.setC1(getC1Distribution());

            cag.setD2(dirac1);
            cag.setD3(dirac049);
            cag.setD4(dirac049);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "S",
                20,
                darr(0, 0.08, 0.15, 0.05, 0),
                cag -> {
                    cag.setD1(bernoulli1);
                    cag.setD5(bernoulli0);
                }
        );

        cagManager.register(
                "P",
                20,
                darr(0, 0.7, 0.25, 0.05, 0),
                cag -> {
                    cag.setD1(bernoulli015);
                    cag.setD5(bernoulli01);
                }
        );

        cagManager.register(
                "M",
                20,
                darr(0, 0.3, 0.6, 0.1, 0),
                cag -> {
                    cag.setD1(bernoulli005);
                    cag.setD5(bernoulli003);
                }
        );

        cagManager.register(
                "L",
                20,
                darr(0, 0.05, 0.15, 0.8, 0),
                cag -> {
                    cag.setD1(bernoulli001);
                    cag.setD5(bernoulli001);
                }
        );

        cagManager.register(
                "K",
                20,
                darr(0, 0, 0, 0, 1),
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
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.5);
        mpm.getPpWeightModule().setScalar(0.5);

//        mpm.getCommunicationModule().setAdopterPoints(1);
//        mpm.getCommunicationModule().setInterestedPoints(1);
//        mpm.getCommunicationModule().setAwarePoints(1);
    }
}
