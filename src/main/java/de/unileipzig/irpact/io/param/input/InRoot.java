package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.graph.topology.GraphTopology;
import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.LocData;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.*;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.agent.population.InPopulationSize;
import de.unileipzig.irpact.io.param.input.agent.population.InRelativeExternConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.interest.InProductGroupThresholdEntry;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
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
import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.start.optact.gvin.GvInRoot;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irpact.start.optact.network.IGraphTopology;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irptools.defstructure.AnnotationResource;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.def.*;
import de.unileipzig.irptools.uiedn.Section;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.UiEdn;
import de.unileipzig.irptools.util.Util;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
@Definition(root = true)
public class InRoot implements RootClass {

    public static final String SET_VERSION = "set_InVersion";

    public static final InRoot INSTANCE = new InRoot();

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
    public InVersion[] version = new InVersion[]{InVersion.currentVersion()};

    public void setVersion(InVersion version) {
        this.version = new InVersion[]{version};
    }
    public void setVersion(InVersion[] version) {
        this.version = version;
    }
    public InVersion getVersion() throws ParsingException {
        return getInstance(version, "version");
    }

    //=========================
    //affinity
    //=========================

    @FieldDefinition
    public InAffinityEntry[] affinityEntries = new InComplexAffinityEntry[0];

    public void setAffinityEntries(InAffinityEntry[] affinityEntries) {
        this.affinityEntries = affinityEntries;
    }
    public InAffinityEntry[] getAffinityEntries() throws ParsingException {
        return getNonEmptyArray(affinityEntries, "affinitEntries");
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
    public InPopulationSize[] agentPopulationSizes = new InPopulationSize[0];

    public void setAgentPopulationSize(InPopulationSize agentPopulationSize) {
        this.agentPopulationSizes = new InPopulationSize[]{agentPopulationSize};
    }
    public void setAgentPopulationSizes(InPopulationSize[] agentPopulationSizes) {
        this.agentPopulationSizes = agentPopulationSizes;
    }
    public InPopulationSize[] getAgentPopulationSizes() throws ParsingException {
        return getNonNullArray(agentPopulationSizes, "agentPopulationSizes");
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
    public InConsumerAgentGroupColor[] consumerAgentGroupColors = new InConsumerAgentGroupColor[0];

    public void setConsumerAgentGroupColors(InConsumerAgentGroupColor[] consumerAgentGroupColors) {
        this.consumerAgentGroupColors = consumerAgentGroupColors;
    }
    public InConsumerAgentGroupColor[] getConsumerAgentGroupColors() throws ParsingException {
        return ParamUtil.getNonNullArray(consumerAgentGroupColors, "consumerAgentGroupColors");
    }

    @FieldDefinition
    public GraphvizLayoutAlgorithm[] layoutAlgorithms = new GraphvizLayoutAlgorithm[0];

    @FieldDefinition
    public GraphvizOutputFormat[] outputFormats = new GraphvizOutputFormat[0];

    @FieldDefinition
    public GraphvizColor[] colors = new GraphvizColor[0];

    @FieldDefinition
    public GraphvizGlobal graphvizGlobal = new GraphvizGlobal();

    //=========================
    //OPTACT
    //=========================

    @FieldDefinition
    public AgentGroup[] agentGroups = new AgentGroup[0];

    @FieldDefinition
    public IGraphTopology[] topologies = new IGraphTopology[0];

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

    //==================================================

    public InRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES_WITH_GRAPHVIZ;
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
            imageSection.setImage("agentGraph");
            imageSection.setDescription("Agentennetzwerk in IRPact");
            imageSection.setIcon("fa fa-spinner");
            sections.add(imageSection);
        }
    }

    //=========================
    //IRPACT
    //=========================

    private static <T> Stream<T> streamArray(T[] array, Predicate<? super T> filter) {
        if(array == null) {
            return Stream.empty();
        } else {
            return Arrays.stream(array)
                    .filter(filter);
        }
    }

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

    public InProductGroup findProductGroup(String name) {
        if(productGroups == null) {
            return null;
        }
        for(InProductGroup pg: productGroups) {
            if(Objects.equals(pg.getName(), name)) {
                return pg;
            }
        }
        return null;
    }

    public boolean hasBinaryPersistData() {
        return binaryPersistData != null && binaryPersistData.length > 0;
    }

    //=========================
    //OPTACT
    //=========================

    public GraphvizColor getColor(String agentName) {
        for(AgentGroup grp: agentGroups) {
            if(agentName.startsWith(grp._name)) {
                return grp.agentColor;
            }
        }
        return null;
    }

