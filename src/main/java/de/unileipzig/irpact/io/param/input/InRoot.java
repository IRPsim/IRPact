package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.graph.topology.GraphTopology;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.calc.ProductModule;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.io.param.*;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.*;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.io.param.input.process.modular.InModularProcessModel;
import de.unileipzig.irpact.io.param.input.process.modular.InModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.InConsumerAgentModularProcessModel;
import de.unileipzig.irpact.io.param.input.process.modular.ca.InSimpleConsumerAgentMPM;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentCalculationModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc.*;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval.*;
import de.unileipzig.irpact.io.param.input.process.mra.InModularRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.mra.component.*;
import de.unileipzig.irpact.io.param.input.product.initial.*;
import de.unileipzig.irpact.io.param.input.special.InSpecialPVactInput;
import de.unileipzig.irpact.io.param.input.visualisation.network.InGraphvizGeneral;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGnuPlotOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InROutputImage;
import de.unileipzig.irpact.io.param.input.interest.InProductGroupThresholdEntry;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.visualisation.network.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.*;
import de.unileipzig.irpact.io.param.irpopt.*;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.names.InName;
import de.unileipzig.irpact.io.param.input.process.InProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.*;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.process.ra.*;
import de.unileipzig.irpact.io.param.input.spatial.dist.*;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.distribution.*;
import de.unileipzig.irpact.io.param.input.network.*;
import de.unileipzig.irpact.io.param.input.product.*;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.output.OutInformation;
import de.unileipzig.irpact.io.param.output.agent.OutConsumerAgentGroup;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.start.optact.gvin.GvInRoot;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irpact.start.optact.network.IGraphTopology;
import de.unileipzig.irptools.defstructure.AnnotationResource;
import de.unileipzig.irptools.defstructure.DefinitionType;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.StandardLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.StandardOutputFormat;
import de.unileipzig.irptools.graphviz.def.*;
import de.unileipzig.irptools.uiedn.Section;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.UiEdn;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Path;
import java.util.*;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
@Definition(root = true)
public class InRoot implements RootClass {

    public static final String CONFIG_YEAR = "year";
    public static final String SET_VERSION = InScenarioVersion.deriveSetName();
    public static final String SET_BINARY_PERSIST_DATA = BinaryPersistData.deriveSetName();

