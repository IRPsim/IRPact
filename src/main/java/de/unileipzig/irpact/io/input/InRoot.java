package de.unileipzig.irpact.io.input;

import de.unileipzig.irpact.io.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.input.network.*;
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
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irptools.defstructure.AnnotationResource;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.def.*;
import de.unileipzig.irptools.util.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(root = true)
public class InRoot implements RootClass {

    //=========================
    //general
    //=========================

    @FieldDefinition
    public InGeneral general;

    //=========================
    //affinity
    //=========================

    @FieldDefinition
    public InAffinityEntry[] affinityEntries;

    //=========================
    //agent
    //=========================

    @FieldDefinition
    public InConsumerAgentGroup[] consumerAgentGroups;

    //=========================
    //network
    //=========================

    @FieldDefinition
    public InGraphTopologyScheme[] graphTopologySchemes;

    @FieldDefinition
    public InDistanceEvaluator[] distanceEvaluators;

    @FieldDefinition
    public InNumberOfTies[] numberOfTies;

    //=========================
    //process
    //=========================

    @FieldDefinition
    public InProcessModel[] processModel;

    @FieldDefinition
    public InOrientationSupplier[] orientationSupplier;

    @FieldDefinition
    public InSlopeSupplier[] slopeSupplier;

    //=========================
    //product
    //=========================

    @FieldDefinition
    public InProductGroup[] productGroups;

    //=========================
    //spatial
    //=========================

    @FieldDefinition
    public InSpatialModel[] spatialModel;

    @FieldDefinition
    public InSpatialDistribution[] spatialDistributions;

    //=========================
    //time
    //=========================

    @FieldDefinition
    public InTimeModel[] timeModel;

    //=========================
    //Graphviz
    //=========================

    @FieldDefinition
    public InConsumerAgentGroupColor[] consumerAgentGroupColors;

    @FieldDefinition
    public GraphvizLayoutAlgorithm[] layoutAlgorithms;

    @FieldDefinition
    public GraphvizOutputFormat[] outputFormats;

    @FieldDefinition
    public GraphvizColor[] colors;

    @FieldDefinition
    public GraphvizGlobal graphvizGlobal;

    //=========================
    //OLD DATA
    //=========================

    @FieldDefinition()
    public InGlobal global;

    @FieldDefinition
    public Sector[] sectors;

    @FieldDefinition
    public SideCustom[] customs;

    @FieldDefinition
    public SideFares[] fares;

    @FieldDefinition
    public LoadDSE[] dse;

    @FieldDefinition
    public TechDESES[] deses;

    @FieldDefinition
    public TechDESPV[] despv;

    //==================================================

    public static final List<ParserInput> INPUT = ParserInput.listOf(Type.INPUT,
            InAffinityEntry.class,
            InConsumerAgentGroup.class,
            InConsumerAgentGroupAttribute.class,
            InConstantUnivariateDistribution.class,
            InUnivariateDoubleDistribution.class,
            InDistanceEvaluator.class,
            InFreeNetworkTopology.class,
            InGraphTopologyScheme.class,
            InInverse.class,
            InNoDistance.class,
            InNumberOfTies.class,
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
            InRoot.class,
            //===
            InConsumerAgentGroupColor.class,
            GraphvizColor.class,
            GraphvizLayoutAlgorithm.class,
            GraphvizOutputFormat.class,
            GraphvizGlobal.class
    );

    public static final List<ParserInput> MERGED_INPUT = Util.mergedArrayListOf(
            de.unileipzig.irpact.start.optact.in.InRoot.CLASSES_WITHOUT_ROOT,
            INPUT
    );

    private static final AnnotationResource RES = GraphvizResource.DEFAULT;

    public InRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return MERGED_INPUT;
    }

    @Override
    public AnnotationResource getResources() {
        return RES;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InRoot)) return false;
        InRoot root = (InRoot) o;
        return Objects.equals(general, root.general) && Arrays.equals(affinityEntries, root.affinityEntries) && Arrays.equals(consumerAgentGroups, root.consumerAgentGroups) && Arrays.equals(graphTopologySchemes, root.graphTopologySchemes) && Arrays.equals(distanceEvaluators, root.distanceEvaluators) && Arrays.equals(numberOfTies, root.numberOfTies) && Arrays.equals(processModel, root.processModel) && Arrays.equals(orientationSupplier, root.orientationSupplier) && Arrays.equals(slopeSupplier, root.slopeSupplier) && Arrays.equals(productGroups, root.productGroups) && Arrays.equals(spatialModel, root.spatialModel) && Arrays.equals(spatialDistributions, root.spatialDistributions) && Arrays.equals(timeModel, root.timeModel);
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
}
