package de.unileipzig.irpact.util.scenarios.demo;

import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.core.postprocessing.image.d2v.DataToVisualize;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.InExample;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.network.InCompleteGraphTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.mra.InModularRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.mra.component.*;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.network.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGnuPlotOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.scenarios.AbstractScenario;
import de.unileipzig.irptools.util.Util;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public class CustomModularDemo extends AbstractScenario {

    public static final int REVISION = 1;

    public CustomModularDemo(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    @Override
    public List<InRoot> createInRoots() {
        //===
        InUnivariateDoubleDistribution constant0 = new InDiracUnivariateDistribution("dirac0", 0);
        InUnivariateDoubleDistribution constant1 = new InDiracUnivariateDistribution("dirac1", 1);

        //cag0
        InPVactConsumerAgentGroup cag0 = new InPVactConsumerAgentGroup();
        cag0.setName("TRA");
        cag0.setForAll(constant0);
        cag0.setNoveltySeeking(constant1);

        //cag1
        InPVactConsumerAgentGroup cag1 = new InPVactConsumerAgentGroup();
        cag1.setName("BUM");
        cag1.setForAll(constant0);

        //Population
        InFixConsumerAgentPopulation populationSize = new InFixConsumerAgentPopulation();
        populationSize.setName("PopSize");
        populationSize.setSize(1);
        populationSize.setConsumerAgentGroups(new InConsumerAgentGroup[]{cag0, cag1});

        //affinity
        InComplexAffinityEntry cag0_cag0 = new InComplexAffinityEntry(cag0.getName() + "_" + cag0.getName(), cag0, cag0, 0.7);
        InComplexAffinityEntry cag0_cag1 = new InComplexAffinityEntry(cag0.getName() + "_" + cag1.getName(), cag0, cag1, 0.3);
        InComplexAffinityEntry cag1_cag1 = new InComplexAffinityEntry(cag1.getName() + "_" + cag1.getName(), cag1, cag1, 0.9);
        InComplexAffinityEntry cag1_cag0 = new InComplexAffinityEntry(cag1.getName() + "_" + cag0.getName(), cag1, cag0, 0.1);

        //InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("unlinked");
        InCompleteGraphTopology topology = new InCompleteGraphTopology("complete", 1.0);

        //process
        InSumAttributeComponent input = new InSumAttributeComponent();
        input.setName("NS_IN");
        input.setAttributeNames(new InAttributeName[]{new InAttributeName(RAConstants.NOVELTY_SEEKING)});
        input.setWeight(10);

        InSumIntermediateComponent middle = new InSumIntermediateComponent();
        middle.setName("NS_MID");
        middle.setComponents(new InValueComponent[]{input});
        middle.setWeight(10);

        InSumThresholdComponent output = new InSumThresholdComponent();
        output.setName("NS_OUT");
        output.setWeight(1.0);
        output.setThreshold(50);
        output.setComponents(new InValueComponent[]{middle});

        InDoNothingComponent ignore = new InDoNothingComponent();
        ignore.setName("IGNORE");

        InPVactGroupBasedDeffuantUncertainty uncertainty = new InPVactGroupBasedDeffuantUncertainty();
        uncertainty.setName("UNCERT");
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(new InConsumerAgentGroup[]{cag0, cag1});

        InModularRAProcessModel processModel = new InModularRAProcessModel();
        processModel.setName("MRA");
        processModel.setUncertainty(uncertainty);
        processModel.setDefaultValues();
        processModel.setSpeedOfConvergence(0);
        processModel.setInterestComponent(output);
        processModel.setFeasibilityComponent(output);
        processModel.setDecisionMakingComponent(output);
        processModel.setActionComponent(ignore);

        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");
        InFileBasedPVactMilieuSupplier spaDist = new InFileBasedPVactMilieuSupplier();
        spaDist.setName("testdist");
        spaDist.setFile(tableFile);
        cag0.setSpatialDistribution(spaDist);
        cag1.setSpatialDistribution(spaDist);

        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.EUCLIDEAN);

        //images
        List<InOutputImage> images = new ArrayList<>();
        Collections.addAll(images, InGenericOutputImage.createDefaultImages());
        images.forEach(InOutputImage::enableAll);
        images.add(new InGnuPlotOutputImage("Bild1", DataToVisualize.ANNUAL_ZIP, true));
        images.add(new InGnuPlotOutputImage("Bild2", DataToVisualize.COMPARED_ANNUAL_ZIP, true));
        images.add(new InGnuPlotOutputImage("Bild3", DataToVisualize.CUMULATIVE_ANNUAL_PHASE, true));

        //time
        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel("DiscreteUnitStep", 1, ChronoUnit.WEEKS);

        //general
        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.setLogLevel(IRPLevel.ALL);
        general.logAllIRPact = true;
        general.enableAllDataLogging();
        general.enableAllResultLogging();
        general.enableAllScriptLogging();
        general.setFirstSimulationYear(2015);

        //=====
        InRoot root = createRootWithInformationsWithFullLogging();
        InExample.initOptAct(root);
        InExample.initGV(root);

//        PredefinedPostAgentCreationTask task = new PredefinedPostAgentCreationTask();
//        task.setInfo("INC");
//        task.setTask(PredefinedPostAgentCreationTask.ADD_ONE_AGENT_TO_EVERY_GROUP);
//        VisibleBinaryData vbd = new VisibleBinaryData();
//        vbd.setID(task.getID());
//        vbd.setBytes(task.getBytes());
//        root.visibleBinaryData = new VisibleBinaryData[]{vbd};

        //graphviz
        InConsumerAgentGroupColor cag0Color = InConsumerAgentGroupColor.RED.derive(cag0);
        InConsumerAgentGroupColor cag1Color = InConsumerAgentGroupColor.GREEN.derive(cag1);
        root.setConsumerAgentGroupColors(new InConsumerAgentGroupColor[]{cag0Color, cag1Color});

        root.getGraphvizGeneral().setStoreEndImage(true);
        root.getGraphvizGeneral().setStoreDotFile(true);
        root.getGraphvizGeneral().setTerminalCharset(Util.IBM850());

        //=====
        root.general = general;
        root.setAffinities(new InAffinities("affs", new InComplexAffinityEntry[]{cag0_cag0, cag0_cag1, cag1_cag1, cag1_cag0}));
        root.consumerAgentGroups = new InConsumerAgentGroup[]{cag0, cag1};
        root.setAgentPopulationSize(populationSize);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};
        root.setImages(images);
        root.getGeneral().setSkipPersist(true);

        return Collections.singletonList(root);
    }
}