    public static final InRoot INSTANCE = new InRoot();

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InRoot.class);

    //=========================
    //IRPopt
    //=========================

    @FieldDefinition
    public A[] a = new A[0];

    @FieldDefinition
    public ATotal[] aTotal = new ATotal[0];

    @FieldDefinition
    public Ii[] ii = new Ii[0];

    @FieldDefinition
    public Ii0[] ii0 = new Ii0[0];

    @FieldDefinition
    public Jj[] jj = new Jj[0];

    @FieldDefinition
    public OptInitial[] optInitial = new OptInitial[0];

    @FieldDefinition
    public OptSteps[] optSteps = new OptSteps[0];

    @FieldDefinition
    public OptStore[] optStore = new OptStore[0];

    @FieldDefinition
    public Side[] side = new Side[0];

    @FieldDefinition
    public SideCustom[] sideCustom = new SideCustom[0];

    @FieldDefinition
    public T[] t = new T[0];

    //=========================
    //test
    //=========================

    @FieldDefinition
    public InTestData[] testData = new InTestData[0];

    //=========================
    //general
    //=========================

    @FieldDefinition
    public InGeneral general = new InGeneral();

    public void setGeneral(InGeneral general) {
        this.general = general;
    }
    public void setGeneral(InGeneral[] general) throws ParsingException {
        this.general = getInstance(general, "general");
    }
    public InGeneral getGeneral() {
        return general;
    }

    @FieldDefinition
    public InScenarioVersion[] version = new InScenarioVersion[]{InScenarioVersion.currentVersion()};

    public void setVersion(InScenarioVersion version) {
        this.version = new InScenarioVersion[]{version};
    }
    public void setVersion(InScenarioVersion[] version) {
        this.version = version;
    }
    public boolean hasVersion() {
        return version != null && version.length > 0;
    }
    public InScenarioVersion getVersion() throws ParsingException {
        return getInstance(version, "version");
    }

    @FieldDefinition
    public InIRPactVersion[] aboutPlaceholders = new InIRPactVersion[0];

    @FieldDefinition
    public InInformation[] informations = new InInformation[0];

    public InInformation[] getInformations() {
        return informations;
    }
    public void setInformations(InInformation[] informations) {
        this.informations = informations;
    }
    public void addInformation(InInformation information) {
        informations = ParamUtil.add(informations, information);
    }
    public void addInformations(InInformation... newInformations) {
        informations = ParamUtil.addAll(informations, newInformations);
    }

    //=========================
    //affinity
    //=========================

    @FieldDefinition
    public InAffinities[] affinities = new InAffinities[0];

    public void setAffinities(InAffinities affinities) {
        this.affinities = new InAffinities[]{affinities};
    }
    public InAffinities getAffinities() throws ParsingException {
        return getInstance(affinities, "affinities");
    }

    //=========================
    //agent
    //=========================

    @FieldDefinition
    public InConsumerAgentGroup[] consumerAgentGroups = new InConsumerAgentGroup[0];

    public void setConsumerAgentGroups(InConsumerAgentGroup[] consumerAgentGroups) {
        this.consumerAgentGroups = consumerAgentGroups;
    }
    public InConsumerAgentGroup[] getConsumerAgentGroups() throws ParsingException {
        return getNonNullArray(consumerAgentGroups, "consumerAgentGroups");
    }
    public boolean addConsumerAgentGroup(InConsumerAgentGroup consumerAgentGroup) {
        if(consumerAgentGroups == null) {
            consumerAgentGroups = new InConsumerAgentGroup[]{consumerAgentGroup};
            return true;
        }
        else if(!CollectionUtil.containsSame(consumerAgentGroups, consumerAgentGroup)) {
            consumerAgentGroups = Arrays.copyOf(consumerAgentGroups, consumerAgentGroups.length + 1);
            consumerAgentGroups[consumerAgentGroups.length - 1] = consumerAgentGroup;
            return true;
        }
        return false;
    }

    @FieldDefinition
    public InIndependentConsumerAgentGroupAttribute[] independentConsumerAgentGroupAttributes = new InIndependentConsumerAgentGroupAttribute[0];

    public void setIndependentConsumerAgentGroupAttributes(InIndependentConsumerAgentGroupAttribute[] independentConsumerAgentGroupAttributes) {
        this.independentConsumerAgentGroupAttributes = independentConsumerAgentGroupAttributes;
    }
    public InIndependentConsumerAgentGroupAttribute[] getIndependentConsumerAgentGroupAttributes() throws ParsingException {
        return getNonNullArray(independentConsumerAgentGroupAttributes, "independentConsumerAgentGroupAttributes");
    }

    @FieldDefinition
    public InAgentPopulation[] agentPopulationSizes = new InAgentPopulation[0];

    public void setAgentPopulationSize(InAgentPopulation agentPopulationSize) {
        this.agentPopulationSizes = new InAgentPopulation[]{agentPopulationSize};
    }
    public void setAgentPopulationSizes(InAgentPopulation[] agentPopulationSizes) {
        this.agentPopulationSizes = agentPopulationSizes;
    }
    public InAgentPopulation[] getAgentPopulationSizes() throws ParsingException {
        return getNonNullArray(agentPopulationSizes, "agentPopulationSizes");
    }

    //=========================
    //image
    //=========================

    @FieldDefinition
    public InOutputImage[] images = new InOutputImage[0];

    public boolean hasImages() {
        return images != null && images.length > 0;
    }
    public InOutputImage[] getImages() throws ParsingException {
        return getNonNullArray(images, "images");
    }
    public void setImages(InOutputImage[] images) {
        this.images = images;
    }
    public void setImages(Collection<? extends InOutputImage> images) {
        this.images = images.toArray(new InOutputImage[0]);
    }

    //=========================
    //binary
    //=========================

    @FieldDefinition
    public VisibleBinaryData[] visibleBinaryData = new VisibleBinaryData[0];

    public void setVisibleBinaryData(VisibleBinaryData[] visibleBinaryData) {
        this.visibleBinaryData = visibleBinaryData;
    }
    public VisibleBinaryData[] getVisibleBinaryData() throws ParsingException {
        return getNonEmptyArray(visibleBinaryData, "visibleBinaryData");
    }

    //fuer out->in transfer
    @FieldDefinition
    public BinaryPersistData[] binaryPersistData = new BinaryPersistData[0];

    //=========================
    //network
    //=========================

    @FieldDefinition
    public InGraphTopologyScheme[] graphTopologySchemes = new InGraphTopologyScheme[0];

    public void setGraphTopologyScheme(InGraphTopologyScheme[] graphTopologySchemes) throws ParsingException {
        this.graphTopologySchemes = getOneElementArray(graphTopologySchemes, "graphTopologySchemes");
    }
    public void setGraphTopologyScheme(InGraphTopologyScheme graphTopologyScheme) {
        this.graphTopologySchemes = new InGraphTopologyScheme[]{graphTopologyScheme};
    }
    public InGraphTopologyScheme getGraphTopologyScheme() throws ParsingException {
        return getInstance(graphTopologySchemes, "graphTopologySchemes");
    }

    //=========================
    //process
    //=========================

    @FieldDefinition
    public InProcessModel[] processModels = new InProcessModel[0];

    public void setProcessModel(InProcessModel processModel) {
        this.processModels = new InProcessModel[]{processModel};
    }
    public void setProcessModels(InProcessModel[] processModels) throws ParsingException {
        this.processModels = getNonEmptyArray(processModels, "processModel");
    }
    public InProcessModel[] getProcessModels() throws ParsingException {
        return getNonEmptyArray(processModels, "processModel");
    }

    //=========================
    //product
    //=========================

    @FieldDefinition
    public InProductGroup[] productGroups = new InProductGroup[0];
    public void setProductGroups(InProductGroup[] productGroups) {
        this.productGroups = productGroups;
    }
    public InProductGroup[] getProductGroups() throws ParsingException {
        return getNonNullArray(productGroups, "productGroups");
    }

    @FieldDefinition
    public InIndependentProductGroupAttribute[] independentProductGroupAttributes = new InIndependentProductGroupAttribute[0];
    public void setIndependentProductGroupAttributes(InIndependentProductGroupAttribute[] independentProductGroupAttributes) {
        this.independentProductGroupAttributes = independentProductGroupAttributes;
    }
    public InIndependentProductGroupAttribute[] getIndependentProductGroupAttributes() throws ParsingException {
        return getNonNullArray(independentProductGroupAttributes, "independentProductGroupAttributes");
    }

    @FieldDefinition
    public InFixProduct[] fixProducts = new InFixProduct[0];
    public void setFixProducts(InFixProduct[] fixProducts) {
        this.fixProducts = fixProducts;
    }
    public InFixProduct[] getFixProducts() throws ParsingException {
        return getNonNullArray(fixProducts, "fixProducts");
    }

    @FieldDefinition
    public InInitialAdoptionHandler[] initialAdoptionHandlers = new InInitialAdoptionHandler[0];
    public void setInitialAdoptionHandlers(InInitialAdoptionHandler[] initialAdoptionHandlers) {
        this.initialAdoptionHandlers = initialAdoptionHandlers;
    }
    public InInitialAdoptionHandler[] getInitialAdoptionHandlers() {
        return initialAdoptionHandlers;
    }
    public boolean hasInInitialAdoptionHandler() {
        return initialAdoptionHandlers != null && initialAdoptionHandlers.length > 0;
    }

    //=========================
    //spatial
    //=========================

    @FieldDefinition
    public InSpatialModel[] spatialModel = new InSpatialModel[0];

    public void setSpatialModel(InSpatialModel[] spatialModel) throws ParsingException {
        this.spatialModel = getOneElementArray(spatialModel, "spatialModel");
    }
    public void setSpatialModel(InSpatialModel spatialModel) {
        this.spatialModel = new InSpatialModel[]{spatialModel};
    }
    public InSpatialModel getSpatialModel() throws ParsingException {
        return getInstance(spatialModel, "spatialModel");
    }

    @FieldDefinition
    public InSpatialDistribution[] spatialDistributions = new InSpatialDistribution[0];

    //=========================
    //time
    //=========================

    @FieldDefinition
    public InTimeModel[] timeModel = new InTimeModel[0];

    public void setTimeModel(InTimeModel[] timeModel) throws ParsingException {
        this.timeModel = getOneElementArray(timeModel, "timeModel");
    }
    public void setTimeModel(InTimeModel timeModel) {
        this.timeModel = new InTimeModel[]{timeModel};
    }
    public InTimeModel getTimeModel() throws ParsingException {
        return getInstance(timeModel, "timeModel");
    }
    //=========================
    //Graphviz
    //=========================

    @FieldDefinition
    public InGraphvizGeneral graphvizGeneral = new InGraphvizGeneral();
    public void setGraphvizGeneral(InGraphvizGeneral graphvizGeneral) {
        this.graphvizGeneral = graphvizGeneral;
    }
    public InGraphvizGeneral getGraphvizGeneral() {
        return graphvizGeneral;
    }

    //wird nicht mehr unterstuetzt
    @Deprecated
    public GraphvizColor[] colors = new GraphvizColor[0];
    public GraphvizColor[] getColors() {
        return colors;
    }
    public void setColors(GraphvizColor[] colors) {
        this.colors = colors;
    }

    @FieldDefinition
    public InConsumerAgentGroupColor[] consumerAgentGroupColors = new InConsumerAgentGroupColor[0];
    public void setConsumerAgentGroupColors(InConsumerAgentGroupColor[] consumerAgentGroupColors) {
        this.consumerAgentGroupColors = consumerAgentGroupColors;
    }
    public InConsumerAgentGroupColor[] getConsumerAgentGroupColors() throws ParsingException {
        return ParamUtil.getNonNullArray(consumerAgentGroupColors, "consumerAgentGroupColors");
    }

    //=========================
    //special
    //=========================

    @FieldDefinition
    public InSpecialPVactInput specialPVactInput = new InSpecialPVactInput();
    public void setSpecialPVactInput(InSpecialPVactInput specialPVactInput) {
        this.specialPVactInput = specialPVactInput;
    }
    public InSpecialPVactInput getSpecialPVactInput() {
        return specialPVactInput;
    }

    //=========================
    //OPTACT
    //=========================

    @FieldDefinition
    public AgentGroup[] agentGroups = new AgentGroup[0];
    public GraphvizColor getColor(String agentName) {
        for(AgentGroup grp: agentGroups) {
            if(agentName.startsWith(grp._name)) {
                return grp.agentColor;
            }
        }
        return null;
    }

    @FieldDefinition
    public IGraphTopology[] topologies = new IGraphTopology[0];
    public <N, E> GraphTopology<N, E> getTopology() {
        for(IGraphTopology topology: topologies) {
            if(topology.use()) {
                return topology.createInstance();
            }
        }
        return null;
    }

    @FieldDefinition()
    public InGlobal global = new InGlobal();

    @FieldDefinition
    public Sector[] sectors = new Sector[0];

    @FieldDefinition
    public SideCustom[] customs = new SideCustom[0];

    @FieldDefinition
    public SideFares[] fares = new SideFares[0];

    @FieldDefinition
    public LoadDSE[] dse = new LoadDSE[0];

    @FieldDefinition
    public TechDESES[] deses = new TechDESES[0];

    @FieldDefinition
    public TechDESPV[] despv = new TechDESPV[0];

    public OutputFormat getOutputFormat() {
        LOGGER.warn("USES DEFAULT");
        return StandardOutputFormat.PNG;
    }

    public LayoutAlgorithm getLayoutAlgorithm() {
        LOGGER.warn("USES DEFAULT");
        return StandardLayoutAlgorithm.FDP;
    }

    //==================================================

    public InRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return INPUT_WITH_GRAPHVIZ;
    }

    @Override
    public AnnotationResource getResources() {
        return new IOResources();
    }

    @Override
    public AnnotationResource getResource(Path pathToFile) {
        return new IOResources(pathToFile);
    }

    @Override
    public AnnotationResource getResource(Locale locale) {
        return new IOResources(locale);
    }

    @Override
    public void peekEdn(Sections sections, UiEdn ednType) {
        if(ednType == UiEdn.INPUT) {
            Section imageSection = new Section();
            imageSection.setPriority(-1);
            imageSection.setLabel("Agentennetzwerk");
            imageSection.setImage(IRPact.IMAGE_AGENTGRAPH_INPUT);
            imageSection.setDescription("Agentennetzwerk in IRPact");
            imageSection.setIcon("fa fa-spinner");
            sections.add(imageSection);
        }
    }

    public InRoot copy() {
        SimpleCopyCache cache = new SimpleCopyCache();
        InRoot copy = new InRoot();
        //general
        copy.general = cache.copy(general);
        copy.version = cache.copyArray(version);
        copy.informations = cache.copyArray(informations);
        //affinity
        copy.affinities = cache.copyArray(affinities);
        //agent
        copy.consumerAgentGroups = cache.copyArray(consumerAgentGroups);
        copy.independentConsumerAgentGroupAttributes = cache.copyArray(independentConsumerAgentGroupAttributes);
        copy.agentPopulationSizes = cache.copyArray(agentPopulationSizes);
        //binary
        copy.visibleBinaryData = cache.copyArray(visibleBinaryData);
        copy.binaryPersistData = cache.copyArray(binaryPersistData);
        //network
        copy.graphTopologySchemes = cache.copyArray(graphTopologySchemes);
        //process
        copy.processModels = cache.copyArray(processModels);
        //product
        copy.productGroups = cache.copyArray(productGroups);
        copy.independentProductGroupAttributes = cache.copyArray(independentProductGroupAttributes);
        copy.fixProducts = cache.copyArray(fixProducts);
        //spatial
        copy.spatialModel = cache.copyArray(spatialModel);
        //time
        copy.timeModel = cache.copyArray(timeModel);
        //Graphviz
        copy.graphvizGeneral = cache.copy(graphvizGeneral);
        copy.colors = cache.copyArray(colors);
        copy.consumerAgentGroupColors = cache.copyArray(consumerAgentGroupColors);
        //optact
        copy.agentGroups = agentGroups;
        copy.topologies = topologies;
        copy.global = global;
        copy.sectors = sectors;
        copy.customs = customs;
        copy.fares = fares;
        copy.dse = dse;
        copy.deses = deses;
        copy.despv = despv;
        //test
        copy.testData = cache.copyArray(testData);

        return copy;
    }

    //=========================
    //IRPACT
    //=========================

    public InConsumerAgentGroup findConsumerAgentGroup(InConsumerAgentGroupAttribute attribute) throws ParsingException {
        if(consumerAgentGroups == null) {
            throw new ParsingException("no cags");
        }

        if(attribute instanceof InDependentConsumerAgentGroupAttribute) {
            InDependentConsumerAgentGroupAttribute inDepAttr = (InDependentConsumerAgentGroupAttribute) attribute;
            for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
                if(inCag instanceof InGeneralConsumerAgentGroup) {
                    InGeneralConsumerAgentGroup inGenCag = (InGeneralConsumerAgentGroup) inCag;
                    if(inGenCag.hasAttribute(inDepAttr)) {
                        return inGenCag;
                    }
                }
            }
            throw new ParsingException("attribute '" + attribute.getName() + "' has no group");
        }
        else if(attribute instanceof InIndependentConsumerAgentGroupAttribute) {
            InIndependentConsumerAgentGroupAttribute inIndepAttr = (InIndependentConsumerAgentGroupAttribute) attribute;
            String cagName = inIndepAttr.getConsumerAgentGroupName();
            return findConsumerAgentGroup(cagName);
        }
        else {
            throw new ParsingException("unsupported attribute");
        }
    }

    public InConsumerAgentGroup findConsumerAgentGroup(String name) throws ParsingException {
        if(consumerAgentGroups == null) {
            throw new ParsingException("missing cag '" + name + "'");
        }
        for(InConsumerAgentGroup cag: consumerAgentGroups) {
            if(Objects.equals(cag.getName(), name)) {
                return cag;
            }
        }
        throw new ParsingException("missing cag '" + name + "'");
    }

    public InConsumerAgentGroup getConsumerAgentGroup(String name) {
        return ParamUtil.getEntityByName(consumerAgentGroups, name);
    }

    public InProductGroup getProductGroup(String name) {
        return ParamUtil.getEntityByName(productGroups, name);
    }

    public boolean hasBinaryPersistData() {
        return binaryPersistData != null && binaryPersistData.length > 0;
    }

    public InProcessModel getProcessModel(String name) {
        return ParamUtil.getEntityByName(processModels, name);
    }

    public InSpatialDistribution getSpatialDistribution(String name) throws ParsingException {
        return getSpatialDistribution(InSpatialDistribution.class, name);
    }

    public <R extends InSpatialDistribution> R getSpatialDistribution(Class<R> clazz, String name) throws ParsingException {
        if(spatialDistributions == null || spatialDistributions.length == 0) {
            return null;
        }
        for(InSpatialDistribution dist: spatialDistributions) {
            if(Objects.equals(dist.getName(), name) && clazz.isInstance(dist)) {
                return clazz.cast(dist);
            }
        }

        if(consumerAgentGroups == null || consumerAgentGroups.length == 0) {
            return null;
        }
        for(InConsumerAgentGroup cag: consumerAgentGroups) {
            InSpatialDistribution dist = cag.getSpatialDistribution();
            if(Objects.equals(dist.getName(), name) && clazz.isInstance(dist)) {
                return clazz.cast(dist);
            }
        }

        return null;
    }

    //=========================
    //CLASSES
    //=========================

    public static final List<ParserInput> INPUT_WITHOUT_TEMPLATES = ParserInput.listOf(DefinitionType.INPUT,
            InAffinities.class,
            InAffinityEntry.class,
            InComplexAffinityEntry.class,
            InNameSplitAffinityEntry.class,

            InConsumerAgentGroup.class,
            InConsumerAgentGroupAttribute.class,
            InDependentConsumerAgentGroupAttribute.class,
            InGeneralConsumerAgentAnnualGroupAttribute.class,
            InGeneralConsumerAgentGroup.class,
            InGeneralConsumerAgentGroupAttribute.class,
            InIndependentConsumerAgentGroupAttribute.class,
            InNameSplitConsumerAgentAnnualGroupAttribute.class,
            InNameSplitConsumerAgentGroupAttribute.class,
            InPVactConsumerAgentGroup.class,
            InFixConsumerAgentPopulation.class,
            InAgentPopulation.class,
            InFileBasedConsumerAgentPopulation.class,
            InFileBasedPVactConsumerAgentPopulation.class,

            VisibleBinaryData.class,
            BinaryPersistData.class, //special

            InBernoulliDistribution.class,
            InBooleanDistribution.class,
            InBoundedNormalDistribution.class,
            InBoundedUniformDoubleDistribution.class,
            InBoundedUniformIntegerDistribution.class,
            InDiracUnivariateDistribution.class,
            InFiniteMassPointsDiscreteDistribution.class,
            InMassPoint.class,
            InNormalDistribution.class,
            InSlowTruncatedNormalDistribution.class,
            InTruncatedNormalDistribution.class,
            InUnivariateDoubleDistribution.class,

            InFile.class,
            InPVFile.class,
            InRealAdoptionDataFile.class,
            InSpatialTableFile.class,

            InGenericOutputImage.class,
            InGnuPlotOutputImage.class,
            InOutputImage.class,
            InROutputImage.class,

            InProductGroupThresholdEntry.class,
            InProductInterestSupplyScheme.class,
            InProductThresholdInterestSupplyScheme.class,

            InAttributeName.class,
            InName.class,

            InCompleteGraphTopology.class,
            InDistanceEvaluator.class,
            InFreeNetworkTopology.class,
            InGraphTopologyScheme.class,
            InInverse.class,
            InNoDistance.class,
            InNumberOfTies.class,
            InUnlinkedGraphTopology.class,

            InGlobalDeffuantUncertainty.class,
            InGroupBasedDeffuantUncertainty.class,
            InPVactGlobalDeffuantUncertainty.class,
            InPVactGroupBasedDeffuantUncertainty.class,
            InUncertainty.class,

            InDisabledProcessPlanNodeFilterScheme.class,
            InEntireNetworkNodeFilterScheme.class,
            InRAProcessModel.class,
            InRAProcessPlanMaxDistanceFilterScheme.class,
            InRAProcessPlanNodeFilterScheme.class,
            InProcessModel.class,
            InProcessPlanNodeFilterScheme.class,

            InComponent.class,
            InDefaultDoActionComponent.class,
            InDefaultHandleDecisionMakingComponent.class,
            InDefaultHandleFeasibilityComponent.class,
            InDefaultHandleInterestComponent.class,
            InDoNothingComponent.class,
            InEvaluableComponent.class,
            InSumAttributeComponent.class,
            InSumIntermediateComponent.class,
            InSumThresholdComponent.class,
            InValueComponent.class,
            InModularRAProcessModel.class,

            //MPM v2
            InAddModule_calcgraphnode.class,
            InAttributeInputModule_inputgraphnode.class,
            InDisaggregatedFinancialModule_inputgraphnode.class,
            InDisaggregatedNPVModule_inputgraphnode.class,
            InEnvironmentalConcernModule_inputgraphnode.class,
            InFinancialComponentModule_inputgraphnode.class,
            InLogisticModule_calcgraphnode.class,
            InNoveltySeekingModule_inputgraphnode.class,
            InNPVModule_inputgraphnode.class,
            InProductModule_calcgraphnode.class,
            InShareOfAdopterInLocalNetworkModule_inputgraphnode.class,
            InShareOfAdopterInSocialNetworkModule_inputgraphnode.class,
            InSocialComponentModule_inputgraphnode.class,
            InSumModule_calcgraphnode.class,
            InWeightedAddModule_calcgraphnode.class,
            InWeightedModule_calcgraphnode.class,

            InDefaultActionModule_evalgraphnode.class,
            InDefaultDecisionMakingModule_evalgraphnode.class,
            InDefaultFeasibilityModule_evalgraphnode.class,
            InDefaultInterestModule_evalgraphnode.class,
            InDoNothingModule_evalgraphnode.class,
            InStageEvaluationModule_evalgraphnode.class,
            InSumThresholdEvaluationModule_evalgraphnode.class,
            InThresholdEvaluationModule_evalgraphnode.class,

            InConsumerAgentCalculationModule.class,
            InConsumerAgentEvaluationModule.class,
            InConsumerAgentModule.class,

            InConsumerAgentModularProcessModel.class,
            InSimpleConsumerAgentMPM.class,

            InModularProcessModel.class,
            InModule.class,
            //===

            //special
            InSpecialPVactInput.class,
            //===

            InBasicProductGroup.class,
            InBasicProductGroupAttribute.class,
            InDependentProductGroupAttribute.class,
            InFixProduct.class,
            InFixProductAttribute.class,
            InFixProductFindingScheme.class,
            InIndependentProductGroupAttribute.class,
            InNameSplitProductGroupAttribute.class,
            InProductFindingScheme.class,
            InProductGroup.class,
            InProductGroupAttribute.class,

            InInitialAdoptionHandler.class,
            InPVactAttributeBasedInitialAdoption.class,
            InPVactConsumerGroupBasedInitialAdoption.class,
            InPVactConsumerGroupBasedInitialAdoptionEntry.class,
            InPVactFileBasedConsumerGroupBasedInitialAdoption.class,

            InFileBasedPVactMilieuSupplier.class,
            InFileBasedPVactMilieuZipSupplier.class,
            InFileBasedSelectGroupSpatialInformationSupplier.class,
            InFileBasedSelectSpatialInformationSupplier.class,
            InFileBasedSpatialInformationSupplier.class,
            InSpatialDistribution.class,
            InSpatialDistributionWithCollection.class,
            InSpace2D.class,
            InSpatialModel.class,

            InDiscreteTimeModel.class,
            InTimeModel.class,
            InUnitStepDiscreteTimeModel.class,

            InGraphvizGeneral.class,
            InConsumerAgentGroupColor.class,

            InGeneral.class,
            InInformation.class,
            InIRPactVersion.class,
            InScenarioVersion.class,
            InTestData.class
    );

    public static final List<ParserInput> IRPOPT = ParserInput.listOf(DefinitionType.INPUT,
            A.class,
            ATotal.class,
            Ii.class,
            Ii0.class,
            Jj.class,
            OptInitial.class,
            OptSteps.class,
            OptStore.class,
            Side.class,
            SideCustom.class,
            T.class
    );

    public static final List<ParserInput> INPUT_WITH_TEMPLATES = ParserInput.merge(
            INPUT_WITHOUT_TEMPLATES,
            IRPOPT,
            ParserInput.listOf(DefinitionType.INPUT,
                    OutInformation.class,
                    OutConsumerAgentGroup.class
            )
    );

    public static final List<ParserInput> INPUT_WITH_ROOT = ParserInput.merge(
            INPUT_WITHOUT_TEMPLATES,
            IRPOPT,
            ParserInput.listOf(DefinitionType.INPUT,
                    InRoot.class
            )
    );

    public static final List<ParserInput> INPUT_WITH_TEMPLATE_AND_ROOT = ParserInput.merge(
            INPUT_WITH_TEMPLATES,
            ParserInput.listOf(DefinitionType.INPUT,
                    InRoot.class
            )
    );

    public static final List<ParserInput> INPUT_WITHOUT_GRAPHVIZ = ParserInput.merge(
            INPUT_WITH_TEMPLATE_AND_ROOT,
            GvInRoot.CLASSES_WITHOUT_ROOT_AND_GRAPHVIZ
    );

    @Todo("GraphvizColor irgendwann mal rauswerfen")
    public static final List<ParserInput> INPUT_WITH_GRAPHVIZ = ParserInput.merge(
            INPUT_WITHOUT_GRAPHVIZ,
            ParserInput.listOf(DefinitionType.INPUT,
                    GraphvizColor.class
            )
    );

    //=========================
    //UI
    //=========================

    public static void initRes(TreeAnnotationResource res) {
        InRootUI.initRes(res);
        //TODO remove
//        IOResources.Data userData = res.getUserDataAs();
//        MultiCounter counter = userData.getCounter();
//
//        addPathElement(res, INFORMATIONS, ROOT);
//                addPathElement(res, ABOUT_IRPACT, INFORMATIONS);
//                addPathElement(res, InScenarioVersion.thisName(), INFORMATIONS);
//                addPathElement(res, InInformation.thisName(), INFORMATIONS);
//
//        addPathElement(res, GENERAL_SETTINGS, ROOT);
//                addPathElement(res, LOGGING, GENERAL_SETTINGS);
//                        addPathElement(res, LOGGING_GENERAL, LOGGING);
//                        addPathElement(res, LOGGING_DATA, LOGGING);
//                        addPathElement(res, LOGGING_RESULT, LOGGING);
//                        addPathElement(res, LOGGING_SCRIPT, LOGGING);
//                addPathElement(res, IMAGE, GENERAL_SETTINGS);
//                        addPathElement(res, InGenericOutputImage.thisName(), IMAGE);
//                        addPathElement(res, InGnuPlotOutputImage.thisName(), IMAGE);
//                        addPathElement(res, InROutputImage.thisName(), IMAGE);
//                addPathElement(res, SPECIAL_SETTINGS, GENERAL_SETTINGS);
//                        addPathElement(res, VisibleBinaryData.thisName(), SPECIAL_SETTINGS);
//
//        addPathElement(res, InAttributeName.thisName(), ROOT);
//
//        addPathElement(res, FILES, ROOT);
//            addPathElement(res, InPVFile.thisName(), FILES);
//            addPathElement(res, InSpatialTableFile.thisName(), FILES);
//
//        addPathElement(res, DISTRIBUTIONS, ROOT);
//            addPathElement(res, InBernoulliDistribution.thisName(), DISTRIBUTIONS);
//            addPathElement(res, InBooleanDistribution.thisName(), DISTRIBUTIONS);
//            addPathElement(res, InDiracUnivariateDistribution.thisName(), DISTRIBUTIONS);
//            addPathElement(res, InFiniteMassPointsDiscreteDistribution.thisName(), DISTRIBUTIONS);
//                addPathElement(res, InMassPoint.thisName(), InFiniteMassPointsDiscreteDistribution.thisName());
//            addPathElement(res, InNormalDistribution.thisName(), DISTRIBUTIONS);
//            addPathElement(res, InBoundedNormalDistribution.thisName(), DISTRIBUTIONS);
//            addPathElement(res, InSlowTruncatedNormalDistribution.thisName(), DISTRIBUTIONS);
//            addPathElement(res, InTruncatedNormalDistribution.thisName(), DISTRIBUTIONS);
//            addPathElement(res, InBoundedUniformDoubleDistribution.thisName(), DISTRIBUTIONS);
//            addPathElement(res, InBoundedUniformIntegerDistribution.thisName(), DISTRIBUTIONS);
//
//        addPathElement(res, AGENTS, ROOT);
//                addPathElement(res, CONSUMER, AGENTS);
//                        addPathElement(res, CONSUMER_GROUP, CONSUMER);
//                                addPathElement(res, InGeneralConsumerAgentGroup.thisName(), CONSUMER_GROUP);
//                                addPathElement(res, InPVactConsumerAgentGroup.thisName(), CONSUMER_GROUP);
//                        addPathElement(res, CONSUMER_ATTR, CONSUMER);
//                                addPathElement(res, InGeneralConsumerAgentGroupAttribute.thisName(), CONSUMER_GROUP);
//                                addPathElement(res, InGeneralConsumerAgentAnnualGroupAttribute.thisName(), CONSUMER_GROUP);
//                                addPathElement(res, InNameSplitConsumerAgentGroupAttribute.thisName(), CONSUMER_GROUP);
//                                addPathElement(res, InNameSplitConsumerAgentAnnualGroupAttribute.thisName(), CONSUMER_GROUP);
//                        addPathElement(res, CONSUMER_AFFINITY, CONSUMER);
//                                addPathElement(res, InAffinities.thisName(), CONSUMER_AFFINITY);
//                                addPathElement(res, InComplexAffinityEntry.thisName(), CONSUMER_AFFINITY);
//                                addPathElement(res, InNameSplitAffinityEntry.thisName(), CONSUMER_AFFINITY);
//                        addPathElement(res, CONSUMER_INTEREST, CONSUMER);
//                                addPathElement(res, InProductThresholdInterestSupplyScheme.thisName(), CONSUMER_INTEREST);
//                                        addPathElement(res, InProductGroupThresholdEntry.thisName(), InProductThresholdInterestSupplyScheme.thisName());
//                addPathElement(res, POPULATION, AGENTS);
//                        addPathElement(res, InFixConsumerAgentPopulation.thisName(), POPULATION);
//                        addPathElement(res, InFileBasedConsumerAgentPopulation.thisName(), POPULATION);
//                        addPathElement(res, InFileBasedPVactConsumerAgentPopulation.thisName(), POPULATION);
//
//        addPathElement(res, NETWORK, ROOT);
//                addPathElement(res, TOPOLOGY, NETWORK);
//                        addPathElement(res, InUnlinkedGraphTopology.thisName(), TOPOLOGY);
//                        addPathElement(res, InCompleteGraphTopology.thisName(), TOPOLOGY);
//                        addPathElement(res, InFreeNetworkTopology.thisName(), TOPOLOGY);
//                                addPathElement(res, InNumberOfTies.thisName(), InFreeNetworkTopology.thisName());
//                addPathElement(res, DIST_FUNC, NETWORK);
//                        addPathElement(res, InNoDistance.thisName(), DIST_FUNC);
//                        addPathElement(res, InInverse.thisName(), DIST_FUNC);
//
//        addPathElement(res, PRODUCTS, ROOT);
//                addPathElement(res, PRODUCTS_GROUP, PRODUCTS);
//                        addPathElement(res, InBasicProductGroup.thisName(), PRODUCTS_GROUP);
//                addPathElement(res, PRODUCTS_ATTR, PRODUCTS);
//                        addPathElement(res, InBasicProductGroupAttribute.thisName(), PRODUCTS_ATTR);
//                        addPathElement(res, InNameSplitProductGroupAttribute.thisName(), PRODUCTS_ATTR);
//                addPathElement(res, InFixProduct.thisName(), PRODUCTS);
//                addPathElement(res, InFixProductAttribute.thisName(), PRODUCTS);
//                addPathElement(res, PRODUCTS_FINDING_SCHEME, PRODUCTS);
//                        addPathElement(res, InFixProductFindingScheme.thisName(), PRODUCTS_FINDING_SCHEME);
//
//        addPathElement(res, PROCESS_MODEL, ROOT);
//                addPathElement(res, InRAProcessModel.thisName(), PROCESS_MODEL);
//                        addPathElement(res, PROCESS_MODEL_RA_UNCERT, InRAProcessModel.thisName());
//                                addPathElement(res, InGlobalDeffuantUncertainty.thisName(), PROCESS_MODEL_RA_UNCERT);
//                                addPathElement(res, InPVactGlobalDeffuantUncertainty.thisName(), PROCESS_MODEL_RA_UNCERT);
//                                addPathElement(res, InGroupBasedDeffuantUncertainty.thisName(), PROCESS_MODEL_RA_UNCERT);
//                                addPathElement(res, InPVactGroupBasedDeffuantUncertainty.thisName(), PROCESS_MODEL_RA_UNCERT);
//                addPathElement(res, PROCESS_FILTER, PROCESS_MODEL);
//                        addPathElement(res, InDisabledProcessPlanNodeFilterScheme.thisName(), PROCESS_FILTER);
//                        addPathElement(res, InEntireNetworkNodeFilterScheme.thisName(), PROCESS_FILTER);
//                        addPathElement(res, InRAProcessPlanMaxDistanceFilterScheme.thisName(), PROCESS_FILTER);
//
//        addPathElement(res, SPATIAL, ROOT);
//                addPathElement(res, SPATIAL_MODEL, SPATIAL);
//                        addPathElement(res, InSpace2D.thisName(), SPATIAL_MODEL);
//                addPathElement(res, SPATIAL_MODEL_DIST, SPATIAL);
//                        addPathElement(res, SPATIAL_MODEL_DIST_FILE, SPATIAL_MODEL_DIST);
//                                //addPathElement(res, SPATIAL_MODEL_DIST_FILE_CUSTOMPOS, SPATIAL_MODEL_DIST_FILE);
//                                addPathElement(res, SPATIAL_MODEL_DIST_FILE_FILEPOS, SPATIAL_MODEL_DIST_FILE);
//                                        addPathElement(res, InFileBasedSpatialInformationSupplier.thisName(), SPATIAL_MODEL_DIST_FILE_FILEPOS);
//                                        addPathElement(res, InFileBasedSelectSpatialInformationSupplier.thisName(), SPATIAL_MODEL_DIST_FILE_FILEPOS);
//                                        addPathElement(res, InFileBasedSelectGroupSpatialInformationSupplier.thisName(), SPATIAL_MODEL_DIST_FILE_FILEPOS);
//                                        addPathElement(res, InFileBasedPVactMilieuSupplier.thisName(), SPATIAL_MODEL_DIST_FILE_FILEPOS);
//                                        addPathElement(res, InFileBasedPVactMilieuZipSupplier.thisName(), SPATIAL_MODEL_DIST_FILE_FILEPOS);
//
//        addPathElement(res, TIME, ROOT);
//                addPathElement(res, InDiscreteTimeModel.thisName(), TIME);
//                addPathElement(res, InUnitStepDiscreteTimeModel.thisName(), TIME);
//
//        res.wrapElementBuilder(res.getCachedElement(GRAPHVIZ))
//                .setEdnPriority(counter.getAndInc(ROOT));
//
//                addPathElement(res, GRAPHVIZ_AGENT_COLOR_MAPPING, GRAPHVIZ);
//
//        addPathElement(res, SUBMODULE, ROOT);
//                addPathElement(res, SUBMODULE_GRAPHVIZDEMO, SUBMODULE);
//
//        addPathElement(res, DEV, ROOT);
//                addPathElement(res, TEST, DEV);
//                        addPathElement(res, InTestData.thisName(), TEST);
    }
    public static void applyRes(TreeAnnotationResource res) {
        InRootUI.applyRes(res);
        //TODO remove
//        res.getCachedElement("OPTACT").setParent(res.getCachedElement(SUBMODULE));
//        res.getCachedElement("AgentGroup_Element").setParent(res.getCachedElement("OPTACT"));
    }
}