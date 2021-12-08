package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
public class ToyModel_S_7_3 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_7_3(
            String name,
            String creator,
            String description,
            String spatialDataName,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, spatialDataName, resultConsumer);
        setRevision(REVISION);
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
                "A",
                600,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "H1",
                50,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "H2",
                300,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "H3",
                50,
                DataModifier.DO_NOTHING
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA3(dirac1);
            cag.setA7(dirac0);
            cag.setA8(dirac0);

            cag.setD2(dirac1);
            cag.setD3(dirac0);
            cag.setD4(dirac05);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "A",
                20,
                darr(1, 0, 0, 0),
                cag -> {
                    cag.setA2(dirac04);
                    cag.setA4(dirac04);
                    cag.setC1(dirac005);
                    cag.setD1(dirac002);
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "H1",
                20,
                darr(1, 0, 0, 0),
                cag -> {
                    cag.setA2(dirac0);
                    cag.setA4(dirac0);
                    cag.setC1(dirac1);
                    cag.setD1(dirac1);
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "H2",
                20,
                darr(1, 0, 0, 0),
                cag -> {
                    cag.setA2(dirac07);
                    cag.setA4(dirac07);
                    cag.setC1(dirac1);
                    cag.setD1(dirac1);
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "H3",
                20,
                darr(0, 0, 0, 1),
                cag -> {
                    cag.setA2(dirac1);
                    cag.setA4(dirac1);
                    cag.setC1(dirac0);
                    cag.setD1(dirac1);
                    cag.setD5(dirac0);
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
    protected InUncertaintySupplier createUncertainty(String name) {
        return createInPVactUpdatableGlobalModerateExtremistUncertainty(name, 0.1, 0.05, 0.2);
    }

    @Override
    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getEnvWeightModule().setScalar(0.5);
        mpm.getNovWeightModule().setScalar(0.5);

        mpm.getCommunicationModule().setAdopterPoints(1);
        mpm.getCommunicationModule().setInterestedPoints(1);
        mpm.getCommunicationModule().setAwarePoints(1);
    }
}
