package de.unileipzig.irpact.io.input;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.graph.topology.GraphTopology;
import de.unileipzig.irpact.io.IOResources;
import de.unileipzig.irpact.io.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.awareness.InAwareness;
import de.unileipzig.irpact.io.input.awareness.InThresholdAwareness;
import de.unileipzig.irpact.io.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.input.distribution.InRandomBoundedIntegerDistribution;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.input.network.*;
import de.unileipzig.irpact.io.inout.binary.HiddenBinaryData;
import de.unileipzig.irpact.io.input.process.InOrientationSupplier;
import de.unileipzig.irpact.io.input.process.InProcessModel;
import de.unileipzig.irpact.io.input.process.InRAProcessModel;
import de.unileipzig.irpact.io.input.process.InSlopeSupplier;
import de.unileipzig.irpact.io.input.product.InProductGroup;
import de.unileipzig.irpact.io.input.product.InProductGroupAttribute;
import de.unileipzig.irpact.io.input.spatial.InConstantSpatialDistribution2D;
import de.unileipzig.irpact.io.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.input.spatial.InSpatialDistribution;
import de.unileipzig.irpact.io.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.input.time.InTimeModel;
import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.start.optact.gvin.GvInRoot;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irpact.start.optact.network.IGraphTopology;
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

/**
 * @author Daniel Abitz
 */
