package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

import static de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.DataSetup.*;

/**
 * @author Daniel Abitz
 */
public class ToyModel_R_1_1 extends AbstractToyModel {

    public static final int REVISION = 0;

    protected int toyYear;
    protected int toyLength;

    public ToyModel_R_1_1(
            String name,
            String creator,
            String description,
            int simulationYear,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
        this.toyYear = simulationYear;
        this.toyLength = 1;
    }

    public ToyModel_R_1_1 withYear(int year) {
        return new ToyModel_R_1_1(
                getName() + "_" + year,
                getCreator(),
                getDescription(),
                year,
                resultConsumer)
                .withPvDataName(getPVDataName())
                .withRealAdoptionDataName(getRealAdoptionDataName())
                .withSpatialDataName(getSpatialFileName())
                .castTo(AbstractToyModel.class).callInit()
                .autoCast();
    }

    @Override
    protected void initTestData() {
        testData.setUseAll(true);
        testData.setGlobalModifier(row -> {
            setA5(row, 1);
            setA6(row, 1);
            return row;
        });
    }

    @Override
    protected void initCagManager() {
        cagManager.useDefaultMilieus();
        cagManager.setSelfLinkAffinities(true);

        cagManager.getCagNames().forEach(name -> cagManager.setEdgeCount(name, 10));

        cagManager.registerForAll(cag -> {
            cag.setA2(dirac0);
            cag.setA3(dirac0);
            cag.setA4(dirac0);
            cag.setA7(dirac0);
            cag.setA8(dirac0);

            cag.setB6(dirac0);

            cag.setC1(dirac0);

            cag.setD1(dirac1);
            cag.setD2(dirac1);
            cag.setD3(dirac05);
            cag.setD4(dirac05);
            cag.setD5(dirac0);

            customCagSetup(cag);
        });
    }

    @Override
    protected void createPopulation(InRoot root, String name) {
        InFileBasedPVactConsumerAgentPopulation population = createFullPopulation(name, cagManager.getCagsArray());
        population.setUseAll(false);
        population.setDesiredSize(1341);
        root.setAgentPopulationSize(population);
    }

    protected void customCagSetup(InPVactConsumerAgentGroup cag) {
    }

    @Override
    protected void initThisCustom() {
        simulationStartYear = toyYear;
        simulationLength = toyLength;
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
    }
}
