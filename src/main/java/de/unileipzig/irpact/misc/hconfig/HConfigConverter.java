package de.unileipzig.irpact.misc.hconfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.Util;
//import de.unileipzig.irpact.experimental.deprecated.input.distribution.IConstantUnivariateDistribution;
//import de.unileipzig.irpact.experimental.deprecated.input.distribution.IFiniteMassPointsDiscreteDistribution;
//import de.unileipzig.irpact.experimental.deprecated.input.distribution.IMassPoint;
//import de.unileipzig.irpact.experimental.deprecated.input.distribution.IUnivariateDoubleDistribution;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Daniel Abitz
 */
public class HConfigConverter {

    public static final String ConstantUnivariateDistribution = "ConstantUnivariateDistribution";
    public static final String FiniteMassPointsDiscreteDistribution = "FiniteMassPointsDiscreteDistribution";
    public static final String name = "name";
    public static final String seed = "seed";
    public static final String value = "value";
    public static final String distribution = "distribution";
    public static final String parameters = "parameters";

    private final Map<String, Object> CACHE = new HashMap<>();
    private final ObjectMapper MAPPER = new ObjectMapper();

    //=========================
    //static util
    //=========================

    private static Stream<Map.Entry<String, JsonNode>> streamFields(JsonNode node) {
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        Spliterator<Map.Entry<String, JsonNode>> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
        return StreamSupport.stream(spliterator, false);
    }

    private static String getString(ObjectNode node, String field) {
        JsonNode n = node.get(field);
        if(n == null) {
            throw new NoSuchElementException(field);
        }
        if(!n.isTextual()) {
            throw new IllegalArgumentException(field);
        }
        return n.textValue();
    }

    private static double getDouble(ObjectNode node, String field) {
        JsonNode n = node.get(field);
        if(n == null) {
            throw new NoSuchElementException(field);
        }
        if(!n.isNumber()) {
            throw new IllegalArgumentException(field);
        }
        return n.doubleValue();
    }

    private static long getLongOr(ObjectNode node, String field, long ifMissing) {
        JsonNode n = node.get(field);
        if(n == null) {
            return ifMissing;
        }
        if(!n.isNumber()) {
            return ifMissing;
        }
        return n.longValue();
    }

    private static ArrayNode getArray(ObjectNode node, String field) {
        JsonNode n = node.get(field);
        if(n == null) {
            throw new NoSuchElementException(field);
        }
        if(!n.isArray()) {
            throw new IllegalArgumentException(field);
        }
        return (ArrayNode) n;
    }

    private static ObjectNode getObject(ObjectNode node, String field) {
        JsonNode n = node.get(field);
        if(n == null) {
            throw new NoSuchElementException(field);
        }
        if(!n.isObject()) {
            throw new IllegalArgumentException(field);
        }
        return (ObjectNode) n;
    }

    //=========================
    //util
    //=========================

    @SuppressWarnings("unchecked")
    private <T> T getOrPut(String name, T value) {
        if(CACHE.containsKey(name)) {
            return (T) CACHE.get(name);
        } else {
            CACHE.put(name, value);
            return value;
        }
    }

//    private IMassPoint toMassPoint(String key, JsonNode value) {
//        double mpValue = Double.parseDouble(key);
//        double mpWeight = value.doubleValue();
//        String name = mpValue + "|" + mpWeight;
//        return getOrPut(name, new IMassPoint(name, mpValue, mpWeight));
//    }
//
//    //=========================
//    //from json
//    //=========================
//
//    public IUnivariateDoubleDistribution toIUnivariateDoubleDistribution(ObjectNode node) {
//        String dist = getString(node, distribution);
//        if(FiniteMassPointsDiscreteDistribution.equals(dist)) {
//            return toIFiniteMassPointsDiscreteDistribution(node);
//        }
//        if(ConstantUnivariateDistribution.equals(dist)) {
//            return toIConstantUnivariateDistribution(node);
//        }
//        throw new IllegalArgumentException(dist);
//    }
//
//    public IFiniteMassPointsDiscreteDistribution toIFiniteMassPointsDiscreteDistribution(ObjectNode node) {
//        String fmpName = getString(node, name);
//        long fmpSeed = getLongOr(node, seed, Util.USE_RANDOM_SEED);
//        IMassPoint[] massPoints = toIMassPoint(getObject(node, parameters));
//        return new IFiniteMassPointsDiscreteDistribution(fmpName, fmpSeed, massPoints);
//    }
//
//    public IConstantUnivariateDistribution toIConstantUnivariateDistribution(ObjectNode node) {
//        String cName = getString(node, name);
//        double cValue = getDouble(node, value);
//        return new IConstantUnivariateDistribution(cName, cValue);
//    }
//
//    public IMassPoint[] toIMassPoint(ObjectNode node) {
//        return streamFields(node)
//                .map(e -> toMassPoint(e.getKey(), e.getValue()))
//                .toArray(IMassPoint[]::new);
//    }
//
//    //=========================
//    //to json
//    //=========================
//
//    public ObjectNode fromIFiniteMassPointsDiscreteDistribution(IFiniteMassPointsDiscreteDistribution dist) {
//        ObjectNode root = MAPPER.createObjectNode();
//        root.put(name, dist.getName());
//        root.put(seed, dist.getFmpSeed());
//        root.put(distribution, FiniteMassPointsDiscreteDistribution);
//        ObjectNode paramNode = root.putObject(parameters);
//        for(IMassPoint mp: dist.getMassPoints()) {
//            paramNode.put(Double.toString(mp.getMpValue()), mp.getMpWeight());
//        }
//        return root;
//    }
//
//    public ObjectNode fromIConstantUnivariateDistribution(IConstantUnivariateDistribution dist) {
//        ObjectNode root = MAPPER.createObjectNode();
//        root.put(name, dist.getName());
//        root.put(distribution, FiniteMassPointsDiscreteDistribution);
//        root.put(value, dist.getConstDistValue());
//        return root;
//    }
}
