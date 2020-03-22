package de.unileipzig.irpact.experimental.io.json;

import com.fasterxml.jackson.core.JsonToken;
import de.unileipzig.irpact.commons.annotation.Experimental;
import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irpact.commons.distribution.ConstantDistribution;
import de.unileipzig.irpact.commons.distribution.RandomBoundedDistribution;
import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;

import java.util.*;

/**
 * @author Daniel Abitz
 */
@Experimental
public class JsonRules {

    //==================================================
    //Named Rules
    //==================================================

    private static final Map<String, DeserializationRule<Object>> deserializationRules = new HashMap<>();
    private static final Map<String, SerializationRule<Object>> serializationRules = new HashMap<>();

    static {
        addDistributions();
    }

    private static void addDistributions() {
        deserializationRules.put(
                BooleanDistribution.NAME,
                (jp, ctxt) -> {
                    jp.nextValue();
                    String name = jp.getValueAsString();
                    jp.nextValue();
                    double threshold = jp.getValueAsDouble();
                    return new BooleanDistribution(name, new Random(), threshold);
                }
        );
        serializationRules.put(
                BooleanDistribution.NAME,
                (value, jg, provider) -> {
                    BooleanDistribution dist = (BooleanDistribution) value;
                    jg.writeStringField(Constants.NAME, dist.getName());
                    jg.writeNumberField(Constants.THRESHOLD, dist.getThreshold());
                }
        );

        deserializationRules.put(
                RandomBoundedDistribution.NAME,
                (jp, ctxt) -> {
                    jp.nextValue();
                    String name = jp.getValueAsString();
                    jp.nextValue();
                    double lowerBound = jp.getValueAsDouble();
                    jp.nextValue();
                    double upperBound = jp.getValueAsDouble();
                    return new RandomBoundedDistribution(name, new Random(), lowerBound, upperBound);
                }
        );
        serializationRules.put(
                RandomBoundedDistribution.NAME,
                (value, jg, provider) -> {
                    RandomBoundedDistribution dist = (RandomBoundedDistribution) value;
                    jg.writeStringField(Constants.NAME, dist.getName());
                    jg.writeNumberField(Constants.LOWER_BOUND, dist.getLowerBound());
                    jg.writeNumberField(Constants.UPPER_BOUND, dist.getUpperBound());
                }
        );

        deserializationRules.put(
                ConstantDistribution.NAME,
                (jp, ctxt) -> {
                    jp.nextValue();
                    String name = jp.getValueAsString();
                    jp.nextValue();
                    double value = jp.getValueAsDouble();
                    return new ConstantDistribution(name, value);
                }
        );
        serializationRules.put(
                ConstantDistribution.NAME,
                (value, jg, provider) -> {
                    ConstantDistribution dist = (ConstantDistribution) value;
                    jg.writeStringField(Constants.NAME, dist.getName());
                    jg.writeNumberField(Constants.VALUE, dist.getValue());
                }
        );
    }

    //==================================================
    //Specific
    //==================================================

    private static String getEntityName(Object obj) {
        return obj.getClass().getSimpleName();
    }

    //=========================
    //ProductGroupAttribute
    //=========================

    public static DeserializationRule<ProductGroupAttribute> productGroupAttributeDRule = (jp, ctxt) -> {
        jp.nextValue();
        String name = jp.getValueAsString();
        String field = jp.nextFieldName();
        DeserializationRule<Object> rule = deserializationRules.get(field);
        UnivariateDistribution distribution = (UnivariateDistribution) rule.deserialize(jp, ctxt);
        return new BasicProductGroupAttribute(name, distribution);
    };

    public static SerializationRule<ProductGroupAttribute> productGroupAttributeSRule = (value, jg, provider) -> {
        jg.writeStringField(Constants.NAME, value.getName());
        String entityName = getEntityName(value.getDistribution());
        SerializationRule<Object> rule = serializationRules.get(entityName);
        jg.writeObjectFieldStart(entityName);
        rule.serialize(value.getDistribution(), jg, provider);
        jg.writeEndObject();
    };

    //=========================
    //BasicProductGroup
    //=========================

    @ToDo("env fehlt hier am ende - ganzes konzept neu ueberdenken")
    public static DeserializationRule<ProductGroup> basicProductGroupDRule = (jp, ctxt) -> {
        jp.nextValue();
        String name = jp.getValueAsString();
        Set<ProductGroupAttribute> attr = new HashSet<>();
        while(jp.nextToken() != JsonToken.END_ARRAY) {
            ProductGroupAttribute pgd = productGroupAttributeDRule.deserialize(jp, ctxt);
            attr.add(pgd);
        }
        Set<Need> needs = new HashSet<>();
        while(jp.nextToken() != JsonToken.END_ARRAY) {
            jp.nextValue();
            String needStr = jp.getValueAsString();
            needs.add(new BasicNeed(needStr));
        }
        throw new UnsupportedOperationException();
    };

    public static SerializationRule<BasicProductGroup> basicProductGroupSRule = (value, jg, provider) -> {
        jg.writeStringField(Constants.NAME, value.getName());
        jg.writeArrayFieldStart(Constants.PRODUCT_GROUP_ATTRIBUTES);
        for(ProductGroupAttribute pga: value.getAttributes()) {
            productGroupAttributeSRule.serialize(pga, jg, provider);
        }
        jg.writeEndArray();
        jg.writeArrayFieldStart(Constants.NEEDS_SATISFIED);
        for(Need need: value.getNeedsSatisfied()) {
            jg.writeString(need.print());
        }
        jg.writeEndArray();
    };


}
