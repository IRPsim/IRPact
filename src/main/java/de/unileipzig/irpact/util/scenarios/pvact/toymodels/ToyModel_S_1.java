package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_S_1");
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
                10,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "K",
                10,
                DataModifier.DO_NOTHING
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA2(dirac1);
            cag.setA3(dirac1);
            cag.setA4(dirac1);

            cag.setD2(dirac1);
            cag.setD3(dirac07);
            cag.setD4(dirac07);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "A",
                cag -> {
                    cag.setD1(dirac1);
                }
        );

        cagManager.register(
                "K",
                cag -> {
                    cag.setD1(dirac0);
                }
        );
    }
}
