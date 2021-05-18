package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.agent.consumer.InNameSplitConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.network.InDistanceEvaluator;
import de.unileipzig.irpact.io.param.input.process.ra.InUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.param.input.product.InFixProduct;
import de.unileipzig.irpact.io.param.input.product.InProductFindingScheme;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.spec.impl.agent.consumer.ConsumerAgentGroupSpec;
import de.unileipzig.irpact.io.spec.impl.distance.DistanceEvaluatorSpec;
import de.unileipzig.irpact.io.spec.impl.distribution.UnivariateDoubleDistributionSpec;
import de.unileipzig.irpact.io.spec.impl.file.FilesSpec;
import de.unileipzig.irpact.io.spec.impl.process.UncertaintyGroupAttributeSpec;
import de.unileipzig.irpact.io.spec.impl.product.FixProductSpec;
import de.unileipzig.irpact.io.spec.impl.product.ProductFindingSchemeSpec;
import de.unileipzig.irpact.io.spec.impl.product.ProductGroupSpec;
import de.unileipzig.irpact.io.spec.impl.product.ProductInterestSupplySchemeSpec;
import de.unileipzig.irpact.io.spec.impl.spatial.dist.SpatialDistributionSpec;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author Daniel Abitz
 */
public class SpecificationJob {

    protected final MultiCounter counter = new MultiCounter();
    protected JsonNodeCreator creator = IRPactJson.JSON.getNodeFactory();
    protected SpecificationCache cache;
    protected SpecificationData data;
    protected SpecificationConverter converter;

    protected Set<Class<?>> doInline = new HashSet<>();

    public SpecificationJob(
            SpecificationCache cache,
            SpecificationData data,
            SpecificationConverter converter) {
        this.cache = cache;
        this.data = data;
        this.converter = converter;
    }

    //=========================
    //
    //=========================

    public MultiCounter getCounter() {
        return counter;
    }

    public SpecificationCache getCache() {
        return cache;
    }

    public SpecificationConverter getConverter() {
        return converter;
    }

    public SpecificationData getData() {
        return data;
    }

    public boolean isInline(Class<?> c) {
        return doInline.contains(c);
    }

    //=========================
    //util
    //=========================

    public int getAndInc(String key) {
        return counter.getAndInc(key);
    }

    public boolean isCached(String key) {
        return cache.has(key);
    }

    public <T> T getCached(String key) {
        return cache.getAs(key);
    }

    public void cacheIfNotExists(String key, Object value) {
        cache.putIfNotExists(key, value);
    }

    public void cache(String key, Object value) {
        cache.securePut(key, value);
    }

    public <T> void callToSpec(T input) throws ParsingException {
        Class<?> c = input.getClass();
        ToSpecConverter<T> toSpec = converter.getToSpecConverter(c);
        toSpec.toSpec(input, this);
    }

    public String concData(Object o1, Object o2) {
        return o1 + "_" + o2;
    }

    public String concNames(Object o1, Object o2) {
        return o1 + "__" + o2;
    }

    public String concNames(Object o1, Object o2, Object o3) {
        return o1 + "__" + o2 + "__" + o3;
    }

    public InAttributeName getAttributeName(String name) {
        if(isCached(name)) {
            return getCached(name);
        } else {
            InAttributeName attrName = new InAttributeName(name);
            cache(name, attrName);
            return attrName;
        }
    }

    //=========================
    //find
    //=========================

    public <R> R find(
            String name,
            ToParamConverter<R> toParam,
            Function<? super R, ? extends String> toString) throws ParsingException {
        if(cache.has(name)) {
            return cache.getAs(name);
        }

        R[] objects = toParam.toParamArray(this);
        R result = null;
        for(R obj: objects) {
            String objName = toString.apply(obj);
            cache.putIfNotExists(objName, obj); //falls durch Erstellung noch nicht gecached
            if(Objects.equals(name, objName)) {
                result = obj;
            }
        }

        if(result == null) {
            throw new ParsingException("named object not found: " + name);
        }
        return result;
    }

    public InConsumerAgentGroup findConsumerAgentGroup(String name) throws ParsingException {
        return find(
                name,
                ConsumerAgentGroupSpec.INSTANCE,
                InConsumerAgentGroup::getName
        );
    }