    public OutputFormat getOutputFormat() {
        for(GraphvizOutputFormat f: outputFormats) {
            if(f.useFormat) {
                return f.toOutputFormat();
            }
        }
        return null;
    }

    public LayoutAlgorithm getLayoutAlgorithm() {
        for(GraphvizLayoutAlgorithm a: layoutAlgorithms) {
            if(a.useLayout) {
                return a.toLayoutAlgorithm();
            }
        }
        return null;
    }

    public <N, E> GraphTopology<N, E> getTopology() {
        for(IGraphTopology topology: topologies) {
            if(topology.use()) {
                return topology.createInstance();
            }
        }
        return null;
    }

    //=========================
    //CLASSES
    //=========================

    @Todo("testen, ob alles Klassen geladen werden")
    public static final List<ParserInput> INPUT_WITHOUT_ROOT = ParserInput.listOf(Type.INPUT,
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
            InFixConsumerAgentPopulationSize.class,
            InPopulationSize.class,
            InRelativeExternConsumerAgentPopulationSize.class,

            VisibleBinaryData.class,
            BinaryPersistData.class, //special

            InBernoulliDistribution.class,
            InBooleanDistribution.class,
            InConstantUnivariateDistribution.class,
            InFiniteMassPointsDiscreteDistribution.class,
            InMassPoint.class,
            InNormalDistribution.class,
            InRandomBoundedIntegerDistribution.class,
            InUnivariateDoubleDistribution.class,

            InFile.class,
            InPVFile.class,
            InSpatialTableFile.class,

            InConsumerAgentGroupColor.class,

            InProductGroupThresholdEntry.class,
            InProductInterestSupplyScheme.class,
            InProductThresholdInterestSupplyScheme.class,

            InCompleteGraphTopology.class,
            InDistanceEvaluator.class,
            InFreeNetworkTopology.class,
            InGraphTopologyScheme.class,
            InInverse.class,
            InNoDistance.class,
            InNumberOfTies.class,
            InUnlinkedGraphTopology.class,

            InAutoUncertaintyGroupAttribute.class,
            InDisabledProcessPlanNodeFilterScheme.class,
            InEntireNetworkNodeFilterScheme.class,
            InIndividualAttributeBasedUncertaintyGroupAttribute.class,
            InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute.class,
            InNameBasedUncertaintyGroupAttribute.class,
            InNameBasedUncertaintyWithConvergenceGroupAttribute.class,
            InPVactUncertaintyGroupAttribute.class,
            InRAProcessModel.class,
            InRAProcessPlanMaxDistanceFilterScheme.class,
            InRAProcessPlanNodeFilterScheme.class,
            InUncertaintyGroupAttribute.class,
            InProcessModel.class,
            InProcessPlanNodeFilterScheme.class,

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

            InCustomFileSelectedGroupedSpatialDistribution2D.class,
            InCustomFileSelectedSpatialDistribution2D.class,
            InCustomFileSpatialDistribution2D.class,
            InFileSelectedGroupedSpatialDistribution2D.class,
            InFileSelectedSpatialDistribution2D.class,
            InFileSpatialDistribution2D.class,
            InSpatialDistribution.class,
            InSpace2D.class,
            InSpatialModel.class,

            InDiscreteTimeModel.class,
            InTimeModel.class,
            InUnitStepDiscreteTimeModel.class,

            InAttributeName.class,
            InEntity.class,
            InGeneral.class,
            InVersion.class
    );

    public static final List<ParserInput> INPUT_WITH_ROOT = Util.mergedArrayListOf(
            INPUT_WITHOUT_ROOT,
            ParserInput.asInput(Type.INPUT,
                    CollectionUtil.arrayListOf(
                            InRoot.class
                    )
            )
    );

    public static final List<ParserInput> CLASSES_WITHOUT_GRAPHVIZ = Util.mergedArrayListOf(
            INPUT_WITH_ROOT,
            GvInRoot.CLASSES_WITHOUT_ROOT_AND_GRAPHVIZ
    );

    public static final List<ParserInput> CLASSES_WITH_GRAPHVIZ = Util.mergedArrayListOf(
            CLASSES_WITHOUT_GRAPHVIZ,
            ParserInput.asInput(Type.INPUT,
                    CollectionUtil.arrayListOf(
                            GraphvizColor.class,
                            GraphvizLayoutAlgorithm.class,
                            GraphvizOutputFormat.class,
                            GraphvizGlobal.class
                    )
            )
    );

    //=========================
    //UI
    //=========================

