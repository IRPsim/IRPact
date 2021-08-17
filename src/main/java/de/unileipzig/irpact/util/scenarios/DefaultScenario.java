package de.unileipzig.irpact.util.scenarios;

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
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;

import java.time.temporal.ChronoUnit;

/**
 * @author Daniel Abitz
 */
public class DefaultScenario extends AbstractScenario implements DefaultScenarioFactory {

    public static final int REVISION = 2;

    public DefaultScenario() {
        this(null, null, null);
    }

    public DefaultScenario(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    @Override
    protected void setup() {
        setSupportYears(2015, 1);
    }

    @Override
    protected InRoot createInRoot(int year, int delta) {
        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");
        InPVFile pvFile = new InPVFile("Barwertrechner");
        InUnivariateDoubleDistribution constant0 = new InDiracUnivariateDistribution("dirac0", 0);

        //spatial
        InFileBasedPVactMilieuSupplier spaDist = new InFileBasedPVactMilieuSupplier();
        spaDist.setName("SpatialDistribution");
        spaDist.setFile(tableFile);

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
        InPVactGroupBasedDeffuantUncertainty uncertainty = new InPVactGroupBasedDeffuantUncertainty();
        uncertainty.setName("Unvertainty");
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(cags);

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("ProcessModel");
        processModel.setDefaultValues();
        processModel.setNodeFilterScheme(new InRAProcessPlanMaxDistanceFilterScheme("MaxDistance", 100, true));
        processModel.setPvFile(pvFile);
        processModel.setUncertainty(uncertainty);
        processModel.setSpeedOfConvergence(0.0);

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
        general.runOptActDemo = false;
        general.runPVAct = true;

        return root;
    }

    @Override
    public InRoot createDefaultScenario() {
        return createInRoot(getInitialYear(), getInitialDelta());
    }
}