    public InUnivariateDoubleDistribution findDistribution(String name) throws ParsingException {
        return find(
                name,
                UnivariateDoubleDistributionSpec.INSTANCE,
                InUnivariateDoubleDistribution::getName
        );
    }

    public InProductGroup findProductGroup(String name) throws ParsingException {
        return find(
                name,
                ProductGroupSpec.INSTANCE,
                InProductGroup::getName
        );
    }

    public InFixProduct findFixProduct(String name) throws ParsingException {
        return find(
                name,
                FixProductSpec.INSTANCE,
                InFixProduct::getName
        );
    }

    @SuppressWarnings("unchecked")
    public <R extends InFile> R findFile(String name) throws ParsingException {
        return (R) find(
                name,
                FilesSpec.INSTANCE,
                InFile::getName
        );
    }

    //=========================
    //inline
    //=========================

    public <T> JsonNode inline(
            T input,
            Function<? super T, ? extends String> toString,
            boolean inline) throws ParsingException {
        ToSpecConverter<T> toSpec = converter.getToSpecConverter(input.getClass());
        if(inline) {
            ObjectNode holder = creator.objectNode();
            toSpec.create(input, holder, this);
            return holder;
        } else {
            callToSpec(input);
            String inputName = toString.apply(input);
            return creator.textNode(inputName);
        }
    }

    public <T extends InIRPactEntity> JsonNode inlineEntity(
            T input,
            boolean inline) throws ParsingException {
        return inline(
                input,
                InIRPactEntity::getName,
                inline
        );
    }

//    public <T> void inline(
//            T input,
//            Function<? super T, ? extends String> toString,
//            String tag,
//            SpecificationHelper2 spec,
//            boolean inline) throws ParsingException {
//        if(inline) {
//            ObjectNode holder = spec.putObjectNode(tag);
//            ToSpecConverter2<T> toSpec = converter.getToSpecConverter(input.getClass());
//            toSpec.create(input, holder, this);
//        } else {
//            String inputName = toString.apply(input);
//            spec.set(tag, inputName);
//        }
//    }

    //=========================
    //parse inline
    //=========================

    private static boolean isInlined(JsonNode node) {
        if(node == null) {
            throw new NullPointerException("node is null");
        }
        return node.isObject();
    }

    public <R> R parseInlined(
            JsonNode node,
            ToParamConverter<R> toParam,
            Function<? super R, ? extends String> toString) throws ParsingException {
        if(isInlined(node)) {
            return toParam.toParam((ObjectNode) node, this);
        } else {
            String name = node.textValue();
            return find(name, toParam, toString);
        }
    }

    @SuppressWarnings("unchecked")
    public <R extends InUnivariateDoubleDistribution> R parseInlinedDistribution(JsonNode node) throws ParsingException {
        return (R) parseInlined(
                node,
                UnivariateDoubleDistributionSpec.INSTANCE,
                InIRPactEntity::getName
        );
    }

    @SuppressWarnings("unchecked")
    public <R extends InSpatialDistribution> R parseInlinedSpatialDistribution(JsonNode node) throws ParsingException {
        return (R) parseInlined(
                node,
                SpatialDistributionSpec.INSTANCE,
                InIRPactEntity::getName
        );
    }

    @SuppressWarnings("unchecked")
    public <R extends InProductFindingScheme> R parseInlinedProductFindingScheme(JsonNode node) throws ParsingException {
        return (R) parseInlined(
                node,
                ProductFindingSchemeSpec.INSTANCE,
                InIRPactEntity::getName
        );
    }

    @SuppressWarnings("unchecked")
    public <R extends InProductInterestSupplyScheme> R parseInlinedProductInterestSupplyScheme(JsonNode node) throws ParsingException {
        return (R) parseInlined(
                node,
                ProductInterestSupplySchemeSpec.INSTANCE,
                InIRPactEntity::getName
        );
    }

    @SuppressWarnings("unchecked")
    public <R extends InDistanceEvaluator> R parseInlinedDistanceEvaluator(JsonNode node) throws ParsingException {
        return (R) parseInlined(
                node,
                DistanceEvaluatorSpec.INSTANCE,
                InIRPactEntity::getName
        );
    }

