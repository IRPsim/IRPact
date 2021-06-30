package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.core.logging.IRPLevel;
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
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.image.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.image.InGnuPlotOutputImage;
import de.unileipzig.irpact.io.param.input.image.InOutputImage;
import de.unileipzig.irpact.io.param.input.image.InROutputImage;
import de.unileipzig.irpact.io.param.input.network.InCompleteGraphTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuZipSupplier;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.graphviz.def.GraphvizGlobal;
import de.unileipzig.irptools.graphviz.def.GraphvizLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.def.GraphvizOutputFormat;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public class DefaultScenario extends AbstractScenario implements DefaultScenarioFactory {

    public static final int REVISION = 1;

    public DefaultScenario() {
        this(null, null, null);
    }

    public DefaultScenario(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    @Override
    public List<InRoot> createInRoots() {
        //===
        InUnivariateDoubleDistribution constant0 = new InDiracUnivariateDistribution("dirac0", 0);

        //cag0
        InPVactConsumerAgentGroup cag0 = new InPVactConsumerAgentGroup();
        cag0.setName("TRA");
        cag0.setForAll(constant0);

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

        InPVFile pvFile = new InPVFile("Barwertrechner");

        //process
        InPVactGroupBasedDeffuantUncertainty uncertainty = new InPVactGroupBasedDeffuantUncertainty();
        uncertainty.setName("UNCERT");
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(new InConsumerAgentGroup[]{cag0, cag1});

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA");
        processModel.setDefaultValues();
        processModel.setNodeFilterScheme(new InRAProcessPlanMaxDistanceFilterScheme("RA_maxFilter", 100, true));
        processModel.setPvFile(pvFile);
        processModel.setUncertainty(uncertainty);
        processModel.setSpeedOfConvergence(0.0);

        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");
        InFileBasedPVactMilieuZipSupplier spaDist = new InFileBasedPVactMilieuZipSupplier();
        spaDist.setName("testdist");
        spaDist.setFile(tableFile);
        cag0.setSpatialDistribution(spaDist);
        cag1.setSpatialDistribution(spaDist);

        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.EUCLIDEAN);

        //images
        List<InOutputImage> images = new ArrayList<>();
        Collections.addAll(images, InGenericOutputImage.DEFAULTS);
        images.add(new InGnuPlotOutputImage("gnutest0", InOutputImage.MODE_NOTHING));
        images.add(new InGnuPlotOutputImage("gnutest1", InOutputImage.MODE_ADOPTION_LINECHART));
        images.add(new InGnuPlotOutputImage("gnutest2", InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART));
        images.add(new InGnuPlotOutputImage("gnutest3", InOutputImage.MODE_ADOPTION_PHASE_BARCHART));
        images.add(new InROutputImage("rtest0", InOutputImage.MODE_NOTHING));
        images.add(new InROutputImage("rtest1", InOutputImage.MODE_ADOPTION_LINECHART));
        images.add(new InROutputImage("rtest2", InOutputImage.MODE_ADOPTION_INTERACTION_LINECHART));
        images.add(new InROutputImage("rtest3", InOutputImage.MODE_ADOPTION_PHASE_BARCHART));
        images.forEach(InOutputImage::disableAll);

        //time
        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel("DiscreteUnitStep", 1, ChronoUnit.WEEKS);

        //general
        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.logLevel = IRPLevel.ALL.getLevelId();
        general.logAllIRPact = true;
        general.enableAllDataLogging();
        general.enableAllResultLogging();
        general.enableAllScriptLogging();
        general.setFirstSimulationYear(2015);

        //=====
        InRoot root = createRootWithInformations();
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
        GraphvizColor gc1 = GraphvizColor.RED;
        GraphvizColor gc2 = GraphvizColor.GREEN;
        root.colors = new GraphvizColor[]{gc1, gc2};

        InConsumerAgentGroupColor cag0Color = new InConsumerAgentGroupColor(cag0.getName() + "_color", cag0, gc1);
        InConsumerAgentGroupColor cag1Color = new InConsumerAgentGroupColor(cag1.getName() + "_color", cag1, gc2);
        root.setConsumerAgentGroupColors(new InConsumerAgentGroupColor[]{cag0Color, cag1Color});

        GraphvizLayoutAlgorithm.DOT.useLayout = false;
        GraphvizLayoutAlgorithm.CIRCO.useLayout = true;
        root.layoutAlgorithms = GraphvizLayoutAlgorithm.DEFAULTS;
        GraphvizOutputFormat.PNG.useFormat = true;
        root.outputFormats = new GraphvizOutputFormat[] { GraphvizOutputFormat.PNG };

        root.graphvizGlobal = new GraphvizGlobal();
        root.graphvizGlobal.fixedNeatoPosition = false;
        root.graphvizGlobal.scaleFactor = 0.0;

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

        return Collections.singletonList(root);
    }

    @Override
    public InRoot createDefaultScenario() {
        return createInRoots().get(0);
    }
}