package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.PVactModularProcessModelManager;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
public class ToyModel_S_7_3 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_7_3(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_S_7_3");
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
    protected int getSimulationLength() {
        return 10;
    }

    @Override
    protected void createTopology(InRoot root, String name) {
        createFreeTopology(root, name);
    }

    @Override
    protected InUncertaintySupplier createUncertainty(String name) {
        return createGlobalUnvertaintySupplier(name, 0.1, 0.05, 0.2);
    }

    @Override
    protected void customProcessModelSetup(PVactModularProcessModelManager mpmm) {
        mpmm.getNpvWeightModule().setScalar(0);
        mpmm.getPpWeightModule().setScalar(0);
        mpmm.getLocalWeightModule().setScalar(0);
        mpmm.getSocialWeightModule().setScalar(0);
        mpmm.getEnvWeightModule().setScalar(0.5);
        mpmm.getNovWeightModule().setScalar(0.5);

        mpmm.getCommunicationModule().setAdopterPoints(1);
        mpmm.getCommunicationModule().setInterestedPoints(1);
        mpmm.getCommunicationModule().setAwarePoints(1);
    }
}
