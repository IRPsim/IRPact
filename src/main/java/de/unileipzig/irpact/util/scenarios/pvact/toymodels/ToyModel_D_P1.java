package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalDeffuantUncertainty;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.PVactModularProcessModelManager;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
public class ToyModel_D_P1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_D_P1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_D_P1");
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
                "H1",
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
                1200,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "L",
                300,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "H2",
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

            cag.setD2(dirac1);
            cag.setD3(dirac049);
            cag.setD4(dirac049);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "H1",
                20,
                darr(0, 0.6, 0.3, 0.1, 0),
                cag -> {
                    cag.setA2(dirac1);
                    cag.setA4(dirac1);
                    cag.setC1(dirac05);
                    cag.setD1(dirac1);
                    cag.setD5(dirac1);
                }
        );

        cagManager.register(
                "P",
                20,
                darr(0.4, 0.35, 0.2, 0.05, 0),
                cag -> {
                    cag.setA2(dirac045);
                    cag.setA4(dirac045);
                    cag.setC1(dirac035);
                    cag.setD1(dirac1);
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "M",
                20,
                darr(0, 0.15, 0.7, 0.15, 0),
                cag -> {
                    cag.setA2(dirac03);
                    cag.setA4(dirac03);
                    cag.setC1(dirac02);
                    cag.setD1(dirac1);
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "L",
                20,
                darr(0, 0.05, 0.3, 0.65, 0),
                cag -> {
                    cag.setA2(dirac015);
                    cag.setA4(dirac015);
                    cag.setC1(dirac005);
                    cag.setD1(dirac1);
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "H2",
                20,
                darr(0, 0, 0, 0, 1),
                cag -> {
                    cag.setA2(dirac0);
                    cag.setA4(dirac0);
                    cag.setC1(dirac0);
                    cag.setD1(dirac1);
                    cag.setD5(dirac0);
                }
        );
    }

    @Override
    protected int getSimulationStart() {
        return 2005;
    }

    @Override
    protected int getSimulationLength() {
        return 15;
    }

    @Override
    protected void createTopology(InRoot root, String name) {
        createFreeTopology(root, name);
    }

    @Override
    protected InPVactGlobalDeffuantUncertainty createUncertainty(String name) {
        InPVactGlobalDeffuantUncertainty uncert = createGlobalUnvertainty(name, cagManager.getCagsArray());
        uncert.setExtremistParameter(0.1);
        uncert.setModerateUncertainty(0.2);
        uncert.setExtremistUncertainty(0.05);
        return uncert;
    }

    @Override
    protected void customProcessModelSetup(PVactModularProcessModelManager mpm) {
        mpm.getNpvWeightModule().setScalar(0.5);
        mpm.getPpWeightModule().setScalar(0.5);
        mpm.getLocalWeightModule().setScalar(0);
        mpm.getSocialWeightModule().setScalar(0);
        mpm.getEnvWeightModule().setScalar(0);
        mpm.getNovWeightModule().setScalar(0);

        mpm.getCommunicationModule().setAdopterPoints(1);
        mpm.getCommunicationModule().setInterestedPoints(1);
        mpm.getCommunicationModule().setAwarePoints(1);
    }
}
