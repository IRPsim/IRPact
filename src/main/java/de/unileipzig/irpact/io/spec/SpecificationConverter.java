package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.io.input.InAttributeName;
import de.unileipzig.irpact.io.input.InGeneral;
import de.unileipzig.irpact.io.input.InRoot;
import de.unileipzig.irpact.io.input.InVersion;
import de.unileipzig.irpact.io.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.awareness.InAwareness;
import de.unileipzig.irpact.io.input.awareness.InThresholdAwareness;
import de.unileipzig.irpact.io.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.input.distribution.InRandomBoundedIntegerDistribution;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
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
import de.unileipzig.irptools.util.Util;

import java.util.*;
import java.util.function.Function;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class SpecificationConverter {

    @SuppressWarnings("rawtypes")
    protected final Map<Class, ToSpecFunction> toSpecMap = new HashMap<>();

    public SpecificationConverter() {
        initDefaults();
    }

    private void initDefaults() {
        put(InAffinityEntry.class, affinityEntryToJson);
        put(InConsumerAgentGroup.class, cagToJson);
        put(InThresholdAwareness.class, thresholdAwarenessToJson);
        put(InConstantUnivariateDistribution.class, constantUnivariateDoubleDistributionToJson);
        put(InRandomBoundedIntegerDistribution.class, randomBoundedIntegerDistributiontoJson);
        put(InInverse.class, inInverseToJson);
        put(InNoDistance.class, noDistanceToJson);
        put(InFreeNetworkTopology.class, inFreeNetworkTopologyToJson);
        put(InUnlinkedGraphTopology.class, inUnlinkedGraphToJson);
        put(InRAProcessModel.class, inRAProcessModelToJson);
        put(InOrientationSupplier.class, inOrientationSupplierToJson);
        put(InSlopeSupplier.class, inSlopeSupplierToJson);
        put(InProductGroup.class, inProductGroupToJson);
        put(InSpace2D.class, inSpace2DToJson);
        put(InConstantSpatialDistribution2D.class, inConstantSpatialDistribution2DToJson);
        put(InDiscreteTimeModel.class, inDiscreteTimeModelToJson);
        put(InGeneral.class, inGeneralToJson);
        put(InVersion.class, inVersionToJson);
    }

    public <T> void put(Class<T> c, ToSpecFunction<T> func) {
        if(toSpecMap.containsKey(c)) {
            throw new IllegalArgumentException("class '" + c.getName() + "' already exists");
        }
        toSpecMap.put(c, func);
    }

    protected void apply(SpecificationManager manager, Object[] objects) {
        for(Object obj: objects) {
            apply(manager, obj);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void apply(SpecificationManager manager, Object obj) {
        Class c = obj.getClass();
        ToSpecFunction func = toSpecMap.get(c);
        if(func == null) {
            throw new IllegalArgumentException("missing function for: " + c.getName());
        }
        func.toJson(obj, manager, this);
    }

    public SpecificationManager toSpec(InRoot inRoot) {
        SpecificationManager manager = new SpecificationManager(IRPactJson.JSON);

        //general
        apply(manager, inRoot.general);
        apply(manager, inRoot.version);
        //affinity
        apply(manager, inRoot.affinityEntries);
        //agent
        apply(manager, inRoot.consumerAgentGroups);
        //network
        apply(manager, inRoot.graphTopologySchemes);
        apply(manager, inRoot.distanceEvaluators);
        //process
        apply(manager, inRoot.processModel);
        apply(manager, inRoot.orientationSupplier);
        apply(manager, inRoot.slopeSupplier);
        //product
        apply(manager, inRoot.productGroups);
        //spatial
        apply(manager, inRoot.spatialModel);
        apply(manager, inRoot.spatialDistributions);
        //spatial
        apply(manager, inRoot.timeModel);

        return manager;
    }

    public InRoot toParam(SpecificationManager manager) {
        InRoot root = new InRoot();
        Map<String, Object> cache = new HashMap<>();

        putAll(cache, awarenessToParam.toParam(manager, null, cache), InAwareness::getName);
        putAll(cache, univariateDoubleDistributionToParam.toParam(manager, null, cache), InUnivariateDoubleDistribution::getName);

        root.distanceEvaluators = distEvalToParam.toParam(manager, root.distanceEvaluators, cache);
        putAll(cache, root.distanceEvaluators, InDistanceEvaluator::getName);
        root.consumerAgentGroups = cagToParam.toParam(manager, root.consumerAgentGroups, cache);
        putAll(cache, root.consumerAgentGroups, InConsumerAgentGroup::getName);
        root.affinityEntries = affinityEntryToParam.toParam(manager, root.affinityEntries, cache);
        root.graphTopologySchemes = topoToParam.toParam(manager, root.graphTopologySchemes, cache);
        root.processModel = processToParam.toParam(manager, root.processModel, cache);
        root.orientationSupplier = orientationSupplierToParam.toParam(manager, root.orientationSupplier, cache);
        root.slopeSupplier = slopeSupplierToParam.toParam(manager, root.slopeSupplier, cache);
        root.productGroups = productToParam.toParam(manager, root.productGroups, cache);
        root.spatialModel = spatialModelToParam.toParam(manager, root.spatialModel, cache);
        root.spatialDistributions = spatialDistToParam.toParam(manager, root.spatialDistributions, cache);
        root.timeModel = timeModelToParam.toParam(manager, root.timeModel, cache);
        root.general = inGeneralToParam.toParam(manager, root.general, cache);
        root.version = inVersionToParam.toParam(manager, root.version, cache);

        return root;
    }

    //=========================
    //toJson
    //=========================

    /**
     * @author Daniel Abitz
     */
    public interface ToSpecFunction<T> {

        void toJson(T instance, SpecificationManager manager, SpecificationConverter converter);
    }

    private static final ToSpecFunction<InAffinityEntry> affinityEntryToJson = (instance, manager, converter) -> {
        ObjectNode root = manager.getAffinities();
        ObjectNode srcNode = Util.getOrCreateObject(root, instance.getSrcCag().getName());
        srcNode.put(instance.getTarCag().getName(), instance.getAffinityValue());
    };

    private static final ToSpecFunction<InConsumerAgentGroup> cagToJson = (instance, manager, converter) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getConsumerAgentGroup(instance.getName()));
        spec.setName(instance.getName());
        spec.setNumberOfAgents(instance.getNumberOfAgents());
        spec.set(TAG_awareness, instance.getAwareness().getName());

        ArrayNode arr = spec.getAttributes();
        for(InConsumerAgentGroupAttribute inAttr: instance.getAttributes()) {
            SpecificationHelper specAttr = new SpecificationHelper(arr.addObject());
            specAttr.setName(inAttr.getCagAttrName().getName());
            specAttr.setDistribution(inAttr.getCagAttrDistribution().getName());

            converter.apply(manager, inAttr.getCagAttrDistribution());
        }

        converter.apply(manager, instance.getAwareness());
    };

    private static final ToSpecFunction<InThresholdAwareness> thresholdAwarenessToJson = (instance, manager, converter) -> {
        if(!manager.hasAwareness(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getAwareness(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("ThresholdAwareness");
            spec.setParametersValue(instance.getAwarenessThreshold());
        }
    };

    private static final ToSpecFunction<InConstantUnivariateDistribution> constantUnivariateDoubleDistributionToJson = (instance, manager, converter) -> {
        if(!manager.hasDistribution(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getDistribution(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("ConstantUnivariateDoubleDistribution");
            spec.setParametersValue(instance.getConstDistValue());
        }
    };

    private static final ToSpecFunction<InRandomBoundedIntegerDistribution> randomBoundedIntegerDistributiontoJson = (instance, manager, converter) -> {
        if(!manager.hasDistribution(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getDistribution(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("InRandomBoundedIntegerDistribution");
            spec.setParameters(TAG_lowerBound, instance.getLowerBound());
            spec.setParameters(TAG_upperBound, instance.getUpperBound());
        }
    };

    private static final ToSpecFunction<InInverse> inInverseToJson = (instance, manager, converter) -> {
        if(!manager.hasEvaluator(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getEvaluator(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("Inverse");
        }
    };

    private static final ToSpecFunction<InNoDistance> noDistanceToJson = (instance, manager, converter) -> {
        if(!manager.hasEvaluator(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getEvaluator(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("NoDistance");
        }
    };

    private static final ToSpecFunction<InFreeNetworkTopology> inFreeNetworkTopologyToJson = (instance, manager, converter) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getTopology());
        spec.setName(instance.getName());
        spec.setType("FreeNetworkTopology");
        spec.set(TAG_distanceEvaluator, instance.getDistanceEvaluator().getName());
        spec.setInitialWeight(instance.getInitialWeight());

        ObjectNode tieNode = spec.getObject(TAG_numberOfTies);
        for(InNumberOfTies tie: instance.getNumberOfTies()) {
            tieNode.put(tie.getCag().getName(), tie.getCount());
        }

        converter.apply(manager, instance.getDistanceEvaluator());
    };

    private static final ToSpecFunction<InUnlinkedGraphTopology> inUnlinkedGraphToJson = (instance, manager, converter) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getTopology());
        spec.setName(instance.getName());
        spec.setType("UnlinkedGraphTopology");
    };

    private static final ToSpecFunction<InRAProcessModel> inRAProcessModelToJson = (instance, manager, converter) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getProcess());
        spec.setName(instance.getName());
        spec.setType("RAProcessModel");
        spec.set(TAG_a, instance.getA());
        spec.set(TAG_b, instance.getB());
        spec.set(TAG_c, instance.getC());
        spec.set(TAG_d, instance.getD());
        spec.set(TAG_adopterPoints, instance.getAdopterPoints());
        spec.set(TAG_interestedPoints, instance.getInterestedPoints());
        spec.set(TAG_awarePoints, instance.getAwarePoints());
        spec.set(TAG_unknownPoints, instance.getUnknownPoints());
    };

    private static final ToSpecFunction<InOrientationSupplier> inOrientationSupplierToJson = (instance, manager, converter) -> {
        SpecificationHelper processSpec = new SpecificationHelper(manager.getProcess());
        SpecificationHelper spec = processSpec.getObjectSpec(TAG_orientation);
        spec.setName(instance.getName());
        spec.setDistribution(instance.getDistribution().getName());

        converter.apply(manager, instance.getDistribution());
    };

    private static final ToSpecFunction<InSlopeSupplier> inSlopeSupplierToJson = (instance, manager, converter) -> {
        SpecificationHelper processSpec = new SpecificationHelper(manager.getProcess());
        SpecificationHelper spec = processSpec.getObjectSpec(TAG_slope);
        spec.setName(instance.getName());
        spec.setDistribution(instance.getDistribution().getName());

        converter.apply(manager, instance.getDistribution());
    };

    private static final ToSpecFunction<InProductGroup> inProductGroupToJson = (instance, manager, converter) -> {
        if(!manager.hasProductGroup(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getProductGroup(instance.getName()));
            spec.setName(instance.getName());

            ArrayNode arr = spec.getAttributes();
            for(InProductGroupAttribute inAttr: instance.getAttributes()) {
                SpecificationHelper specAttr = new SpecificationHelper(arr.addObject());
                specAttr.setName(inAttr.getAttrName().getName());
                specAttr.setDistribution(inAttr.getAttrDistribution().getName());

                converter.apply(manager, inAttr.getAttrDistribution());
            }
        }
    };

    private static final ToSpecFunction<InSpace2D> inSpace2DToJson = (instance, manager, converter) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getSpatial());
        spec.setName(instance.getName());
        spec.setType("Space2D");
        spec.setParametersValue(
                instance.useEuclid() ? TAG_euclid : TAG_manhatten
        );
    };

    private static final ToSpecFunction<InConstantSpatialDistribution2D> inConstantSpatialDistribution2DToJson = (instance, manager, converter) -> {
        SpecificationHelper cagSpec = new SpecificationHelper(manager.getConsumerAgentGroup(instance.getConsumerAgentGroup().getName()));
        cagSpec.set(TAG_spatialDistribution, instance.getName());

        if(!manager.hasSpatialDistribution(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getSpatialDistribution(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("ConstantSpatialDistribution2D");
            spec.setParameters(TAG_x, instance.getX());
            spec.setParameters(TAG_y, instance.getY());
        }
    };

    private static final ToSpecFunction<InDiscreteTimeModel> inDiscreteTimeModelToJson = (instance, manager, converter) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getTime());
        spec.setName(instance.getName());
        spec.setType("DiscreteTimeModel");
        spec.set(TAG_timePerTickInMs, instance.getTimePerTickInMs());
    };

    private static final ToSpecFunction<InGeneral> inGeneralToJson = (instance, manager, converter) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getGeneralSettings());
        spec.set(TAG_seed, instance.seed);
        spec.set(TAG_timeout, instance.timeout);
        spec.set(TAG_startYear, instance.startYear);
        spec.set(TAG_endYear, instance.endYear);
    };

    private static final ToSpecFunction<InVersion> inVersionToJson = (instance, manager, converter) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getGeneralSettings());
        spec.set(TAG_version, instance.getVersion());
    };

    //=========================
    //fromJson
    //=========================

    /**
     * @author Daniel Abitz
     */
    public interface ToParamFunction<T> {

        T toParam(SpecificationManager manager, T instance, Map<String, Object> cache);
    }

    @SuppressWarnings("unchecked")
    private static <T> T find(Map<String, Object> cache, String key) {
        if(cache.containsKey(key)) {
            return (T) cache.get(key);
        } else {
            throw new NoSuchElementException(key);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T putIfMissing(Map<String, Object> cache, String key, T value) {
        if(cache.containsKey(key)) {
            return (T) cache.get(key);
        } else {
            cache.put(key, value);
            return value;
        }
    }

    private static <T> void putAll(Map<String, Object> cache, T[] arr, Function<T, String> nameFunc) {
        for(T t: arr) {
            String key = nameFunc.apply(t);
            if(cache.containsKey(key)) {
                throw new IllegalArgumentException("key '" + key + "' already exists");
            } else {
                cache.put(key, t);
            }
        }
    }

    private static final ToParamFunction<InAffinityEntry[]> affinityEntryToParam = (manager, instance, cache) -> {
        List<InAffinityEntry> entryList = new ArrayList<>();
        ObjectNode affinityRoot = manager.getAffinities();
        for(Map.Entry<String, JsonNode> srcEntry: Util.iterateFields(affinityRoot)) {
            InConsumerAgentGroup srcCag = find(cache, srcEntry.getKey());
            for(Map.Entry<String, JsonNode> tarEntry: Util.iterateFields(srcEntry.getValue())) {
                InConsumerAgentGroup tarCag = find(cache, tarEntry.getKey());
                double value = tarEntry.getValue().doubleValue();
                InAffinityEntry entry = new InAffinityEntry(srcCag.getName() + "_" + tarCag.getName(), srcCag, tarCag, value);
                entryList.add(entry);
            }
        }
        return entryList.toArray(new InAffinityEntry[0]);
    };

    private static final ToParamFunction<InConsumerAgentGroup[]> cagToParam = (manager, instance, cache) -> {
        List<InConsumerAgentGroup> cagList = new ArrayList<>();
        for(Map.Entry<String, ObjectNode> cagEntry: manager.cagMap.entrySet()) {
            SpecificationHelper spec = new SpecificationHelper(cagEntry.getValue());
            String cagName = spec.getName();
            int numberOfAgents = spec.getInt(TAG_numberOfAgents);
            InAwareness awareness = find(cache, spec.getText(TAG_awareness));

            List<InConsumerAgentGroupAttribute> attrList = new ArrayList<>();
            for(JsonNode attrNode: Util.iterateElements(spec.getAttributes())) {
                SpecificationHelper attrSpec = new SpecificationHelper(attrNode);
                String attrName = cagName + "_" + attrSpec.getName();
                InAttributeName inName = putIfMissing(cache, attrSpec.getName(), new InAttributeName(attrSpec.getName()));
                InUnivariateDoubleDistribution attrDist = find(cache, attrSpec.getText(TAG_distribution));
                InConsumerAgentGroupAttribute cagAttr = new InConsumerAgentGroupAttribute(
                        attrName,
                        inName,
                        attrDist
                );
                attrList.add(cagAttr);
            }

            InConsumerAgentGroup cag = new InConsumerAgentGroup(
                    cagName,
                    1.0, //!!!
                    numberOfAgents,
                    attrList,
                    awareness
            );
            cagList.add(cag);
        }

        return cagList.toArray(new InConsumerAgentGroup[0]);
    };

    private static final ToParamFunction<InAwareness[]> awarenessToParam = (manager, instance, cache) -> {
        List<InAwareness> list = new ArrayList<>();
        for(Map.Entry<String, ObjectNode> entry: manager.awarenessMap.entrySet()) {
            SpecificationHelper spec = new SpecificationHelper(entry.getValue());
            String name = spec.getName();
            String type = spec.getType();

            if("ThresholdAwareness".equals(type)) {
                double threshold = spec.getParametersSpec().getDouble(TAG_value);
                InThresholdAwareness awa = new InThresholdAwareness(name, threshold);
                list.add(awa);
            }
            else {
                throw new IllegalArgumentException("unknown type: " + type);
            }
        }

        return list.toArray(new InAwareness[0]);
    };

    private static final ToParamFunction<InUnivariateDoubleDistribution[]> univariateDoubleDistributionToParam = (manager, instance, cache) -> {
        List<InUnivariateDoubleDistribution> list = new ArrayList<>();
        for(Map.Entry<String, ObjectNode> entry: manager.distributionMap.entrySet()) {
            SpecificationHelper spec = new SpecificationHelper(entry.getValue());
            String name = spec.getName();
            String type = spec.getType();

            InUnivariateDoubleDistribution dist;
            if("ConstantUnivariateDoubleDistribution".equals(type)) {
                double value = spec.getParametersSpec().getDouble(TAG_value);
                dist = new InConstantUnivariateDistribution(name, value);
            }
            else if("InRandomBoundedIntegerDistribution".equals(type)) {
                int lower = spec.getParametersSpec().getInt(TAG_lowerBound);
                int upper = spec.getParametersSpec().getInt(TAG_upperBound);
                dist = new InRandomBoundedIntegerDistribution(
                        name,
                        lower,
                        upper
                );
            }
            else {
                throw new IllegalArgumentException("unknown type: " + type);
            }
            list.add(dist);
        }

        return list.toArray(new InUnivariateDoubleDistribution[0]);
    };

    private static final ToParamFunction<InDistanceEvaluator[]> distEvalToParam = (manager, instance, cache) -> {
        List<InDistanceEvaluator> list = new ArrayList<>();
        for(Map.Entry<String, ObjectNode> entry: manager.evalMap.entrySet()) {
            SpecificationHelper spec = new SpecificationHelper(entry.getValue());
            String name = spec.getName();
            String type = spec.getType();

            InDistanceEvaluator dist;
            if("Inverse".equals(type)) {
                dist = new InInverse(name);
            }
            else if("NoDistance".equals(type)) {
                dist = new InNoDistance(name);
            }
            else {
                throw new IllegalArgumentException("unknown type: " + type);
            }
            list.add(dist);
        }

        return list.toArray(new InDistanceEvaluator[0]);
    };

    private static final ToParamFunction<InGraphTopologyScheme[]> topoToParam = (manager, instance, cache) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getTopology());
        String name = spec.getName();
        String type = spec.getType();

        InGraphTopologyScheme scheme;
        if("FreeNetworkTopology".equals(type)) {
            double initialWeight = spec.getDouble(TAG_initialWeight);
            InDistanceEvaluator eval = find(cache, spec.getText(TAG_distanceEvaluator));
            List<InNumberOfTies> tieList = new ArrayList<>();
            ObjectNode tieRoot = spec.getObject(TAG_numberOfTies);
            for(Map.Entry<String, JsonNode> tieNode: Util.iterateFields(tieRoot)) {
                String cagName = tieNode.getKey();
                int count = tieNode.getValue().intValue();
                InConsumerAgentGroup cag = find(cache, cagName);
                InNumberOfTies tie = new InNumberOfTies("tie_" + cagName, cag, count);
                tieList.add(tie);
            }
            scheme = new InFreeNetworkTopology(
                    name,
                    eval,
                    tieList.toArray(new InNumberOfTies[0]),
                    initialWeight
            );
        }
        else if("UnlinkedGraphTopology".equals(type)) {
            scheme = new InUnlinkedGraphTopology(name);
        }
        else {
            throw new IllegalArgumentException("unknown type: " + type);
        }

        return new InGraphTopologyScheme[]{scheme};
    };

    private static final ToParamFunction<InProcessModel[]> processToParam = (manager, instance, cache) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getProcess());
        String name = spec.getName();
        String type = spec.getType();

        InProcessModel model;
        if("RAProcessModel".equals(type)) {
            model = new InRAProcessModel(
                    name,
                    spec.getDouble(TAG_a),
                    spec.getDouble(TAG_b),
                    spec.getDouble(TAG_c),
                    spec.getDouble(TAG_d),
                    spec.getInt(TAG_adopterPoints),
                    spec.getInt(TAG_interestedPoints),
                    spec.getInt(TAG_awarePoints),
                    spec.getInt(TAG_unknownPoints)
            );
        }
        else {
            throw new IllegalArgumentException("unknown type: " + type);
        }

        return new InProcessModel[]{model};
    };

    private static final ToParamFunction<InOrientationSupplier[]> orientationSupplierToParam = (manager, instance, cache) -> {
        SpecificationHelper processSpec = new SpecificationHelper(manager.getProcess());
        String type = processSpec.getType();

        if("RAProcessModel".equals(type)) {
            SpecificationHelper spec = processSpec.getObjectSpec(TAG_orientation);
            String name = spec.getName();
            InUnivariateDoubleDistribution dist = find(cache, spec.getText(TAG_distribution));

            return new InOrientationSupplier[]{
                    new InOrientationSupplier(name, dist)
            };
        }
        else {
            return new InOrientationSupplier[0];
        }
    };

    private static final ToParamFunction<InSlopeSupplier[]> slopeSupplierToParam = (manager, instance, cache) -> {
        SpecificationHelper processSpec = new SpecificationHelper(manager.getProcess());
        String type = processSpec.getType();

        if("RAProcessModel".equals(type)) {
            SpecificationHelper spec = processSpec.getObjectSpec(TAG_slope);
            String name = spec.getName();
            InUnivariateDoubleDistribution dist = find(cache, spec.getText(TAG_distribution));

            return new InSlopeSupplier[]{
                    new InSlopeSupplier(name, dist)
            };
        }
        else {
            return new InSlopeSupplier[0];
        }
    };

    private static final ToParamFunction<InProductGroup[]> productToParam = (manager, instance, cache) -> {
        List<InProductGroup> cagList = new ArrayList<>();
        for(Map.Entry<String, ObjectNode> productEntry: manager.productMap.entrySet()) {
            SpecificationHelper spec = new SpecificationHelper(productEntry.getValue());
            String productName = spec.getName();

            List<InProductGroupAttribute> attrList = new ArrayList<>();
            for(JsonNode attrNode: Util.iterateElements(spec.getAttributes())) {
                SpecificationHelper attrSpec = new SpecificationHelper(attrNode);
                String attrName = productName + "_" + attrSpec.getName();
                InAttributeName inName = putIfMissing(cache, attrSpec.getName(), new InAttributeName(attrSpec.getName()));
                InUnivariateDoubleDistribution attrDist = find(cache, attrSpec.getText(TAG_distribution));
                InProductGroupAttribute cagAttr = new InProductGroupAttribute(
                        attrName,
                        inName,
                        attrDist
                );
                attrList.add(cagAttr);
            }

            InProductGroup cag = new InProductGroup(
                    productName,
                    attrList.toArray(new InProductGroupAttribute[0])
            );
            cagList.add(cag);
        }

        return cagList.toArray(new InProductGroup[0]);
    };

    private static final ToParamFunction<InSpatialModel[]> spatialModelToParam = (manager, instance, cache) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getSpatial());
        String name = spec.getName();
        String type = spec.getType();

        InSpatialModel model;
        if("Space2D".equals(type)) {
            String metric = spec.getParametersSpec().getText(TAG_value);
            model = new InSpace2D(
                    name,
                    TAG_euclid.equals(metric)
            );
        }
        else {
            throw new IllegalArgumentException("unknown type: " + type);
        }

        return new InSpatialModel[]{model};
    };

    private static List<InConsumerAgentGroup> findCagsWithSpatialDist(
            String distName,
            SpecificationManager manager,
            Map<String, Object> cache) {
        List<InConsumerAgentGroup> list = new ArrayList<>();
        for(Map.Entry<String, ObjectNode> cagEntry: manager.cagMap.entrySet()) {
            SpecificationHelper spec = new SpecificationHelper(cagEntry.getValue());
            String cagName = spec.getName();
            String cagDist = spec.getText(TAG_spatialDistribution);
            if(Objects.equals(distName, cagDist)) {
                InConsumerAgentGroup cag = find(cache, cagName);
                list.add(cag);
            }
        }
        return list;
    }

    private static final ToParamFunction<InSpatialDistribution[]> spatialDistToParam = (manager, instance, cache) -> {
        List<InSpatialDistribution> distList = new ArrayList<>();
        for(Map.Entry<String, ObjectNode> entry: manager.spatialDistributionMap.entrySet()) {
            SpecificationHelper spec = new SpecificationHelper(entry.getValue());
            String name = spec.getName();
            String type = spec.getType();

            if("ConstantSpatialDistribution2D".equals(type)) {
                double x = spec.getParametersSpec().getDouble(TAG_x);
                double y = spec.getParametersSpec().getDouble(TAG_y);
                List<InConsumerAgentGroup> cagList = findCagsWithSpatialDist(name, manager, cache);
                if(cagList.size() != 1) {
                    throw new IllegalArgumentException("cagList.size != 1");
                }
                InConstantSpatialDistribution2D dist = new InConstantSpatialDistribution2D(
                        name,
                        cagList.get(0),
                        x,
                        y
                );
                distList.add(dist);
            }
            else {
                throw new IllegalArgumentException("unknown type: " + type);
            }
        }

        return distList.toArray(new InSpatialDistribution[0]);
    };

    private static final ToParamFunction<InTimeModel[]> timeModelToParam = (manager, instance, cache) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getTime());
        String name = spec.getName();
        String type = spec.getType();

        InTimeModel model;
        if("DiscreteTimeModel".equals(type)) {
            long timePerTickInMs = spec.getLong(TAG_timePerTickInMs);
            model = new InDiscreteTimeModel(
                    name,
                    timePerTickInMs
            );
        }
        else {
            throw new IllegalArgumentException("unknown type: " + type);
        }

        return new InTimeModel[]{model};
    };

    private static final ToParamFunction<InGeneral> inGeneralToParam = (manager, instance, cache) -> {
        InGeneral general = instance == null ? new InGeneral() : instance;
        SpecificationHelper spec = new SpecificationHelper(manager.getGeneralSettings());
        general.seed = spec.getLong(TAG_seed);
        general.timeout = spec.getLong(TAG_timeout);
        general.startYear = spec.getInt(TAG_startYear);
        general.endYear = spec.getInt(TAG_endYear);
        return general;
    };

    private static final ToParamFunction<InVersion[]> inVersionToParam = (manager, instances, cache) -> {
        SpecificationHelper spec = new SpecificationHelper(manager.getGeneralSettings());
        InVersion version = new InVersion(
                spec.getText(TAG_version)
        );
        return new InVersion[]{version};
    };
}
