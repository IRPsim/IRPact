package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.develop.TestFiles;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InPVactUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomFileSelectedGroupedSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.graphviz.def.GraphvizGlobal;
import de.unileipzig.irptools.graphviz.def.GraphvizLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.def.GraphvizOutputFormat;
import de.unileipzig.irptools.io.base.AnnualEntry;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TimeToyModel {

    private static InRoot createRoot() {
        //===
        InAttributeName Mic_Dominantes_Milieu = new InAttributeName(RAConstants.DOM_MILIEU);
        InAttributeName PLZ = new InAttributeName(RAConstants.ZIP);

        //===
        InUnivariateDoubleDistribution constant0 = new InConstantUnivariateDistribution("constant0", 0);

        //cag0
        InPVactConsumerAgentGroup cag0 = new InPVactConsumerAgentGroup();
        cag0.setName("TRA");
        cag0.setForAll(constant0);

        //Population
        InFixConsumerAgentPopulationSize populationSize = new InFixConsumerAgentPopulationSize();
        populationSize.setName("PopSize");
        populationSize.setSize(1);
        populationSize.setConsumerAgentGroups(new InConsumerAgentGroup[]{cag0});

        //affinity
        InComplexAffinityEntry cag0_cag0 = new InComplexAffinityEntry(cag0.getName() + "_" + cag0.getName(), cag0, cag0, 1);

        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("unlinked");

        InPVFile pvFile = new InPVFile("Barwertrechner");

        InPVactUncertaintyGroupAttribute uncert = new InPVactUncertaintyGroupAttribute();
        uncert.setName("PVact_Uncert");
        uncert.setGroups(new InConsumerAgentGroup[]{cag0});
        uncert.setNoveltySeekingUncertainty(constant0);
        uncert.setDependentJudgmentMakingUncertainty(constant0);
        uncert.setEnvironmentalConcernUncertainty(constant0);

        //process
        InRAProcessModel processModel = new InRAProcessModel(
                "RA",
                0.25, 0.25, 0.25, 0.25,
                3, 2, 1, 0,
                1.0,
                null,
                pvFile,
                new InUncertaintyGroupAttribute[]{uncert}
        );

        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");
        InCustomFileSelectedGroupedSpatialDistribution2D spaDist = new InCustomFileSelectedGroupedSpatialDistribution2D(
                "testdist",
                constant0,
                constant0,
                tableFile,
                Mic_Dominantes_Milieu,
                PLZ
        );
        cag0.setSpatialDistribution(spaDist);

        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.EUCLIDEAN);

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
        general.firstSimulationYear = 2015;
        general.lastSimulationYear = 2019;

        //=====
        InRoot root = new InRoot();

        //graphviz
        GraphvizColor gc1 = GraphvizColor.RED;
        GraphvizColor gc2 = GraphvizColor.GREEN;
        root.colors = new GraphvizColor[]{gc1, gc2};

        InConsumerAgentGroupColor cag0Color = new InConsumerAgentGroupColor(cag0.getName() + "_color", cag0, gc1);
        root.setConsumerAgentGroupColors(new InConsumerAgentGroupColor[]{cag0Color});

        GraphvizLayoutAlgorithm.DOT.useLayout = false;
        GraphvizLayoutAlgorithm.CIRCO.useLayout = true;
        root.layoutAlgorithms = GraphvizLayoutAlgorithm.DEFAULTS;
        GraphvizOutputFormat.PNG.useFormat = true;
        root.outputFormats = new GraphvizOutputFormat[] { GraphvizOutputFormat.PNG };

        root.graphvizGlobal = new GraphvizGlobal();
        root.graphvizGlobal.fixedNeatoPosition = false;
        root.graphvizGlobal.scaleFactor = 0.0;

        //=====
        root.version = new InVersion[]{InVersion.currentVersion()};
        root.general = general;
        root.affinityEntries = new InComplexAffinityEntry[]{cag0_cag0};
        root.consumerAgentGroups = new InConsumerAgentGroup[]{cag0};
        root.setAgentPopulationSize(populationSize);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return root;
    }

    @Test
    void runThisModel() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x9");
        String modelName = "time-test";

        String[] args = {
                "--testMode",
                "-o", dir.resolve("scenariosX").resolve("output-" + modelName + ".json").toString(),
                "--logPath", dir.resolve("scenariosX").resolve("log-" + modelName + ".log").toString(),
                "--logConsoleAndFile",
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString()
        };

        InRoot root = createRoot();
        AnnualEntry<InRoot> entry = ToyModelUtil.buildEntry(root);
        Start.start(args, entry);
    }
}
