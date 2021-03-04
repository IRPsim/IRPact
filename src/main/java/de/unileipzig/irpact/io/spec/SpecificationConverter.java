package de.unileipzig.irpact.io.spec;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.param.input.product.InFixProduct;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.spec.impl.GeneralSpec;
import de.unileipzig.irpact.io.spec.impl.VersionSpec;
import de.unileipzig.irpact.io.spec.impl.affinity.ComplexAffinityEntrySpec;
import de.unileipzig.irpact.io.spec.impl.agent.consumer.ConsumerAgentGroupSpec;
import de.unileipzig.irpact.io.spec.impl.binary.VisibleBinaryDataSpec;
import de.unileipzig.irpact.io.spec.impl.distribution.*;
import de.unileipzig.irpact.io.spec.impl.file.FilesSpec;
import de.unileipzig.irpact.io.spec.impl.file.PVFileSpec;
import de.unileipzig.irpact.io.spec.impl.file.SpatialTableFileSpec;
import de.unileipzig.irpact.io.spec.impl.interest.ProductThresholdInterestSupplySchemeSpec;
import de.unileipzig.irpact.io.spec.impl.network.FreeNetworkTopologySpec;
import de.unileipzig.irpact.io.spec.impl.network.GraphTopologySpec;
import de.unileipzig.irpact.io.spec.impl.network.UnlinkedGraphTopologySpec;
import de.unileipzig.irpact.io.spec.impl.process.CustomUncertaintyGroupAttributeSpec;
import de.unileipzig.irpact.io.spec.impl.process.ProcessModelSpec;
import de.unileipzig.irpact.io.spec.impl.process.RAProcessModelSpec;
import de.unileipzig.irpact.io.spec.impl.process.UncertaintyGroupAttributeSpec;
import de.unileipzig.irpact.io.spec.impl.product.FixProductFindingSchemeSpec;
import de.unileipzig.irpact.io.spec.impl.product.FixProductSpec;
import de.unileipzig.irpact.io.spec.impl.product.ProductGroupSpec;
import de.unileipzig.irpact.io.spec.impl.spatial.Space2DSpec;
import de.unileipzig.irpact.io.spec.impl.spatial.SpatialSpec;
import de.unileipzig.irpact.io.spec.impl.spatial.dist.CustomSelectedGroupedSpatialDistribution2DSpec;
import de.unileipzig.irpact.io.spec.impl.spatial.dist.CustomSpatialDistribution2DSpec;
import de.unileipzig.irpact.io.spec.impl.time.DiscreteTimeModelSpec;
import de.unileipzig.irpact.io.spec.impl.time.TimeModelSpec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class SpecificationConverter {

    public SpecificationConverter() {
        init();
    }

    private void init() {
        initSpecMap();
    }

    //=========================
    //general
    //=========================

    @SuppressWarnings("ConstantConditions")
    public SpecificationManager toSpec(InRoot root) throws ParsingException {
        boolean inline = false;
        SpecificationManager manager = new SpecificationManager();

        //general
        GeneralSpec.INSTANCE.toSpec(root.general, manager, this, inline);
        VersionSpec.INSTANCE.toSpec(root.version, manager, this, inline);
        //affinity
        ComplexAffinityEntrySpec.INSTANCE.toSpec(root.affinityEntries, manager, this, inline);
        //agent
        ConsumerAgentGroupSpec.INSTANCE.toSpec(root.consumerAgentGroups, manager, this, inline);
        //network
        GraphTopologySpec.INSTANCE.toSpec(root.graphTopologySchemes, manager, this, inline);
        //process
        ProcessModelSpec.INSTANCE.toSpec(root.processModel, manager, this, inline);
        UncertaintyGroupAttributeSpec.INSTANCE.toSpec(root.uncertaintyGroupAttributes, manager, this, inline);
        //product
        ProductGroupSpec.INSTANCE.toSpec(root.productGroups, manager, this, inline);
        FixProductSpec.INSTANCE.toSpec(root.fixProducts, manager, this, inline);
        //spatial
        SpatialSpec.INSTANCE.toSpec(root.spatialModel, manager, this, inline);
        //time
        TimeModelSpec.INSTANCE.toSpec(root.timeModel, manager, this, inline);
        //binary
        VisibleBinaryDataSpec.INSTANCE.toSpec(root.visibleBinaryData, manager, this, inline);

        return manager;
    }

    public InRoot toParam(SpecificationManager manager) throws ParsingException {
        SpecificationCache cache = new SpecificationCache();
        InRoot root = new InRoot();

        //general
        root.general = GeneralSpec.INSTANCE.toParam(manager, this, cache)[0];
        root.version = VersionSpec.INSTANCE.toParam(manager, this, cache);
        //affinity
        root.affinityEntries = ComplexAffinityEntrySpec.INSTANCE.toParam(manager, this, cache);
        //agent
        root.consumerAgentGroups = ConsumerAgentGroupSpec.INSTANCE.toParam(manager, this, cache);
        //network
        root.graphTopologySchemes = GraphTopologySpec.INSTANCE.toParam(manager, this, cache);
        //process
        root.processModel = ProcessModelSpec.INSTANCE.toParam(manager, this, cache);
        root.uncertaintyGroupAttributes = UncertaintyGroupAttributeSpec.INSTANCE.toParam(manager, this, cache);
        //products
        root.productGroups = ProductGroupSpec.INSTANCE.toParam(manager, this, cache);
        root.fixProducts = FixProductSpec.INSTANCE.toParam(manager, this, cache);
        //spatial
        root.spatialModel = SpatialSpec.INSTANCE.toParam(manager, this, cache);
        //time
        root.timeModel = TimeModelSpec.INSTANCE.toParam(manager, this, cache);
        //binary
        root.visibleBinaryData = VisibleBinaryDataSpec.INSTANCE.toParam(manager, this, cache);

        return root;
    }

    //=========================
    //ToSpec
    //=========================

    protected final Map<Class<?>, ToSpecConverter<?>> toSpecMap = new HashMap<>();

    private void initSpecMap() {
        securePut(ComplexAffinityEntrySpec.INSTANCE);
        securePut(ConsumerAgentGroupSpec.INSTANCE);
        securePut(VisibleBinaryDataSpec.INSTANCE);
        securePut(BooleanDistributionSpec.INSTANCE);
        securePut(ConstantUnivariateDistributionSpec.INSTANCE);
        securePut(FiniteMassPointsDiscreteDistributionSpec.INSTANCE);
        securePut(RandomBoundedIntegerDistributionSpec.INSTANCE);
        //securePut(UnivariateDoubleDistributionSpec.INSTANCE);
        //securePut(FilesSpec.INSTANCE);
        securePut(PVFileSpec.INSTANCE);
        securePut(SpatialTableFileSpec.INSTANCE);
        //securePut(ProductInterestSupplySchemeSpec.INSTANCE);
        securePut(ProductThresholdInterestSupplySchemeSpec.INSTANCE);
        securePut(FreeNetworkTopologySpec.INSTANCE);
        securePut(GraphTopologySpec.INSTANCE);
        securePut(UnlinkedGraphTopologySpec.INSTANCE);
        securePut(CustomUncertaintyGroupAttributeSpec.INSTANCE);
        //securePut(ProcessModelSpec.INSTANCE);
        securePut(RAProcessModelSpec.INSTANCE);
        //securePut(UncertaintyGroupAttributeSpec.INSTANCE);
        securePut(FixProductFindingSchemeSpec.INSTANCE);
        securePut(FixProductSpec.INSTANCE);
        //securePut(ProductFindingSchemeSpec.INSTANCE);
        securePut(ProductGroupSpec.INSTANCE);
        securePut(CustomSelectedGroupedSpatialDistribution2DSpec.INSTANCE);
        securePut(CustomSpatialDistribution2DSpec.INSTANCE);
        //securePut(SpatialDistributionSpec.INSTANCE);
        securePut(Space2DSpec.INSTANCE);
        securePut(SpatialSpec.INSTANCE);
        securePut(DiscreteTimeModelSpec.INSTANCE);
        securePut(GeneralSpec.INSTANCE);
        securePut(VersionSpec.INSTANCE);
    }

    private <T> void securePut(ToSpecConverter<T> converter) {
        if(toSpecMap.containsKey(converter.getParamType())) {
            throw new IllegalArgumentException("already exists: " + converter.getParamType());
        }
        toSpecMap.put(converter.getParamType(), converter);
    }

    @SuppressWarnings("unchecked")
    public <T> ToSpecConverter<T> getToSpecConverter(Class<?> c) {
        ToSpecConverter<?> converter = toSpecMap.get(c);
        if(converter == null) {
            throw new IllegalArgumentException("missing converter: " + c.getName());
        }
        return (ToSpecConverter<T>) converter;
    }

    public <T> void callToSpec(T input, SpecificationManager manager, boolean inline) throws ParsingException {
        Class<?> c = input.getClass();
        ToSpecConverter<T> converter = getToSpecConverter(c);
        converter.toSpec(input, manager, this, inline);
    }

    //=========================
    //ToParam
    //=========================

    public InProductGroup getProductGroup(
            String name,
            SpecificationManager manager,
            SpecificationCache cache) throws ParsingException {
        return getByName(
                name,
                manager,
                cache,
                ProductGroupSpec.INSTANCE,
                InProductGroup::getName
        );
    }

    public InFixProduct getFixProduct(
            String name,
            SpecificationManager manager,
            SpecificationCache cache) throws ParsingException {
        return getByName(
                name,
                manager,
                cache,
                FixProductSpec.INSTANCE,
                InFixProduct::getName
        );
    }

    public InConsumerAgentGroup getConsumerAgentGroup(
            String name,
            SpecificationManager manager,
            SpecificationCache cache) throws ParsingException {
        return getByName(
                name,
                manager,
                cache,
                ConsumerAgentGroupSpec.INSTANCE,
                InConsumerAgentGroup::getName
        );
    }

    public InUnivariateDoubleDistribution getDistribution(
            String name,
            SpecificationManager manager,
            SpecificationCache cache) throws ParsingException {
        return getByName(
                name,
                manager,
                cache,
                UnivariateDoubleDistributionSpec.INSTANCE,
                InUnivariateDoubleDistribution::getName
        );
    }

    public InAttributeName getName(String name, SpecificationCache cache) {
        return cache.getAttrName(name);
    }

    public InFile getFile(
            String name,
            SpecificationManager manager,
            SpecificationCache cache) throws ParsingException {
        return getByName(
                name,
                manager,
                cache,
                FilesSpec.INSTANCE,
                InFile::getName
        );
    }

    public  <R> R getByName(
            String name,
            SpecificationManager manager,
            SpecificationCache cache,
            ToParamConverter<R> toParam,
            Function<? super R, ? extends String> toString) throws ParsingException {
        if(cache.has(name)) {
            return cache.getAs(name);
        }

        R[] dists = toParam.toParam(manager, this, cache);
        R result = null;
        for(R dist: dists) {
            String n = toString.apply(dist);
            if(!cache.has(n)) {
                cache.securePut(n, dist);
            }
            if(n.equals(name)) {
                result = dist;
            }
        }

        if(result == null) {
            throw new NullPointerException("name not found: " + name);
        }
        return result;
    }
}
