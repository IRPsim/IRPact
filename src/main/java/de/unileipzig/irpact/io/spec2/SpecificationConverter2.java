package de.unileipzig.irpact.io.spec2;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.spec2.impl.GeneralSpec;
import de.unileipzig.irpact.io.spec2.impl.VersionSpec;
import de.unileipzig.irpact.io.spec2.impl.affinity.AffinityEntrySpec;
import de.unileipzig.irpact.io.spec2.impl.agent.consumer.ConsumerAgentGroupSpec;
import de.unileipzig.irpact.io.spec2.impl.agent.consumer.GeneralConsumerAgentGroupSpec;
import de.unileipzig.irpact.io.spec2.impl.agent.consumer.PVactConsumerAgentGroupSpec;
import de.unileipzig.irpact.io.spec2.impl.binary.VisibleBinaryDataSpec;
import de.unileipzig.irpact.io.spec2.impl.distance.DistanceEvaluatorSpec;
import de.unileipzig.irpact.io.spec2.impl.distance.InverseSpec;
import de.unileipzig.irpact.io.spec2.impl.distance.NoDistanceSpec;
import de.unileipzig.irpact.io.spec2.impl.distribution.*;
import de.unileipzig.irpact.io.spec2.impl.file.FilesSpec;
import de.unileipzig.irpact.io.spec2.impl.file.PVFileSpec;
import de.unileipzig.irpact.io.spec2.impl.file.SpatialTableFileSpec;
import de.unileipzig.irpact.io.spec2.impl.network.CompleteGraphTopologySpec;
import de.unileipzig.irpact.io.spec2.impl.network.FreeNetworkTopologySpec;
import de.unileipzig.irpact.io.spec2.impl.network.GraphTopologySpec;
import de.unileipzig.irpact.io.spec2.impl.network.UnlinkedGraphTopologySpec;
import de.unileipzig.irpact.io.spec2.impl.process.*;
import de.unileipzig.irpact.io.spec2.impl.product.*;
import de.unileipzig.irpact.io.spec2.impl.spatial.Space2DSpec;
import de.unileipzig.irpact.io.spec2.impl.spatial.SpatialModelSpec;
import de.unileipzig.irpact.io.spec2.impl.spatial.dist.CustomSelectedGroupedSpatialDistribution2DSpec;
import de.unileipzig.irpact.io.spec2.impl.spatial.dist.SelectedGroupedSpatialDistribution2DSpec;
import de.unileipzig.irpact.io.spec2.impl.time.DiscreteTimeModelSpec;
import de.unileipzig.irpact.io.spec2.impl.time.TimeModelSpec;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class SpecificationConverter2 {

    protected final Map<Class<?>, ToSpecConverter2<?>> toSpecMap = new HashMap<>();

    public SpecificationConverter2() {
        initToSpecMap();
    }

    //=========================
    //ToSpec
    //=========================

    private void initToSpecMap() {
        //general
        securePut(GeneralSpec.INSTANCE);
        securePut(VersionSpec.INSTANCE);
        //affinity
        securePut(AffinityEntrySpec.INSTANCE);
        //agent.consumer
        securePut(ConsumerAgentGroupSpec.INSTANCE);
        securePut(GeneralConsumerAgentGroupSpec.INSTANCE);
        securePut(PVactConsumerAgentGroupSpec.INSTANCE);
        //binary
        securePut(VisibleBinaryDataSpec.INSTANCE);
        //distance
        securePut(DistanceEvaluatorSpec.INSTANCE);
        securePut(InverseSpec.INSTANCE);
        securePut(NoDistanceSpec.INSTANCE);
        //distribution
        securePut(BooleanDistributionSpec.INSTANCE);
        securePut(ConstantUnivariateDistributionSpec.INSTANCE);
        securePut(FiniteMassPointsDiscreteDistributionSpec.INSTANCE);
        securePut(RandomBoundedIntegerDistributionSpec.INSTANCE);
        securePut(UnivariateDoubleDistributionSpec.INSTANCE);
        //file
        securePut(FilesSpec.INSTANCE);
        securePut(PVFileSpec.INSTANCE);
        securePut(SpatialTableFileSpec.INSTANCE);
        //network
        securePut(CompleteGraphTopologySpec.INSTANCE);
        securePut(FreeNetworkTopologySpec.INSTANCE);
        securePut(GraphTopologySpec.INSTANCE);
        securePut(UnlinkedGraphTopologySpec.INSTANCE);
        //process
        securePut(IndividualAttributeBasedUncertaintyGroupAttributeSpec.INSTANCE);
        securePut(IndividualAttributeBasedUncertaintyGroupAttributeSpec.INSTANCE);
        securePut(NameBasedUncertaintyGroupAttributeSpec.INSTANCE);
        securePut(NameBasedUncertaintyWithConvergenceGroupAttributeSpec.INSTANCE);
        securePut(ProcessModelSpec.INSTANCE);
        securePut(RAProcessModelSpec.INSTANCE);
        securePut(UncertaintyGroupAttributeSpec.INSTANCE);
        //product
        securePut(FixProductFindingSchemeSpec.INSTANCE);
        securePut(FixProductSpec.INSTANCE);
        securePut(ProductFindingSchemeSpec.INSTANCE);
        securePut(ProductGroupSpec.INSTANCE);
        securePut(ProductInterestSupplySchemeSpec.INSTANCE);
        securePut(ProductThresholdInterestSupplySchemeSpec.INSTANCE);
        //spatial
        securePut(CustomSelectedGroupedSpatialDistribution2DSpec.INSTANCE);
        securePut(SelectedGroupedSpatialDistribution2DSpec.INSTANCE);
        securePut(SpatialTableFileSpec.INSTANCE);
        securePut(Space2DSpec.INSTANCE);
        securePut(SpatialModelSpec.INSTANCE);
        //time
        securePut(DiscreteTimeModelSpec.INSTANCE);
        securePut(TimeModelSpec.INSTANCE);
    }

    private <T> void securePut(ToSpecConverter2<T> converter) {
        if(toSpecMap.containsKey(converter.getParamType())) {
            throw new IllegalArgumentException("already exists: " + converter.getParamType());
        }
        toSpecMap.put(converter.getParamType(), converter);
    }

    @SuppressWarnings("unchecked")
    protected  <T> ToSpecConverter2<T> getToSpecConverter(Class<?> c) throws ParsingException {
        ToSpecConverter2<T> converter = (ToSpecConverter2<T>) toSpecMap.get(c);
        if(converter == null) {
            throw new ParsingException("no converter found: " + c.getName());
        }
        return converter;
    }

    public SpecificationData2 toSpec(InRoot root) throws ParsingException {
        SpecificationData2 data  = new SpecificationData2();
        SpecificationCache2 cache = new SpecificationCache2();
        SpecificationJob2 job = new SpecificationJob2(cache, data, this);
        executeToSpec(root, job);
        return data;
    }

    private void executeToSpec(InRoot root, SpecificationJob2 job) throws ParsingException {
        //general
        GeneralSpec.INSTANCE.toSpec(root.getGeneral(), job);
        VersionSpec.INSTANCE.toSpec(root.getVersion(), job);
        //affinity
        AffinityEntrySpec.INSTANCE.toSpec(root.getAffinityEntries(), job);
        //agent
        ConsumerAgentGroupSpec.INSTANCE.toSpec(root.getConsumerAgentGroups(), job);
        //binary
        VisibleBinaryDataSpec.INSTANCE.toSpec(root.getVisibleBinaryData(), job);
        //network
        GraphTopologySpec.INSTANCE.toSpec(root.getGraphTopologyScheme(), job);
        //process
        ProcessModelSpec.INSTANCE.toSpec(root.getProcessModels(), job);
        //products
        ProductGroupSpec.INSTANCE.toSpec(root.getProductGroups(), job);
        FixProductSpec.INSTANCE.toSpec(root.getFixProducts(), job);
        //spatial
        SpatialModelSpec.INSTANCE.toSpec(root.getSpatialModel(), job);
        //time
        TimeModelSpec.INSTANCE.toSpec(root.getTimeModel(), job);
    }

    //=========================
    //toParam
    //=========================

    public InRoot toParam(Path dataPath) throws ParsingException, IOException {
        SpecificationData2 data = new SpecificationData2();
        data.load(dataPath);
        return toParam(data);
    }

    public InRoot toParam(SpecificationData2 data) throws ParsingException {
        SpecificationCache2 cache = new SpecificationCache2();
        SpecificationJob2 job = new SpecificationJob2(cache, data, this);
        InRoot root = new InRoot();
        executeToParam(job, root);
        return root;
    }

    private void executeToParam(SpecificationJob2 job, InRoot root) throws ParsingException {
        //general
        root.setGeneral(GeneralSpec.INSTANCE.toParamArray(job));
        root.setVersion(VersionSpec.INSTANCE.toParamArray(job));
        //affinity
        root.setAffinityEntries(AffinityEntrySpec.INSTANCE.toParamArray(job));
        //agent
        root.setConsumerAgentGroups(ConsumerAgentGroupSpec.INSTANCE.toParamArray(job));
        //binary
        root.setVisibleBinaryData(VisibleBinaryDataSpec.INSTANCE.toParamArray(job));
        //network
        root.setGraphTopologyScheme(GraphTopologySpec.INSTANCE.toParamArray(job));
        //process
        root.setProcessModels(ProcessModelSpec.INSTANCE.toParamArray(job));
        //products
        root.setProductGroups(ProductGroupSpec.INSTANCE.toParamArray(job));
        root.setFixProducts(FixProductSpec.INSTANCE.toParamArray(job));
        //spatial
        root.setSpatialModel(SpatialModelSpec.INSTANCE.toParamArray(job));
        //time
        root.setTimeModel(TimeModelSpec.INSTANCE.toParamArray(job));
    }
}