    public static void initRes(TreeAnnotationResource res) {
        IOResources.Data userData = res.getUserDataAs();
        MultiCounter counter = userData.getCounter();
        LocData loc = userData.getData();

        addPathElement(res, GENERAL_SETTINGS, ROOT);
                addPathElement(res, LOGGING, GENERAL_SETTINGS);
                        addPathElement(res, LOGGING_GENERAL, LOGGING);
                        addPathElement(res, LOGGING_DATA, LOGGING);
                        addPathElement(res, LOGGING_RESULT, LOGGING);
                addPathElement(res, SPECIAL_SETTINGS, GENERAL_SETTINGS);
                    addPathElement(res, VisibleBinaryData.thisName(), SPECIAL_SETTINGS);

        addPathElement(res, InAttributeName.thisName(), ROOT);

        addPathElement(res, FILES, ROOT);
            addPathElement(res, InPVFile.thisName(), FILES);
            addPathElement(res, InSpatialTableFile.thisName(), FILES);

        addPathElement(res, DISTRIBUTIONS, ROOT);
            addPathElement(res, InBooleanDistribution.thisName(), DISTRIBUTIONS);
            addPathElement(res, InConstantUnivariateDistribution.thisName(), DISTRIBUTIONS);
            addPathElement(res, InRandomBoundedIntegerDistribution.thisName(), DISTRIBUTIONS);
            addPathElement(res, InFiniteMassPointsDiscreteDistribution.thisName(), DISTRIBUTIONS);
                addPathElement(res, InMassPoint.thisName(), InFiniteMassPointsDiscreteDistribution.thisName());

        addPathElement(res, AGENTS, ROOT);
                addPathElement(res, CONSUMER, AGENTS);
                        addPathElement(res, CONSUMER_GROUP, CONSUMER);
                            addPathElement(res, InGeneralConsumerAgentGroup.thisName(), CONSUMER_GROUP);
                            addPathElement(res, InPVactConsumerAgentGroup.thisName(), CONSUMER_GROUP);
                        addPathElement(res, CONSUMER_ATTR, CONSUMER);
                                    addPathElement(res, InGeneralConsumerAgentGroupAttribute.thisName(), CONSUMER_GROUP);
                                    addPathElement(res, InGeneralConsumerAgentAnnualGroupAttribute.thisName(), CONSUMER_GROUP);
                                    addPathElement(res, InNameSplitConsumerAgentGroupAttribute.thisName(), CONSUMER_GROUP);
                                    addPathElement(res, InNameSplitConsumerAgentAnnualGroupAttribute.thisName(), CONSUMER_GROUP);
                        addPathElement(res, CONSUMER_AFFINITY, CONSUMER);
                                addPathElement(res, InComplexAffinityEntry.thisName(), CONSUMER_AFFINITY);
                                addPathElement(res, InNameSplitAffinityEntry.thisName(), CONSUMER_AFFINITY);
                        addPathElement(res, CONSUMER_INTEREST, CONSUMER);
                                addPathElement(res, InProductThresholdInterestSupplyScheme.thisName(), CONSUMER_INTEREST);
                                    addPathElement(res, InProductGroupThresholdEntry.thisName(), InProductThresholdInterestSupplyScheme.thisName());
                addPathElement(res, POPULATION, AGENTS);
                        addPathElement(res, InFixConsumerAgentPopulationSize.thisName(), POPULATION);
                        addPathElement(res, InRelativeExternConsumerAgentPopulationSize.thisName(), POPULATION);

        addPathElement(res, NETWORK, ROOT);
                addPathElement(res, TOPOLOGY, NETWORK);
                        addPathElement(res, InUnlinkedGraphTopology.thisName(), TOPOLOGY);
                        addPathElement(res, InCompleteGraphTopology.thisName(), TOPOLOGY);
                        addPathElement(res, InFreeNetworkTopology.thisName(), TOPOLOGY);
                                addPathElement(res, InNumberOfTies.thisName(), InFreeNetworkTopology.thisName());
                addPathElement(res, DIST_FUNC, NETWORK);
                        addPathElement(res, InNoDistance.thisName(), DIST_FUNC);
                        addPathElement(res, InInverse.thisName(), DIST_FUNC);

        addPathElement(res, PRODUCTS, ROOT);
                addPathElement(res, PRODUCTS_GROUP, PRODUCTS);
                        addPathElement(res, InBasicProductGroup.thisName(), PRODUCTS_GROUP);
                addPathElement(res, PRODUCTS_ATTR, PRODUCTS);
                        addPathElement(res, InBasicProductGroupAttribute.thisName(), PRODUCTS_ATTR);
                        addPathElement(res, InNameSplitProductGroupAttribute.thisName(), PRODUCTS_ATTR);
                addPathElement(res, InFixProduct.thisName(), PRODUCTS);
                addPathElement(res, InFixProductAttribute.thisName(), PRODUCTS);
                addPathElement(res, PRODUCTS_FINDING_SCHEME, PRODUCTS);
                        addPathElement(res, InFixProductFindingScheme.thisName(), PRODUCTS_FINDING_SCHEME);

        addPathElement(res, PROCESS_MODEL, ROOT);
                addPathElement(res, InRAProcessModel.thisName(), PROCESS_MODEL);
                        addPathElement(res, PROCESS_MODEL_RA_UNCERT, InRAProcessModel.thisName());
                                addPathElement(res, InAutoUncertaintyGroupAttribute.thisName(), PROCESS_MODEL_RA_UNCERT);
                                addPathElement(res, InIndividualAttributeBasedUncertaintyGroupAttribute.thisName(), PROCESS_MODEL_RA_UNCERT);
                                addPathElement(res, InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute.thisName(), PROCESS_MODEL_RA_UNCERT);
                                addPathElement(res, InNameBasedUncertaintyGroupAttribute.thisName(), PROCESS_MODEL_RA_UNCERT);
                                addPathElement(res, InNameBasedUncertaintyWithConvergenceGroupAttribute.thisName(), PROCESS_MODEL_RA_UNCERT);
                                addPathElement(res, InPVactUncertaintyGroupAttribute.thisName(), PROCESS_MODEL_RA_UNCERT);
                addPathElement(res, PROCESS_FILTER, PROCESS_MODEL);
                        addPathElement(res, InDisabledProcessPlanNodeFilterScheme.thisName(), PROCESS_FILTER);
                        addPathElement(res, InEntireNetworkNodeFilterScheme.thisName(), PROCESS_FILTER);
                        addPathElement(res, InRAProcessPlanMaxDistanceFilterScheme.thisName(), PROCESS_FILTER);

        addPathElement(res, SPATIAL, ROOT);
                addPathElement(res, SPATIAL_MODEL, SPATIAL);
                        addPathElement(res, InSpace2D.thisName(), SPATIAL_MODEL);
                addPathElement(res, SPATIAL_MODEL_DIST, SPATIAL);
                        addPathElement(res, SPATIAL_MODEL_DIST_FILE, SPATIAL_MODEL_DIST);
                                addPathElement(res, SPATIAL_MODEL_DIST_FILE_CUSTOMPOS, SPATIAL_MODEL_DIST_FILE);
                                        addPathElement(res, InCustomFileSpatialDistribution2D.thisName(), SPATIAL_MODEL_DIST_FILE_CUSTOMPOS);
                                        addPathElement(res, InCustomFileSelectedSpatialDistribution2D.thisName(), SPATIAL_MODEL_DIST_FILE_CUSTOMPOS);
                                        addPathElement(res, InCustomFileSelectedGroupedSpatialDistribution2D.thisName(), SPATIAL_MODEL_DIST_FILE_CUSTOMPOS);
                                addPathElement(res, SPATIAL_MODEL_DIST_FILE_FILEPOS, SPATIAL_MODEL_DIST_FILE);
                                        addPathElement(res, InFileSpatialDistribution2D.thisName(), SPATIAL_MODEL_DIST_FILE_FILEPOS);
                                        addPathElement(res, InFileSelectedSpatialDistribution2D.thisName(), SPATIAL_MODEL_DIST_FILE_FILEPOS);
                                        addPathElement(res, InFileSelectedGroupedSpatialDistribution2D.thisName(), SPATIAL_MODEL_DIST_FILE_FILEPOS);

        addPathElement(res, TIME, ROOT);
                addPathElement(res, InDiscreteTimeModel.thisName(), TIME);
                addPathElement(res, InUnitStepDiscreteTimeModel.thisName(), TIME);

        res.wrapElementBuilder(res.getCachedElement(GRAPHVIZ))
                .setEdnPriority(counter.getAndInc(ROOT));

                addPathElement(res, GRAPHVIZ_AGENT_COLOR_MAPPING, GRAPHVIZ);

        addPathElement(res, SUBMODULE, ROOT);
                addPathElement(res, SUBMODULE_GRAPHVIZDEMO, SUBMODULE);
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.getCachedElement("OPTACT").setParent(res.getCachedElement(SUBMODULE));
        res.getCachedElement("AgentGroup_Element").setParent(res.getCachedElement("OPTACT"));
    }
}