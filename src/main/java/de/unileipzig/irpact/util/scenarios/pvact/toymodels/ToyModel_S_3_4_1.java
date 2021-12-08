package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.FixValues;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_3_4_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_3_4_1(
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
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });

        FixValues<Double> sValues = new FixValues<>(104.69, 104.83, 105.95, 105.95, 105.95);
        testData.setSizeAndModifier(
                "S",
                5,
                row -> {
                    setA1(row, sValues.next());
                    return row;
                }
        );

        FixValues<Double> aValues = new FixValues<>(108.15, 96.39, 101.45, 99.89, 99.89);
        testData.setSizeAndModifier(
                "A",
                5,
                row -> {
                    setA1(row, aValues.next());
                    return row;
                }
        );

        FixValues<Double> kValues = new FixValues<>(79.29,79.29, 83.65, 79.29, 81.43);
        testData.setSizeAndModifier(
                "K",
                5,
                row -> {
                    setA1(row, kValues.next());
                    return row;
                }
        );
    }

    @Override
    protected void initCagManager() {
        cagManager.registerForAll(cag -> {
            cag.setA2(dirac1);
            cag.setA3(dirac1);

            cag.setD1(dirac1);
            cag.setD2(dirac1);
            cag.setD3(dirac03);
            cag.setD4(dirac047);
            cag.setD6(dirac1);
        });

        cagManager.register(
                "S",
                cag -> {
                    cag.setD5(dirac1);
                }
        );

        cagManager.register(
                "A",
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "K",
                cag -> {
                    cag.setD5(dirac0);
                }
        );
    }

    @Override
    protected void customProcessModelSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.5);
        mpm.getPpWeightModule().setScalar(0.5);
    }
}
