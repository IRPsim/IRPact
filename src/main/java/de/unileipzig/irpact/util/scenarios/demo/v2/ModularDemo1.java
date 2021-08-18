package de.unileipzig.irpact.util.scenarios.demo.v2;

import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.modular.ca.InSimpleConsumerAgentMPM;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc.*;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval.InDoNothingModule_evalgraphnode;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval.InSumThresholdEvaluationModule_evalgraphnode;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.util.scenarios.pvact.AbstractPVactScenario;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ModularDemo1 extends AbstractPVactScenario {

    public static final int REVISION = 0;

    public ModularDemo1(String name, String creator, String description) {
        super(name, creator, description);
        init();
    }

    protected void init() {
        setRevision(REVISION);
    }

    protected InProcessModel createProcessModel() {
        List<InConsumerAgentModule> modules = new ArrayList<>();

        InNoveltySeekingModule_inputgraphnode novModule = new InNoveltySeekingModule_inputgraphnode();
        novModule.setName("NOVELTY_SEEKING");
        modules.add(novModule);

        InEnvironmentalConcernModule_inputgraphnode envModule = new InEnvironmentalConcernModule_inputgraphnode();
        envModule.setName("ENV_CONCERN");
        modules.add(envModule);

        InWeightedAddModule_calcgraphnode addNovEnv = new InWeightedAddModule_calcgraphnode();
        addNovEnv.setName("ADD_NOV_ENV");
        addNovEnv.setWeight1(0.4);
        addNovEnv.setWeight2(1.4);
        addNovEnv.setFirst(novModule);
        addNovEnv.setSecond(envModule);
        modules.add(addNovEnv);

        InDisaggregatedFinancialModule_inputgraphnode disFind = new InDisaggregatedFinancialModule_inputgraphnode();
        disFind.setName("DIS_FIN");
        disFind.setLogisticFactor(0.125);
        disFind.setWeight(1.0);
        modules.add(disFind);

        InDisaggregatedNPVModule_inputgraphnode disNPV = new InDisaggregatedNPVModule_inputgraphnode();
        disNPV.setName("DIS_NPV");
        disNPV.setLogisticFactor(1.125);
        disNPV.setPvFile(computePVFileIfAbsent());
        modules.add(disNPV);

        InNPVModule_inputgraphnode npvModule = new InNPVModule_inputgraphnode();
        npvModule.setName("NPV_MOD");
        npvModule.setPvFile(computePVFileIfAbsent());
        modules.add(npvModule);

        InPurchasePowerModule_inputgraphnode ppModule = new InPurchasePowerModule_inputgraphnode();
        ppModule.setName("PP_MOD");
        modules.add(ppModule);

        InFinancialComponentModule_inputgraphnode finMod = new InFinancialComponentModule_inputgraphnode();
        finMod.setName("FIN_COM_MOD");
        finMod.setPvFile(computePVFileIfAbsent());
        modules.add(finMod);

        InProductModule_calcgraphnode prodMod = new InProductModule_calcgraphnode();
        prodMod.setName("PRODUCT_MOD");
        prodMod.setInput(Arrays.asList(npvModule, ppModule, novModule));

        //eval

        InSumThresholdEvaluationModule_evalgraphnode sumEval = new InSumThresholdEvaluationModule_evalgraphnode();
        sumEval.setThreshold(42);
        sumEval.setAdoptIfAccepted(true);
        sumEval.setImpededIfFailed(false);
        sumEval.setAcceptIfBelowThreshold(false);
        sumEval.setInputs(Arrays.asList(addNovEnv, prodMod));
        modules.add(sumEval);

        //===

        InDoNothingModule_evalgraphnode master = new InDoNothingModule_evalgraphnode();
        master.setName("MASTER");
        master.setInputModules(modules);

        InSimpleConsumerAgentMPM mpm = new InSimpleConsumerAgentMPM();
        mpm.setName("MPM");
        mpm.setStartModule(master);
        return mpm;
    }

    @Override
    protected InRoot createInRoot(int year, int delta) {
        InUnivariateDoubleDistribution constant0 = new InDiracUnivariateDistribution("dirac0", 0);

        //spatial
        InFileBasedPVactMilieuSupplier spaDist = new InFileBasedPVactMilieuSupplier();
        spaDist.setName("SpatialDistribution");
        spaDist.setFile(computeSpatialFileIfAbsent());

        //cag
        InPVactConsumerAgentGroup cag0 = new InPVactConsumerAgentGroup();
        cag0.setName("TRA");
        cag0.setForAll(constant0);
        cag0.setSpatialDistribution(spaDist);
        InPVactConsumerAgentGroup[] cags = new InPVactConsumerAgentGroup[]{cag0};

        //Population
        InFixConsumerAgentPopulation populationSize = new InFixConsumerAgentPopulation();
        populationSize.setName("PopulationSize");
        populationSize.setSize(1);
        populationSize.setConsumerAgentGroups(cags);

        //affinity
        InComplexAffinityEntry cag0_cag0 = new InComplexAffinityEntry(cag0.getName() + "_" + cag0.getName(), cag0, cag0, 1);
        InAffinities affinities = new InAffinities("Affinities", new InAffinityEntry[] {cag0_cag0});

        //topo
        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("Topology");

        //process
        InProcessModel processModel = createProcessModel();

        //space
        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.HAVERSINE_KM);

        //time
        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel("DiscreteUnitStep", 1, ChronoUnit.WEEKS);

        //root
        InRoot root = createRootWithInformationsWithFullLogging(year, delta);
        root.setConsumerAgentGroups(cags);
        root.setAgentPopulationSize(populationSize);
        root.setAffinities(affinities);
        root.setProcessModel(processModel);
        root.setSpatialModel(space2D);
        root.setGraphTopologyScheme(topology);
        root.setTimeModel(timeModel);

        //general
        InGeneral general = root.getGeneral();
        general.doLogAll();
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.setPersistDisabled(true);

        return root;
    }
}
