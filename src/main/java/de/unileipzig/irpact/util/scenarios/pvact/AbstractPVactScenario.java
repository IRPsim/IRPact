package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.io.param.input.visualisation.network.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InNoDistance;
import de.unileipzig.irpact.io.param.input.network.InNumberOfTies;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.util.scenarios.AbstractScenario;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractPVactScenario extends AbstractScenario {

    protected final InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected final InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);

    protected Map<String, InAttributeName> nameCache = new HashMap<>();

    protected String spatialDataName;
    protected String realAdoptionDataName;
    protected String pvDataName;

    @Deprecated
    protected int totalAgents = -1;
    protected boolean runPvAct = true;

    public AbstractPVactScenario() {
        super();
    }

    public AbstractPVactScenario(String name, String creator, String description) {
        super(name, creator, description);
    }

    public void setRunPvAct(boolean runPvAct) {
        this.runPvAct = runPvAct;
    }

    public boolean isRunPvAct() {
        return runPvAct;
    }

    @Deprecated
    public void setTotalAgents(int totalAgents) {
        this.totalAgents = totalAgents;
    }

    @Deprecated
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

    public String getRealAdoptionDataName() {
        return realAdoptionDataName;
    }
    public void setRealAdoptionDataName(String realAdoptionDataName) {
        this.realAdoptionDataName = realAdoptionDataName;
    }
    public AbstractPVactScenario withRealAdoptionDataName(String realAdoptionDataName) {
        setRealAdoptionDataName(realAdoptionDataName);
        return this;
    }

    public AbstractPVactScenario withFiles(
            String pvDataName,
            String realAdoptionDataName,
            String spatialDataName) {
        return withPvDataName(pvDataName)
                .withRealAdoptionDataName(realAdoptionDataName)
                .withSpatialDataName(spatialDataName);
    }

    protected InPVFile pvFile;
    public InPVFile getPVFile() {
        if(pvFile == null) {
            pvFile = new InPVFile(getPVDataName());
        }
        return pvFile;
    }
    public InPVFile computePVFileIfAbsent() {
        if(isCached(getPVDataName())) {
            return getCached(getPVDataName());
        } else {
            InPVFile pvFile = getPVFile();
            cache(getPVDataName(), pvFile);
            return pvFile;
        }
    }

    protected InSpatialTableFile spatialTableFile;
    public InSpatialTableFile getSpatialFile() {
        if(spatialTableFile == null) {
            spatialTableFile = new InSpatialTableFile(getSpatialFileName());
        }
        return spatialTableFile;
    }
    public InSpatialTableFile computeSpatialFileIfAbsent() {
        if(isCached(getSpatialFileName())) {
            return getCached(getSpatialFileName());
        } else {
            InSpatialTableFile spatialTableFile = getSpatialFile();
            cache(getSpatialFileName(), spatialTableFile);
            return spatialTableFile;
        }
    }

    protected InRealAdoptionDataFile realAdoptionFile;
    public InRealAdoptionDataFile getRealAdoptionDataFile() {
        if(realAdoptionFile == null) {
            realAdoptionFile = new InRealAdoptionDataFile(getRealAdoptionDataName());
        }
        return realAdoptionFile;
    }
    public InRealAdoptionDataFile computeRealAdoptionDataFileIfAbsent() {
        if(isCached(getRealAdoptionDataName())) {
            return getCached(getRealAdoptionDataName());
        } else {
            InRealAdoptionDataFile realAdoptionFile = getRealAdoptionDataFile();
            cache(getRealAdoptionDataName(), realAdoptionFile);
            return realAdoptionFile;
        }
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
        general.setLogLevel(IRPLevel.INFO);
        general.logAllIRPact = true;
        general.enableAllDataLogging();
        general.enableAllResultLogging();
        general.setFirstSimulationYear(2015);
        general.lastSimulationYear = 2015;
        return general;
    }

    protected static final String DEFAULT_CONSUMER_INIT_ADOPTER = "DEFAULT_CONSUMER_INIT_ADOPTER";
    protected InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData getDefaultPVactFileBasedInitialAdopter() {
        if(isCached(DEFAULT_CONSUMER_INIT_ADOPTER)) {
            return getCached(DEFAULT_CONSUMER_INIT_ADOPTER);
        } else {
            InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData initAdopter = new InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData();
            initAdopter.setName(DEFAULT_CONSUMER_INIT_ADOPTER);
            initAdopter.setFile(getRealAdoptionDataFile());
            cache(initAdopter.getName(), initAdopter);
            return initAdopter;
        }
    }

    protected static final String DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER = "DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER";
    protected InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData getDefaultPVactFileBasedWeightedInitialAdopter() {
        if(isCached(DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER)) {
            return getCached(DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER);
        } else {
            InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData initAdopter = new InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData();
            initAdopter.setName(DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER);
            initAdopter.setFile(getRealAdoptionDataFile());
            cache(initAdopter.getName(), initAdopter);
            return initAdopter;
        }
    }

    @Override
    public void setupGeneral(InGeneral general) {
        super.setupGeneral(general);
        general.runPVAct = runPvAct;
        general.runOptActDemo = false;
    }

    @Override
    public void setupRoot(InRoot root) {
        super.setupRoot(root);
        List<InAttributeName> attributes = new ArrayList<>();
        for(String attr: RAConstants.DEFAULT_ATTRIBUTES) {
            attributes.add(getAttribute(attr));
        }
        root.setAttributeNames(attributes);
    }

    public static void setColors(InRoot root, InConsumerAgentGroup... cags) {
        InConsumerAgentGroupColor[] colors = InConsumerAgentGroupColor.copyColors();
        InConsumerAgentGroupColor.roundRobin(colors, cags);
        root.setConsumerAgentGroupColors(colors);
    }

    public InGenericOutputImage[] createDefaultImages() {
        InGenericOutputImage[] defaults = InGenericOutputImage.createDefaultImages(computeRealAdoptionDataFileIfAbsent());
        InGenericOutputImage.setEnableAll(true, defaults);
        InGenericOutputImage.setEngine(SupportedEngine.GNUPLOT, defaults);
        return defaults;
    }

    public InPVactGroupBasedDeffuantUncertainty createDefaultUnvertainty(String name, InConsumerAgentGroup... cags) {
        InPVactGroupBasedDeffuantUncertainty uncertainty = new InPVactGroupBasedDeffuantUncertainty();
        uncertainty.setName(name);
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(cags);
        return uncertainty;
    }

    public InPVactGlobalDeffuantUncertainty createGlobalUnvertainty(String name, InConsumerAgentGroup... cags) {
        InPVactGlobalDeffuantUncertainty uncertainty = new InPVactGlobalDeffuantUncertainty();
        uncertainty.setName(name);
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(cags);
        return uncertainty;
    }

    public InPVactGlobalDeffuantUncertainty createGlobalUnvertainty(String name, double extremParam, double extremUncert, double moderateUncert, InConsumerAgentGroup... cags) {
        InPVactGlobalDeffuantUncertainty uncertainty = new InPVactGlobalDeffuantUncertainty();
        uncertainty.setName(name);
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(cags);
        uncertainty.setExtremistParameter(extremParam);
        uncertainty.setExtremistUncertainty(extremUncert);
        uncertainty.setModerateUncertainty(moderateUncert);
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

    public InUnlinkedGraphTopology createUnlinkedTopology(String name) {
        return new InUnlinkedGraphTopology(name);
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

    public InFreeNetworkTopology createFreeTopology(
            String name,
            InAffinities affinities,
            Map<? extends InConsumerAgentGroup, Integer> edgeCount) {
        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName(name);
        topology.setAffinities(affinities);
        topology.setNumberOfTies(createTies(edgeCount));
        topology.setDistanceEvaluator(new InNoDistance("NoDist"));
        topology.setInitialWeight(1);
        topology.setAllowLessEdges(false);
        return topology;
    }

    public InNumberOfTies[] createTies(Map<? extends InConsumerAgentGroup, Integer> edgeCount) {
        InNumberOfTies[] ties = new InNumberOfTies[edgeCount.size()];
        int i = 0;
        for(Map.Entry<? extends InConsumerAgentGroup, Integer> entry: edgeCount.entrySet()) {
            InConsumerAgentGroup cag = entry.getKey();
            int count = entry.getValue();
            ties[i++] = new InNumberOfTies(cag.getName() + "_EDGES", cag, count);
        }
        return ties;
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

    public InFileBasedPVactConsumerAgentPopulation createFullPopulation(String name, InConsumerAgentGroup... cags) {
        return createPopulation(name, -1, cags);
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

    public InAffinities createEvenlyDistributedAffinities(String name, InConsumerAgentGroup... cags) {
        InComplexAffinityEntry[] entries = InComplexAffinityEntry.buildAll(cags, 1.0 / cags.length);
        return createAffinities(name, entries);
    }

    public InAffinities createSelfLinkedAffinities(String name, InConsumerAgentGroup... cags) {
        InComplexAffinityEntry[] entries = InComplexAffinityEntry.buildSelfLinked(cags);
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
            entries[i] = entry;
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
