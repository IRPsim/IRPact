package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.network.InDistanceEvaluator;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InNoDistance;
import de.unileipzig.irpact.io.param.input.network.InNumberOfTies;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.util.scenarios.AbstractScenario;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractPVactScenario extends AbstractScenario {

    protected final InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected final InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);

    protected Map<String, InAttributeName> nameCache = new HashMap<>();

    protected String spatialDataName;
    protected String pvDataName;

    protected int totalAgents = -1;

    public AbstractPVactScenario() {
        super();
    }

    public AbstractPVactScenario(String name, String creator, String description) {
        super(name, creator, description);
    }

    public void setTotalAgents(int totalAgents) {
        this.totalAgents = totalAgents;
    }

    public int getTotalAgents() {
        return totalAgents;
    }

    public String getPVDataName() {
        return pvDataName;
    }
    public void setPvDataName(String pvDataName) {
        this.pvDataName = pvDataName;
    }
    public AbstractPVactScenario withPvDataName(String pvDataName) {
        setPvDataName(pvDataName);
        return this;
    }

    public String getSpatialFileName() {
        return spatialDataName;
    }
    public void setSpatialDataName(String spatialDataName) {
        this.spatialDataName = spatialDataName;
    }
    public AbstractPVactScenario withSpatialDataName(String spatialDataName) {
        setSpatialDataName(spatialDataName);
        return this;
    }

    protected InPVFile pvFile;
    public InPVFile getPVFile() {
        if(pvFile == null) {
            pvFile = new InPVFile(getPVDataName());
        }
        return pvFile;
    }

    protected InSpatialTableFile spatialTableFile;
    public InSpatialTableFile getSpatialFile() {
        if(spatialTableFile == null) {
            spatialTableFile = new InSpatialTableFile(getSpatialFileName());
        }
        return spatialTableFile;
    }

    public InAttributeName getAttribute(String text) {
        return nameCache.computeIfAbsent(text, InAttributeName::new);
    }

    public InSpace2D createSpace2D(String name) {
        return new InSpace2D(name, Metric2D.HAVERSINE_KM);
    }

    public InGeneral createGeneralPart() {
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
        return general;
    }

    public InPVactGroupBasedDeffuantUncertainty createDefaultUnvertainty(String name, InConsumerAgentGroup... cags) {
        InPVactGroupBasedDeffuantUncertainty uncertainty = new InPVactGroupBasedDeffuantUncertainty();
        uncertainty.setName(name);
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(cags);
        return uncertainty;
    }

    public InRAProcessModel createDefaultProcessModel(String name, InUncertainty uncertainty, double speedOfConvergence) {
        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName(name);
        processModel.setDefaultValues();
        processModel.setNodeFilterScheme(null);
        processModel.setPvFile(getPVFile());
        processModel.setUncertainty(uncertainty);
        processModel.setSpeedOfConvergence(speedOfConvergence);
        return processModel;
    }

    public InUnitStepDiscreteTimeModel createOneWeekTimeModel(String name) {
        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel();
        timeModel.setName(name);
        timeModel.setAmountOfTime(1);
        timeModel.setUnit(ChronoUnit.WEEKS);
        return timeModel;
    }

    public InFileBasedPVactMilieuSupplier createSpatialDistribution(String name) {
        InFileBasedPVactMilieuSupplier supplier = new InFileBasedPVactMilieuSupplier();
        supplier.setName(name);
        supplier.setFile(getSpatialFile());
        return supplier;
    }

    public InFreeNetworkTopology createFreeTopology(
            String name,
            InAffinities affinities,
            InConsumerAgentGroup[] cags,
            int numberOfEdges) {
        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName(name);
        topology.setAffinities(affinities);
        topology.setNumberOfTies(new InNumberOfTies(name + "_ties", cags, numberOfEdges));
        topology.setDistanceEvaluator(new InNoDistance("NoDist"));
        topology.setInitialWeight(1);
        topology.setAllowLessEdges(false);
        return topology;
    }

    public InFileBasedPVactConsumerAgentPopulation createPopulation(String name, int agents, InConsumerAgentGroup... cags) {
        InFileBasedPVactConsumerAgentPopulation supplier = new InFileBasedPVactConsumerAgentPopulation();
        supplier.setName(name);
        supplier.setFile(getSpatialFile());
        supplier.setRequiresDesiredSize(true);
        supplier.setConsumerAgentGroups(cags);
        if(agents < 1) {
            supplier.setUseAll(true);
            supplier.setDesiredSize(-1);
        } else {
            supplier.setUseAll(false);
            supplier.setDesiredSize(agents);
        }
        return supplier;
    }

    public InComplexAffinityEntry createAffinityEntry(String prefix, InConsumerAgentGroup a, InConsumerAgentGroup b, double value) {
        return new InComplexAffinityEntry(prefix + "_" + a.getName() + "_" + b.getName(), a, b, value);
    }

    public InAffinities createAffinities(String name, InAffinityEntry... entries) {
        InAffinities affinities = new InAffinities();
        affinities.setName(name);
        affinities.setEntries(entries);
        return affinities;
    }

    public InAffinities createZeroAffinities(String name, InConsumerAgentGroup... cags) {
        InComplexAffinityEntry[] entries = InComplexAffinityEntry.buildAll(cags, 0);
        return createAffinities(name, entries);
    }

    public InAffinities createAffinities(String name, InAffinityEntry[]... entries) {
        InAffinityEntry[] flatEntries = Arrays.stream(entries)
                .flatMap(Arrays::stream)
                .toArray(InAffinityEntry[]::new);
        return createAffinities(name, flatEntries);
    }

    public InAffinityEntry[] createEntries(String prefix, InConsumerAgentGroup from, InConsumerAgentGroup[] targets, double[] values) {
        InAffinityEntry[] entries = new InAffinityEntry[targets.length];
        for(int i = 0; i < entries.length; i++) {
            InConsumerAgentGroup to = targets[i];
            InComplexAffinityEntry entry = new InComplexAffinityEntry();
            String name = prefix == null || prefix.isEmpty()
                    ? from.getName() + "_" + to.getName()
                    : prefix + "_" + from.getName() + "_" + to.getName();
            entry.setName(name);
            entry.setSrcCag(from);
            entry.setTarCag(to);
            entry.setAffinityValue(values[i]);
        }
        return entries;
    }

    public InPVactConsumerAgentGroup createNullAgent(String name) {
        return createNullAgent(name, null);
    }

    public InPVactConsumerAgentGroup createNullAgent(String name, InSpatialDistribution distribution) {
        InPVactConsumerAgentGroup grp = new InPVactConsumerAgentGroup();
        grp.setName(name);
        if(distribution != null) {
            grp.setSpatialDistribution(distribution);
        }

        //A1 in file
        grp.setNoveltySeeking(dirac0);                            //A2
        grp.setDependentJudgmentMaking(dirac0);                   //A3
        grp.setEnvironmentalConcern(dirac0);                      //A4
        //A5 in file
        //A6 in file
        grp.setConstructionRate(dirac0);                          //A7
        grp.setRenovationRate(dirac0);                            //A8

        grp.setRewire(dirac0);                                    //B6

        grp.setCommunication(dirac0);                             //C1
        grp.setRateOfConvergence(dirac0);                         //C3

        grp.setInitialProductAwareness(dirac0);                     //D1
        grp.setInterestThreshold(dirac0);                         //D2
        grp.setFinancialThreshold(dirac0);                       //D3
        grp.setAdoptionThreshold(dirac0);                        //D4
        grp.setInitialAdopter(dirac0);                            //D5
        grp.setInitialProductInterest(dirac0);                    //D6

        return grp;
    }
}
