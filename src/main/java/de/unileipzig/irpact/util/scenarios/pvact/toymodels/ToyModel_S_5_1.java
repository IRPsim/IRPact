package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.PVactModularProcessModelManager;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_5_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_5_1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_S_5_1");
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
                9900,
                DataModifier.DO_NOTHING
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA2(dirac1);
            cag.setA3(dirac2);
            cag.setA4(dirac1);
            cag.setA8(dirac0);

            cag.setB6(dirac1);

            cag.setC1(dirac1);

            cag.setD1(dirac0);
            cag.setD2(dirac0);
            cag.setD3(dirac0);
            cag.setD4(dirac0501);
        });

        cagManager.register(
                "A",
                99,
                darr(0.5, 0.5, 0),
                cag -> {
                    cag.setA8(dirac0);
                }
        );

        cagManager.register(
                "S",
                99,
                darr(0.5, 0.5, 0),
                cag -> {
                    cag.setA8(dirac1);
                }
        );

        cagManager.register(
                "K",
                0,
                darr(0, 0, 1),
                cag -> {
                    cag.setA8(dirac0);
                }
        );
    }

    @Override
    protected int getSimulationLength() {
        return 10;
    }

    @Override
    protected void customProcessModelSetup(PVactModularProcessModelManager mpm) {
        mpm.getNpvWeightModule().setScalar(0);
        mpm.getPpWeightModule().setScalar(0);
        mpm.getLocalWeightModule().setScalar(0.5);
        mpm.getSocialWeightModule().setScalar(0.5);
        mpm.getEnvWeightModule().setScalar(0);
        mpm.getNovWeightModule().setScalar(0);
    }
}
