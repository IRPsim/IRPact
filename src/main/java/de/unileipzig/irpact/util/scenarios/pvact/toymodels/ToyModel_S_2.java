package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.PVactCagModifier;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.setA1;

/**
 * @author Daniel Abitz
 */
public class ToyModel_S_2 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_2(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_S_2");
    }

    @Override
    protected void initTestData() {
        testData.setGlobalModifier(row -> {
            setA1(row, 1);
            return row;
        });

        testData.setSizeAndModifier(
                "A",
                10,
                row -> {
                    DataSetup.setA5(row, 1);
                    DataSetup.setA6(row, 1);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "K1",
                10,
                row -> {
                    DataSetup.setA5(row, 1);
                    DataSetup.setA6(row, 0);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "K2",
                10,
                row -> {
                    DataSetup.setA5(row, 0);
                    DataSetup.setA6(row, 1);
                    return row;
                }
        );

        testData.setSizeAndModifier(
                "K3",
                10,
                row -> {
                    DataSetup.setA5(row, 0);
                    DataSetup.setA6(row, 0);
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

            cag.setD1(dirac1);
            cag.setD2(dirac0);
            cag.setD3(dirac07);
            cag.setD4(dirac07);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "A",
                PVactCagModifier.DO_NOTHING
        );

        cagManager.register(
                "K1",
                PVactCagModifier.DO_NOTHING
        );

        cagManager.register(
                "K2",
                PVactCagModifier.DO_NOTHING
        );

        cagManager.register(
                "K3",
                PVactCagModifier.DO_NOTHING
        );
    }
}
