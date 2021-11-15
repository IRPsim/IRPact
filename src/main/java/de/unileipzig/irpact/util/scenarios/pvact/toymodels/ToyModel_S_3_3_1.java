package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataModifier;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.PVactModularProcessModelManager;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("CodeBlock2Expr")
public class ToyModel_S_3_3_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    public ToyModel_S_3_3_1(String name, String creator, String description, BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setToyModelInputFile() {
        setSpatialDataName("Datensatz_ToyModel_S_3_3_1");
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
                "S",
                10,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "A",
                11,
                DataModifier.DO_NOTHING
        );

        testData.setSizeAndModifier(
                "K",
                11,
                DataModifier.DO_NOTHING
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
                10,
                darr(0, 1, 0),
                cag -> {
                    cag.setD5(dirac1);
                }
        );

        cagManager.register(
                "A",
                10,
                darr(0, 1, 0),
                cag -> {
                    cag.setD5(dirac0);
                }
        );

        cagManager.register(
                "K",
                10,
                darr(0, 0, 1),
                cag -> {
                    cag.setD5(dirac0);
                }
        );
    }

    @Override
    protected void createToyModelAffinities(InRoot root, String name) {
        root.setAffinities(cagManager.createAffinities("Affinities"));
    }

    @Override
    protected InNodeDistanceFilterScheme createNodeFilter() {
        return createNodeFilterScheme(1);
    }

    @Override
    protected void createTopology(InRoot root, String name) {
        createFreeTopology(root, name);
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