    //=========================
    //inlineArray
    //=========================

    public <T> ArrayNode inlineArray(
            T[] inputArray,
            Function<? super T, ? extends String> toString,
            boolean inline) throws ParsingException {
        ArrayNode arrNode = creator.arrayNode(inputArray.length);
        for(T input: inputArray) {
            JsonNode node = inline(input, toString, inline);
            arrNode.add(node);
        }
        return arrNode;
    }

    public <T extends InIRPactEntity> ArrayNode inlineEntitiyArray(T[] inputArray, boolean inline) throws ParsingException {
        return inlineArray(inputArray, InIRPactEntity::getName, inline);
    }

    //=========================
    //parse inline array
    //=========================

    public <R> R[] parseInlinedArray(
            JsonNode node,
            ToParamConverter<R> toParam,
            Function<? super R, ? extends String> toString,
            IntFunction<R[]> arrCreator) throws ParsingException {
        R[] arr = arrCreator.apply(node.size());
        for(int i = 0; i < node.size(); i++) {
            JsonNode child = node.get(i);
            R obj = parseInlined(child, toParam, toString);
            arr[i] = obj;
        }
        return arr;
    }

    public InUncertaintyGroupAttribute[] parseInlinedUncertaintyGroupAttributes(JsonNode node) throws ParsingException {
        return parseInlinedArray(
                node,
                UncertaintyGroupAttributeSpec.INSTANCE,
                InUncertaintyGroupAttribute::getName,
                InUncertaintyGroupAttribute[]::new
        );
    }

    //=========================
    //name
    //=========================

    public <T> ArrayNode namedArray(
            T[] inputArray,
            Function<? super T, ? extends String> toString) throws ParsingException {
        return inlineArray(inputArray, toString, false);
    }

    public <T extends InIRPactEntity> ArrayNode namedEntityArray(T[] inputArray) throws ParsingException {
        return namedArray(inputArray, InIRPactEntity::getName);
    }

    public ArrayNode namedNameSplitConsumerAgentGroupAttributes(InConsumerAgentGroupAttribute[] attrs) throws ParsingException {
        ArrayNode arrNode = creator.arrayNode(attrs.length);
        for(InConsumerAgentGroupAttribute attr: attrs) {
            if(attr instanceof InNameSplitConsumerAgentGroupAttribute) {
                String name = attr.getName();
                arrNode.add(name);
            } else {
                throw new ParsingException("requires InNameSplitConsumerAgentGroupAttribute");
            }
        }
        return arrNode;
    }

    //=========================
    //parse name
    //=========================

    public <R> R[] parseNamedArray(
            JsonNode node,
            ToParamConverter<R> toParam,
            Function<? super R, ? extends String> toString,
            IntFunction<R[]> arrCreator) throws ParsingException {
        return parseInlinedArray(node, toParam, toString, arrCreator);
    }

    public InConsumerAgentGroup[] parseNamedConsumerAgentGroupArray(JsonNode node) throws ParsingException {
        return parseNamedArray(
                node,
                ConsumerAgentGroupSpec.INSTANCE,
                InConsumerAgentGroup::getName,
                InConsumerAgentGroup[]::new
        );
    }

    public InConsumerAgentGroupAttribute[] parseNamedSplitConsumerAgentGroupAttributeArray(JsonNode node) {
        InConsumerAgentGroupAttribute[] arr = new InConsumerAgentGroupAttribute[node.size()];
        for(int i = 0; i < node.size(); i++) {
            JsonNode child = node.get(i);
            String fullName = child.textValue();
            InNameSplitConsumerAgentGroupAttribute attr;
            if(isCached(fullName)) {
                attr = getCached(fullName);
            } else {
                attr = new InNameSplitConsumerAgentGroupAttribute();
                attr.setName(fullName);
            }
            arr[i] = attr;
        }
        return arr;
    }

    public InAttributeName[] parseAttributeNameArray(JsonNode node) {
        InAttributeName[] arr = new InAttributeName[node.size()];
        for(int i = 0; i < node.size(); i++) {
            JsonNode child = node.get(i);
            arr[i] = getAttributeName(child.textValue());
        }
        return arr;
    }
}
