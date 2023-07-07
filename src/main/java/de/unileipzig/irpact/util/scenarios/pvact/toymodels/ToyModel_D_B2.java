package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
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
public class ToyModel_D_B2 extends AbstractToyModel {

    public static final int REVISION = 0;

    protected String c1Name;
    protected double c1Value;
    protected InUnivariateDoubleDistribution c1dist;

    public ToyModel_D_B2(
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

    public ToyModel_D_B2 withMultiplier(double c1Multiplier) {
        return new ToyModel_D_B2(
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
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });

        testData.setSizeAndModifier(
                "P",
                500,
                row -> {
                    setA1(row, 150);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "M",
                1500,
                row -> {
                    setA1(row, 100);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "L",
                500,
                row -> {
                    setA1(row, 50);
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
            cag.setA7(dirac0);
            cag.setA8(dirac0);

            cag.setB6(dirac0);

            cag.setC1(getC1Distribution());

            cag.setD2(dirac1);
            cag.setD3(dirac05);
            cag.setD4(dirac05);
            cag.setD5(dirac0);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "P",
                20,
                darr(0.77, 0.23, 0),
                cag -> {
                    cag.setD1(bernoulli01);
                }
        );

        cagManager.register(
                "M",
                20,
                darr(0, 0.77, 0.23),
                cag -> {
                    cag.setD1(bernoulli01);
                }
        );

        cagManager.register(
                "L",
                20,
                darr(0, 0, 1),
                cag -> {
                    cag.setD1(bernoulli01);
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
        mpm.getNpvWeightModule().setScalar(0.5);
        mpm.getPpWeightModule().setScalar(0.5);

//        mpm.getCommunicationModule().setAdopterPoints(1);
//        mpm.getCommunicationModule().setInterestedPoints(1);
//        mpm.getCommunicationModule().setAwarePoints(1);
    }
}
