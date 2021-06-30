package de.unileipzig.irpact.util.scenarios.custom;

import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.util.img.DataToVisualize;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InScenarioVersion;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.image.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.image.InGnuPlotOutputImage;
import de.unileipzig.irpact.io.param.input.image.InOutputImage;
import de.unileipzig.irpact.io.param.input.image.InROutputImage;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InNoDistance;
import de.unileipzig.irpact.io.param.input.network.InNumberOfTies;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuZipSupplier;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.util.scenarios.AbstractScenario;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.graphviz.def.GraphvizGlobal;
import de.unileipzig.irptools.graphviz.def.GraphvizLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.def.GraphvizOutputFormat;

import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public class Demo1 extends AbstractScenario {

    public Demo1(String name, String creator, String description) {
        super(name, creator, description);
    }

    @Override
    public String getName() {
        return "Demo1";
    }

    @Override
    public List<InRoot> createInRoots() {
        InPVFile pvFile = new InPVFile("Barwertrechner");
        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");

        InDiracUnivariateDistribution diraq0 = new InDiracUnivariateDistribution("dirac0", 0);

        InFileBasedPVactMilieuZipSupplier spatialDist = new InFileBasedPVactMilieuZipSupplier();
        spatialDist.setName("SpaDis");
        spatialDist.setFile(tableFile);

        InPVactConsumerAgentGroup BUM = new InPVactConsumerAgentGroup();
        BUM.setName("BUM");
        BUM.setForAll(diraq0);
        BUM.setSpatialDistribution(spatialDist);

        InPVactConsumerAgentGroup G = new InPVactConsumerAgentGroup();
        G.setName("G");
        G.setForAll(diraq0);
        G.setSpatialDistribution(spatialDist);

        InPVactConsumerAgentGroup PRA = new InPVactConsumerAgentGroup();
        PRA.setName("PRA");
        PRA.setForAll(diraq0);
        PRA.setSpatialDistribution(spatialDist);

        InAffinities affinities = new InAffinities();
        affinities.setName("Affi");
        affinities.setEntries(new InAffinityEntry[]{
                new InNameSplitAffinityEntry(BUM, BUM, 0.50),
                new InNameSplitAffinityEntry(BUM, G, 0.25),
                new InNameSplitAffinityEntry(BUM, PRA, 0.25),

                new InNameSplitAffinityEntry(G, BUM, 0.25),
                new InNameSplitAffinityEntry(G, G, 0.50),
                new InNameSplitAffinityEntry(G, PRA, 0.25),

                new InNameSplitAffinityEntry(PRA, BUM, 0.25),
                new InNameSplitAffinityEntry(PRA, G, 0.25),
                new InNameSplitAffinityEntry(PRA, PRA, 0.50),
        });

        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName("Topo");
        topology.setInitialWeight(1.0);
        topology.setAllowLessEdges(false);
        topology.setDistanceEvaluator(new InNoDistance("NoDist"));
        topology.setAffinities(affinities);
        topology.setNumberOfTies(
                new InNumberOfTies("BUM_ties", BUM, 5),
                new InNumberOfTies("G_ties", G, 6),
                new InNumberOfTies("PRA_ties", PRA, 7)
        );

        InFileBasedPVactConsumerAgentPopulation population = new InFileBasedPVactConsumerAgentPopulation();
        population.setName("Pop");
        population.setConsumerAgentGroups(new InConsumerAgentGroup[]{BUM, G, PRA});
        population.setDesiredSize(100);
        population.setRequiresDesiredSize(true);
        population.setUseAll(false);
        population.setFile(tableFile);

        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel();
        timeModel.setName("DiscretTime");
        timeModel.setAmountOfTime(1);
        timeModel.setUnit(ChronoUnit.WEEKS);

        InPVactGroupBasedDeffuantUncertainty uncertainty = new InPVactGroupBasedDeffuantUncertainty();
        uncertainty.setName("UNCERT");
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(new InConsumerAgentGroup[]{BUM, G, PRA});

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA");
        processModel.setDefaultValues();
        processModel.setNodeFilterScheme(null);
        processModel.setPvFile(pvFile);
        processModel.setUncertainty(uncertainty);
        processModel.setSpeedOfConvergence(0.0);

        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.HAVERSINE_KM);

        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.logLevel = IRPLevel.ALL.getLevelId();
        general.logAllIRPact = true;
        general.enableAllDataLogging();
        general.enableAllResultLogging();
        general.setFirstSimulationYear(2015);
        general.lastSimulationYear = 2015;

        //images
        List<InOutputImage> images = new ArrayList<>();
        Collections.addAll(images, InGenericOutputImage.DEFAULTS);
        images.add(new InGnuPlotOutputImage("gnutest1", DataToVisualize.ANNUAL_ZIP));
        images.add(new InGnuPlotOutputImage("gnutest2", DataToVisualize.COMPARED_ANNUAL_ZIP));
        images.add(new InGnuPlotOutputImage("gnutest3", DataToVisualize.CUMULATIVE_ANNUAL_PHASE));
        images.add(new InROutputImage("rtest1", DataToVisualize.ANNUAL_ZIP));
        images.add(new InROutputImage("rtest2", DataToVisualize.COMPARED_ANNUAL_ZIP));
        images.add(new InROutputImage("rtest3", DataToVisualize.CUMULATIVE_ANNUAL_PHASE));
        images.forEach(InOutputImage::enableAll);

        //=====
        InRoot root = new InRoot();
        root.version = new InScenarioVersion[]{InScenarioVersion.currentVersion()};
        root.general = general;
        root.setAffinities(affinities);
        root.consumerAgentGroups = new InConsumerAgentGroup[]{BUM, G, PRA};
        root.setAgentPopulationSize(population);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};
        root.setImages(images);

        //=====
        GraphvizColor gc1 = GraphvizColor.RED;
        GraphvizColor gc2 = GraphvizColor.GREEN;
        GraphvizColor gc3 = new GraphvizColor("BLUE", Color.BLUE);
        root.colors = new GraphvizColor[]{gc1, gc2, gc3};
        root.setConsumerAgentGroupColors(new InConsumerAgentGroupColor[]{
                new InConsumerAgentGroupColor("BUM_color", BUM, gc1),
                new InConsumerAgentGroupColor("G_color", G, gc2),
                new InConsumerAgentGroupColor("PRA_color", PRA, gc3)
        });

        GraphvizLayoutAlgorithm.DOT.useLayout = false;
        GraphvizLayoutAlgorithm.FDP.useLayout = true;
        GraphvizLayoutAlgorithm.CIRCO.useLayout = false;
        root.layoutAlgorithms = GraphvizLayoutAlgorithm.DEFAULTS;
        GraphvizOutputFormat.PNG.useFormat = true;
        root.outputFormats = new GraphvizOutputFormat[] { GraphvizOutputFormat.PNG };

        root.graphvizGlobal = new GraphvizGlobal();
        root.graphvizGlobal.fixedNeatoPosition = false;
        root.graphvizGlobal.scaleFactor = 0.0;

        return Collections.singletonList(root);
    }
}