@Definition(root = true)
public class InRoot implements RootClass {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Allgemeine Einstellungen")
                .setEdnPriority(0)
                .putCache("Allgemeine Einstellungen");
        res.newElementBuilder()
                .setEdnLabel("Namen")
                .setEdnPriority(1)
                .putCache("Namen");
        res.newElementBuilder()
                .setEdnLabel("Verteilungsfunktionen")
                .setEdnPriority(2)
                .putCache("Verteilungsfunktionen");
        res.newElementBuilder()
                .setEdnLabel("Agenten")
                .setEdnPriority(3)
                .putCache("Agenten");
        res.newElementBuilder()
                .setEdnLabel("Netzwerk")
                .setEdnPriority(4)
                .putCache("Netzwerk");
        res.newElementBuilder()
                .setEdnLabel("Produkte")
                .setEdnPriority(5)
                .putCache("Produkte");
        res.newElementBuilder()
                .setEdnLabel("Prozessmodell")
                .setEdnPriority(6)
                .putCache("Prozessmodell");
        res.newElementBuilder()
                .setEdnLabel("Räumliche Modell")
                .setEdnPriority(7)
                .putCache("Räumliche Modell");
        res.newElementBuilder()
                .setEdnLabel("Zeitliche Modell")
                .setEdnPriority(8)
                .putCache("Zeitliche Modell");
        res.wrapElementBuilder(res.getCachedElement("Graphviz"))
                .setEdnPriority(9);
        res.newElementBuilder()
                .setEdnLabel("Submodule")
                .setEdnPriority(10)
                .putCache("Submodule");
        res.newElementBuilder()
                .setEdnLabel("Graphvizdemo")
                .setEdnPriority(1)
                .putCache("Graphvizdemo");
        res.newElementBuilder()
                .setEdnLabel("Diverses")
                .setEdnPriority(0)
                .putCache("Diverses");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.getCachedElement("OPTACT").setParent(res.getCachedElement("Submodule"));
        res.getCachedElement("AgentGroup_Element").setParent(res.getCachedElement("OPTACT"));
    }

    public static final String SET_VERSION = "set_InVersion";

    public static final InRoot INSTANCE = new InRoot();

    //=========================
    //general
    //=========================

    @FieldDefinition
    public InGeneral general = new InGeneral();

    @FieldDefinition
    public InVersion[] version = new InVersion[0];

    //=========================
    //affinity
    //=========================

    @FieldDefinition
    public InAffinityEntry[] affinityEntries = new InAffinityEntry[0];

    //=========================
    //agent
    //=========================

    @FieldDefinition
    public InConsumerAgentGroup[] consumerAgentGroups = new InConsumerAgentGroup[0];

    //=========================
    //network
    //=========================

    @FieldDefinition
    public InGraphTopologyScheme[] graphTopologySchemes = new InGraphTopologyScheme[0];

    @FieldDefinition
    public InDistanceEvaluator[] distanceEvaluators = new InDistanceEvaluator[0];

    @FieldDefinition
    public InNumberOfTies[] numberOfTies = new InNumberOfTies[0];

    //=========================
    //process
    //=========================

    @FieldDefinition
    public InProcessModel[] processModel = new InProcessModel[0];

    @FieldDefinition
    public InOrientationSupplier[] orientationSupplier = new InOrientationSupplier[0];

    @FieldDefinition
    public InSlopeSupplier[] slopeSupplier = new InSlopeSupplier[0];

    //=========================
    //product
    //=========================

    @FieldDefinition
    public InProductGroup[] productGroups = new InProductGroup[0];

    //=========================
    //spatial
    //=========================

    @FieldDefinition
    public InSpatialModel[] spatialModel = new InSpatialModel[0];

    @FieldDefinition
    public InSpatialDistribution[] spatialDistributions = new InSpatialDistribution[0];

    //=========================
    //time
    //=========================

    @FieldDefinition
    public InTimeModel[] timeModel = new InTimeModel[0];

    //=========================
    //binary
    //=========================

    @FieldDefinition
    public VisibleBinaryData[] visibleBinaryData = new VisibleBinaryData[0];

    @FieldDefinition
    public HiddenBinaryData[] hiddenBinaryData = new HiddenBinaryData[0];

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

    public static final List<ParserInput> INPUT_WITHOUT_ROOT = ParserInput.listOf(Type.INPUT,
            InAffinityEntry.class,
            InAwareness.class,
            InThresholdAwareness.class,
            InConsumerAgentGroup.class,
            InConsumerAgentGroupAttribute.class,
            InConstantUnivariateDistribution.class,
            InRandomBoundedIntegerDistribution.class,
            InUnivariateDoubleDistribution.class,
            InConsumerAgentGroupColor.class,
            InDistanceEvaluator.class,
            InFreeNetworkTopology.class,
            InUnlinkedGraphTopology.class,
            InGraphTopologyScheme.class,
            InInverse.class,
            InNoDistance.class,
            InNumberOfTies.class,
            VisibleBinaryData.class,
            HiddenBinaryData.class,
            InOrientationSupplier.class,
            InProcessModel.class,
            InRAProcessModel.class,
            InSlopeSupplier.class,
            InProductGroup.class,
            InProductGroupAttribute.class,
            InConstantSpatialDistribution2D.class,
            InSpace2D.class,
            InSpatialDistribution.class,
            InSpatialModel.class,
            InDiscreteTimeModel.class,
            InTimeModel.class,
            //===
            InAttributeName.class,
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

    public InRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES_WITH_GRAPHVIZ;
    }

    @Override
    public AnnotationResource getResources() {
        return IOResources.getInstance();
    }

    @Override
    public void peekEdn(Sections sections, UiEdn ednType) {
//        for(Section s: sections.getList()) {
//            System.out.println("'" + s.getPriority() + "' '" + s.getLabel() + "'");
//        }
    }

    public InConsumerAgentGroup findCag(String name) {
        if(consumerAgentGroups == null) {
            throw new NoSuchElementException(name);
        }
        for(InConsumerAgentGroup cag: consumerAgentGroups) {
            if(Objects.equals(cag.getName(), name)) {
                return cag;
            }
        }
        throw new NoSuchElementException(name);
    }

    public InDistanceEvaluator findDistEval(String name) {
        if(distanceEvaluators == null) {
            throw new NoSuchElementException(name);
        }
        for(InDistanceEvaluator eval: distanceEvaluators) {
            if(Objects.equals(eval.getName(), name)) {
                return eval;
            }
        }
        throw new NoSuchElementException(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InRoot)) return false;
        InRoot root = (InRoot) o;
        return Objects.equals(general, root.general) && Arrays.equals(affinityEntries, root.affinityEntries) && Arrays.equals(consumerAgentGroups, root.consumerAgentGroups) && Arrays.equals(graphTopologySchemes, root.graphTopologySchemes) && Arrays.equals(distanceEvaluators, root.distanceEvaluators)
                && Arrays.equals(numberOfTies, root.numberOfTies)
                && Arrays.equals(processModel, root.processModel) && Arrays.equals(orientationSupplier, root.orientationSupplier) && Arrays.equals(slopeSupplier, root.slopeSupplier) && Arrays.equals(productGroups, root.productGroups) && Arrays.equals(spatialModel, root.spatialModel) && Arrays.equals(spatialDistributions, root.spatialDistributions) && Arrays.equals(timeModel, root.timeModel);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(general);
        result = 31 * result + Arrays.hashCode(affinityEntries);
        result = 31 * result + Arrays.hashCode(consumerAgentGroups);
        result = 31 * result + Arrays.hashCode(graphTopologySchemes);
        result = 31 * result + Arrays.hashCode(distanceEvaluators);
        result = 31 * result + Arrays.hashCode(numberOfTies);
        result = 31 * result + Arrays.hashCode(processModel);
        result = 31 * result + Arrays.hashCode(orientationSupplier);
        result = 31 * result + Arrays.hashCode(slopeSupplier);
        result = 31 * result + Arrays.hashCode(productGroups);
        result = 31 * result + Arrays.hashCode(spatialModel);
        result = 31 * result + Arrays.hashCode(spatialDistributions);
        result = 31 * result + Arrays.hashCode(timeModel);
        return result;
    }

    @Override
    public String toString() {
        return "InRoot{" +
                "general=" + general +
                ", affinityEntries=" + Arrays.toString(affinityEntries) +
                ", consumerAgentGroups=" + Arrays.toString(consumerAgentGroups) +
                ", graphTopologySchemes=" + Arrays.toString(graphTopologySchemes) +
                ", distanceEvaluators=" + Arrays.toString(distanceEvaluators) +
                ", numberOfTies=" + Arrays.toString(numberOfTies) +
                ", processModel=" + Arrays.toString(processModel) +
                ", orientationSupplier=" + Arrays.toString(orientationSupplier) +
                ", slopeSupplier=" + Arrays.toString(slopeSupplier) +
                ", productGroups=" + Arrays.toString(productGroups) +
                ", spatialModel=" + Arrays.toString(spatialModel) +
                ", spatialDistributions=" + Arrays.toString(spatialDistributions) +
                ", timeModel=" + Arrays.toString(timeModel) +
                '}';
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
}
