package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.MultiCounter;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.graph.topology.GraphTopology;
import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.LocData;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.*;
import de.unileipzig.irpact.io.param.input.agent.population.FixConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.agent.population.PopulationSize;
import de.unileipzig.irpact.io.param.input.agent.population.RelativeExternConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
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
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.AnnotationResource;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.def.*;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.UiEdn;
import de.unileipzig.irptools.util.Util;

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

    @FieldDefinition
    public InIndependentConsumerAgentGroupAttribute[] independentConsumerAgentGroupAttributes = new InIndependentConsumerAgentGroupAttribute[0];

    public void setIndependentConsumerAgentGroupAttributes(InIndependentConsumerAgentGroupAttribute[] independentConsumerAgentGroupAttributes) {
        this.independentConsumerAgentGroupAttributes = independentConsumerAgentGroupAttributes;
    }
    public InIndependentConsumerAgentGroupAttribute[] getIndependentConsumerAgentGroupAttributes() throws ParsingException {
        return getNonNullArray(independentConsumerAgentGroupAttributes, "independentConsumerAgentGroupAttributes");
    }

    @FieldDefinition
    public PopulationSize[] agentPopulationSizes = new PopulationSize[0];

    public void setAgentPopulationSizes(PopulationSize[] agentPopulationSizes) {
        this.agentPopulationSizes = agentPopulationSizes;
    }
    public PopulationSize[] getAgentPopulationSizes() throws ParsingException {
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
    public void peekEdn(Sections sections, UiEdn ednType) {
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
            InGeneralConsumerAgentGroup.class,
            InGeneralConsumerAgentGroupAttribute.class,
            InNameSplitConsumerAgentGroupAttribute.class,
            InPVactConsumerAgentGroup.class,

            InProductInterestSupplyScheme.class,
            InProductThresholdInterestSupplyScheme.class,

            VisibleBinaryData.class,
            BinaryPersistData.class, //special

            InBooleanDistribution.class,
            InConstantUnivariateDistribution.class,
            InFiniteMassPointsDiscreteDistribution.class,
            InMassPoint.class,
            InRandomBoundedIntegerDistribution.class,
            InUnivariateDoubleDistribution.class,

            InFile.class,
            InPVFile.class,
            InSpatialTableFile.class,

            InConsumerAgentGroupColor.class,

            InCompleteGraphTopology.class,
            InDistanceEvaluator.class,
            InFreeNetworkTopology.class,
            InGraphTopologyScheme.class,
            InInverse.class,
            InNoDistance.class,
            InNumberOfTies.class,
            InUnlinkedGraphTopology.class,

            InAutoUncertaintyGroupAttribute.class,
            InNameBasedUncertaintyWithConvergenceGroupAttribute.class,
            InProcessModel.class,
            InRAProcessModel.class,
            InUncertaintyGroupAttribute.class,

            InFixProduct.class,
            InFixProductAttribute.class,
            InFixProductFindingScheme.class,
            InProductFindingScheme.class,
            InBasicProductGroup.class,

            InCustomFileSelectedGroupedSpatialDistribution2D.class,
            InCustomFileSelectedSpatialDistribution2D.class,
            InCustomFileSpatialDistribution2D.class,
            InSpatialDistribution.class,
            InSpace2D.class,
            InSpatialModel.class,

            InDiscreteTimeModel.class,
            InTimeModel.class,

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
                        addPathElement(res, LOGGING_SPECIFIC, LOGGING);
                addPathElement(res, SPECIAL_SETTINGS, GENERAL_SETTINGS);
                    addPathElement(res, VisibleBinaryData.thisName(), SPECIAL_SETTINGS);

        addPathElement(res, NAMES, ROOT);

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
                                    addPathElement(res, InNameSplitConsumerAgentGroupAttribute.thisName(), CONSUMER_GROUP);
                        addPathElement(res, CONSUMER_AFFINITY, CONSUMER);
                                addPathElement(res, InComplexAffinityEntry.thisName(), CONSUMER_AFFINITY);
                                addPathElement(res, InNameSplitAffinityEntry.thisName(), CONSUMER_AFFINITY);
                        addPathElement(res, CONSUMER_INTEREST, CONSUMER);
                                addPathElement(res, InProductThresholdInterestSupplyScheme.thisName(), CONSUMER_INTEREST);
                addPathElement(res, POPULATION, AGENTS);
                        addPathElement(res, FixConsumerAgentPopulationSize.thisName(), POPULATION);
                        addPathElement(res, RelativeExternConsumerAgentPopulationSize.thisName(), POPULATION);

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



    /*
    public static void initRes(TreeAnnotationResource res) {
        MultiCounter counter = res.getUserDataAs();

        res.newElementBuilder()
                .setEdnLabel("Allgemeine Einstellungen")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Allgemeine Einstellungen");

                res.newElementBuilder()
                        .setEdnLabel("Logging")
                        .setEdnPriority(counter.getAndInc("Allgemeine Einstellungen"))
                        .setEdnDescription("Einstellungen für das Logging und die Ausgabe von Informationen und Daten. Unabhängig von den individuellen Logging-Einstellungen werden wichtige Informationen weiterhin geloggt. Diese können nur mittels dem Logging-Level deaktiviert werden (nicht empfohlen).")
                        .putCache("Logging");

                res.newElementBuilder()
                        .setEdnLabel("Spezielle Einstellungen")
                        .setEdnPriority(counter.getAndInc("Allgemeine Einstellungen"))
                        .putCache("Spezielle Einstellungen");

                        res.newElementBuilder()
                                .setEdnLabel("Binäre Datem")
                                .setEdnPriority(counter.getAndInc("Spezielle Einstellungen"))
                                .putCache("VisibleBinaryData");

        res.newElementBuilder()
                .setEdnLabel("Namen")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Namen");

        res.newElementBuilder()
                .setEdnLabel("Dateien")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Dateien");

                res.newElementBuilder()
                        .setEdnLabel("PV Daten")
                        .setEdnPriority(counter.getAndInc("Dateien"))
                        .putCache("PV Daten");

                res.newElementBuilder()
                        .setEdnLabel("Tabellen")
                        .setEdnPriority(counter.getAndInc("Dateien"))
                        .putCache("Tabellen");

        res.newElementBuilder()
                .setEdnLabel("Verteilungsfunktionen")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Verteilungsfunktionen");

                res.newElementBuilder()
                        .setEdnLabel("Boolean")
                        .setEdnPriority(counter.getAndInc("Verteilungsfunktionen"))
                        .setEdnDescription("Verteilungsfunktion, welche 0 oder 1 zurück gibt.")
                        .putCache("Boolean");

                res.newElementBuilder()
                        .setEdnLabel("Dirac")
                        .setEdnPriority(counter.getAndInc("Verteilungsfunktionen"))
                        .setEdnDescription("Verteilungsfunktion, welche einen konstanten Wert zurück gibt.")
                        .putCache("Dirac");

                res.newElementBuilder()
                        .setEdnLabel("Gleichverteilte ganze Zahlen")
                        .setEdnPriority(counter.getAndInc("Verteilungsfunktionen"))
                        .setEdnDescription("Verteilungsfunktion, welche ganzzahlige Werte gleichverteilt aus einem Bereich zieht.")
                        .putCache("RandomBoundedInteger");

                res.newElementBuilder()
                        .setEdnLabel("Gewichtete Massepunkte")
                        .setEdnPriority(counter.getAndInc("Verteilungsfunktionen"))
                        .setEdnDescription("Verteilungsfunktion, welche auf Massepunkten basiert.")
                        .putCache("FiniteMassPointsDiscreteDistribution");

                            res.newElementBuilder()
                                    .setEdnLabel("Massepunkt")
                                    .setEdnPriority(counter.getAndInc("FiniteMassPointsDiscreteDistribution"))
                                    .setEdnDescription("Massepunkt mit Wert und Gewicht")
                                    .putCache("Massepunkt");

        res.newElementBuilder()
                .setEdnLabel("Agenten")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Agenten");

                res.newElementBuilder()
                        .setEdnLabel("Konsumer")
                        .setEdnPriority(counter.getAndInc("Agenten"))
                        .putCache("Konsumer");

                        res.newElementBuilder()
                                .setEdnLabel("Gruppen")
                                .setEdnPriority(counter.getAndInc("Konsumer"))
                                .putCache("Gruppen");

                                res.newElementBuilder()
                                        .setEdnLabel("Gruppe-Attribut-Mapping")
                                        .setEdnPriority(counter.getAndInc("KonsumerGruppen"))
                                        .putCache("Gruppe-Attribut-Mapping");

                                res.newElementBuilder()
                                        .setEdnLabel("Gruppe-Awareness-Mapping")
                                        .setEdnPriority(counter.getAndInc("KonsumerGruppen"))
                                        .putCache("Gruppe-Awareness-Mapping");

                                res.newElementBuilder()
                                        .setEdnLabel("Gruppe-ProductFinding-Mapping")
                                        .setEdnPriority(counter.getAndInc("KonsumerGruppen"))
                                        .putCache("Gruppe-ProductFinding-Mapping");

                                res.newElementBuilder()
                                        .setEdnLabel("Gruppe-Spatial-Mapping")
                                        .setEdnPriority(counter.getAndInc("KonsumerGruppen"))
                                        .putCache("Gruppe-Spatial-Mapping");

                        res.newElementBuilder()
                                .setEdnLabel("Attribute")
                                .setEdnPriority(counter.getAndInc("Konsumer"))
                                .putCache("Attribute");

                                res.newElementBuilder()
                                        .setEdnLabel("Attribute-Name-Mapping")
                                        .setEdnPriority(counter.getAndInc("KonsumerAttribute"))
                                        .putCache("Attribute-Name-Mapping");

                                res.newElementBuilder()
                                        .setEdnLabel("Attribute-Verteilung-Mapping")
                                        .setEdnPriority(counter.getAndInc("KonsumerAttribute"))
                                        .putCache("Attribute-Verteilung-Mapping");

                        res.newElementBuilder()
                                .setEdnLabel("Affinity")
                                .setEdnPriority(counter.getAndInc("Konsumer"))
                                .setEdnDescription("Legt die Affinity zwischen den jeweiligen Konsumergruppen fest.")
                                .putCache("Affinity");

                        res.newElementBuilder()
                                .setEdnLabel("Awareness")
                                .setEdnPriority(counter.getAndInc("Konsumer"))
                                .setEdnDescription("Legt die verwendeten Awareness-Varianten für die Konsumergruppen fest.")
                                .putCache("Awareness");

                                res.newElementBuilder()
                                        .setEdnLabel("Threshold")
                                        .setEdnPriority(counter.getAndInc("Awareness"))
                                        .setEdnDescription("Grenzwert-basierte Awareness")
                                        .putCache("Threshold");

        res.newElementBuilder()
                .setEdnLabel("Netzwerk")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Netzwerk");

                res.newElementBuilder()
                        .setEdnLabel("Topologie")
                        .setEdnPriority(counter.getAndInc("Netzwerk"))
                        .putCache("Topologie");

                        res.newElementBuilder()
                                .setEdnLabel("Leere Topologie")
                                .setEdnPriority(counter.getAndInc("Topologie"))
                                .setEdnDescription("Leere Topologie, welche keine Kanten besitzt.")
                                .putCache("Leere Topologie");

                        res.newElementBuilder()
                                .setEdnLabel("Vollständiger Graph")
                                .setEdnPriority(counter.getAndInc("Topologie"))
                                .setEdnDescription("Nutzt einen vollständigen Netzwerkgraph.")
                                .putCache("Complete Topologie");

                        res.newElementBuilder()
                                .setEdnLabel("Freie Topologie")
                                .setEdnPriority(counter.getAndInc("Topologie"))
                                .setEdnDescription("Freie Topologie")
                                .putCache("Freie Topologie");

                                res.newElementBuilder()
                                        .setEdnLabel("Kanten je Konsumergruppe")
                                        .setEdnPriority(counter.getAndInc("Freie Topologie"))
                                        .setEdnDescription("Legt die Kantenanzahl je Gruppe fest.")
                                        .putCache("Anzahl Kanten");

                res.newElementBuilder()
                        .setEdnLabel("Abstandsfunktion")
                        .setEdnPriority(counter.getAndInc("Netzwerk"))
                        .putCache("Abstandsfunktion");

                        res.newElementBuilder()
                                .setEdnLabel("Inverse Distanz")
                                .setEdnPriority(counter.getAndInc("Abstandsfunktion"))
                                .setEdnDescription("Benutzt 1/distance für die Auswertung.")
                                .putCache("Invers");

                        res.newElementBuilder()
                                .setEdnLabel("Keine Distanzfunktion")
                                .setEdnPriority(counter.getAndInc("Abstandsfunktion"))
                                .setEdnDescription("Keine Distanzfunktion")
                                .putCache("NoDistance");

        res.newElementBuilder()
                .setEdnLabel("Produkte")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Produkte");

                        res.newElementBuilder()
                                .setEdnLabel("Gruppen")
                                .setEdnPriority(counter.getAndInc("Produkte"))
                                .putCache("Gruppen_Product");

                                res.newElementBuilder()
                                        .setEdnLabel("Produkt-Attribut-Mapping")
                                        .setEdnPriority(counter.getAndInc("ProdukteGruppen"))
                                        .putCache("Produkt-Attribut-Mapping");

                        res.newElementBuilder()
                                .setEdnLabel("Attribute")
                                .setEdnPriority(counter.getAndInc("Produkte"))
                                .putCache("Attribute_Product");

                                res.newElementBuilder()
                                        .setEdnLabel("Attribut-Namen-Mapping")
                                        .setEdnPriority(counter.getAndInc("ProdukteAttribute"))
                                        .putCache("Namen-Mapping_Product");

                                res.newElementBuilder()
                                        .setEdnLabel("Attribut-Verteilungs-Mapping")
                                        .setEdnPriority(counter.getAndInc("ProdukteAttribute"))
                                        .putCache("Verteilungs-Mapping_Product");

                        res.newElementBuilder()
                                .setEdnLabel("Initiale Produkte")
                                .setEdnPriority(counter.getAndInc("Produkte"))
                                .setEdnDescription("Die initialen Produkte, welche von Beginn an existieren sollen.")
                                .putCache("Initiale_Produkte");

                                res.newElementBuilder()
                                        .setEdnLabel("Initiale Produkt-Attribut-Mapping")
                                        .setEdnPriority(counter.getAndInc("ProdukteInitiale"))
                                        .putCache("Initiale_Produkt-Attribut-Mapping");

                        res.newElementBuilder()
                                .setEdnLabel("Initiale Produktattribute")
                                .setEdnPriority(counter.getAndInc("Produkte"))
                                .setEdnDescription("Attribute der initialen Produkte")
                                .putCache("Initiale_Produktattribute");

                        res.newElementBuilder()
                                .setEdnLabel("Schema für Produktfindung")
                                .setEdnPriority(counter.getAndInc("Produkte"))
                                .putCache("InProductFindingScheme");

                                res.newElementBuilder()
                                        .setEdnLabel("Schema für initiale Produkte")
                                        .setEdnPriority(counter.getAndInc("InProductFindingScheme"))
                                        .setEdnDescription("Schema basierend auf initialen Produkten")
                                        .putCache("InFixProductFindingScheme");

        res.newElementBuilder()
                .setEdnLabel("Prozessmodell")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Prozessmodell");

                res.newElementBuilder()
                        .setEdnLabel("Relative Agreement")
                        .setEdnPriority(counter.getAndInc("Prozessmodell"))
                        .putCache("Relative Agreement");

                        res.newElementBuilder()
                                .setEdnLabel("Datenerweiterung")
                                .setEdnPriority(counter.getAndInc("Relative Agreement"))
                                .putCache("Datenerweiterung");

                                res.newElementBuilder()
                                        .setEdnLabel("Orientierung")
                                        .setEdnPriority(counter.getAndInc("Datenerweiterung"))
                                        .setEdnDescription("Zieht die Orietierungsdaten basierend auf der verwendeten Verteilungsfunktion.")
                                        .putCache("Orientierung");

                                res.newElementBuilder()
                                        .setEdnLabel("Neigung")
                                        .setEdnPriority(counter.getAndInc("Datenerweiterung"))
                                        .putCache("Neigung");

                        res.newElementBuilder()
                                .setEdnLabel("Uncertainty")
                                .setEdnPriority(counter.getAndInc("Relative Agreement"))
                                .putCache("Uncertainty");

                                res.newElementBuilder()
                                        .setEdnLabel("Automatisch")
                                        .setEdnPriority(counter.getAndInc("Uncertainty"))
                                        .putCache("UncertaintyAuto");

                                res.newElementBuilder()
                                        .setEdnLabel("Benutzerdefiniert")
                                        .setEdnPriority(counter.getAndInc("Uncertainty"))
                                        .putCache("UncertaintyCustom");

        res.newElementBuilder()
                .setEdnLabel("Räumliche Modell")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Räumliche Modell");

                res.newElementBuilder()
                        .setEdnLabel("Space2D")
                        .setEdnPriority(counter.getAndInc("SpatialModel"))
                        .putCache("Space2D");

                res.newElementBuilder()
                        .setEdnLabel("Verteilungsfunktion")
                        .setEdnPriority(counter.getAndInc("SpatialModel"))
                        .putCache("SpatialDist");

                        res.newElementBuilder()
                                .setEdnLabel("Benutzerdefinierte Position")
                                .setEdnPriority(counter.getAndInc("SpatialVerteilungsfunktion"))
                                .putCache("CustomPos");

                                res.newElementBuilder()
                                        .setEdnLabel("Ungefiltert")
                                        .setEdnPriority(counter.getAndInc("CustomPos"))
                                        .putCache("InCustomSpatialDistribution2D");

                                res.newElementBuilder()
                                        .setEdnLabel("Gefiltert")
                                        .setEdnPriority(counter.getAndInc("CustomPos"))
                                        .putCache("InCustomSelectedSpatialDistribution2D");

                                res.newElementBuilder()
                                        .setEdnLabel("Gefiltert und gewichtet")
                                        .setEdnPriority(counter.getAndInc("CustomPos"))
                                        .putCache("InCustomSelectedGroupedSpatialDistribution2D");

                        res.newElementBuilder()
                                .setEdnLabel("vorgegebene Positionen")
                                .setEdnPriority(counter.getAndInc("SpatialVerteilungsfunktion"))
                                .putCache("DataPos");

                                res.newElementBuilder()
                                        .setEdnLabel("Milieuunabhängig")
                                        .setEdnPriority(counter.getAndInc("DataPos"))
                                        .putCache("InDataSpatialDistribution2D");

                                res.newElementBuilder()
                                        .setEdnLabel("Milieuabhängig")
                                        .setEdnPriority(counter.getAndInc("DataPos"))
                                        .putCache("InDataSelectedSpatialDistribution2D");

                                res.newElementBuilder()
                                        .setEdnLabel("Milieuabhängig und gewichtet")
                                        .setEdnPriority(counter.getAndInc("DataPos"))
                                        .putCache("InDataSelectedGroupedSpatialDistribution2D");

                res.newElementBuilder()
                        .setEdnLabel("Räumliche Eingabedateien")
                        .setEdnPriority(counter.getAndInc("SpatialModel"))
                        .putCache("SpatialInput");

        res.newElementBuilder()
                .setEdnLabel("Zeitliche Modell")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Zeitliche Modell");

                res.newElementBuilder()
                        .setEdnLabel("Diskret")
                        .setEdnPriority(counter.getAndInc("Zeitliche Modell"))
                        .putCache("Diskret");

        res.wrapElementBuilder(res.getCachedElement("Graphviz"))
                .setEdnPriority(counter.getAndInc("main"));

                res.newElementBuilder()
                        .setEdnLabel("Agentengruppe-Farben-Mapping")
                        .setEdnPriority(counter.getAndInc("Graphviz"))
                        .setEdnDescription("Agentengruppe, welche im Graphen dargestellt werden sollen.")
                        .putCache("Agentengruppe-Farben-Mapping");

        res.newElementBuilder()
                .setEdnLabel("Submodule")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Submodule");

        res.newElementBuilder()
                .setEdnLabel("Graphvizdemo")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Graphvizdemo");

        res.newElementBuilder()
                .setEdnLabel("Diverses")
                .setEdnPriority(counter.getAndInc("main"))
                .putCache("Diverses");

    }
    */