package de.unileipzig.irpact.experimental.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.annotation.Experimental;
import de.unileipzig.irpact.commons.distribution.*;
import de.unileipzig.irpact.core.agent.policy.NoTaxes;
import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import de.unileipzig.irpact.core.agent.policy.TaxesScheme;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Experimental
public final class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Json() {
    }

    //=========================
    //general
    //=========================

    private static final PrettyPrinter minimalPrinter = new MinimalPrettyPrinter("");
    private static final PrettyPrinter prettyPrinter = new DefaultPrettyPrinter()
            .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

    private static String toString(JsonNode node, PrettyPrinter printer) {
        StringWriter sw = new StringWriter();
        try(JsonGenerator gen = MAPPER.getFactory().createGenerator(sw)) {
            if(printer != null) {
                gen.setPrettyPrinter(printer);
            }
            gen.writeTree(node);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return sw.toString();
    }

    public static String print(JsonNode node) {
        return toString(node, null);
    }

    public static String printMinimal(JsonNode node) {
        return toString(node, minimalPrinter);
    }

    public static String printPretty(JsonNode node) {
        return toString(node, prettyPrinter);
    }

    //=========================
    //dist
    //=========================

    public static ObjectNode toNode(Distribution distribution) {
        if(distribution instanceof BooleanDistribution) {
            return toNode((BooleanDistribution) distribution);
        }
        if(distribution instanceof ConstantDistribution) {
            return toNode((ConstantDistribution) distribution);
        }
        if(distribution instanceof RandomDistribution) {
            return toNode((RandomDistribution) distribution);
        }
        throw new IllegalStateException("unknown Distribution");
    }

    public static Distribution toDistribution(ObjectNode node) {
        String type = node.get("type").textValue();
        switch (type) {
            case "BooleanDistribution":
                return toBooleanDistribution(node);
            case "ConstantDistribution":
                return toConstantDistribution(node);
            case "RandomDistribution":
                return toRandomDistribution(node);
            default:
                throw new IllegalStateException();
        }
    }

    public static ObjectNode toNode(BooleanDistribution booleanDistribution) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", BooleanDistribution.NAME);
        node.put("name", booleanDistribution.getName());
        node.put("seed", booleanDistribution.getSeed());
        node.put("threshold", booleanDistribution.getThreshold());
        return node;
    }

    public static BooleanDistribution toBooleanDistribution(ObjectNode node) {
        String type = node.get("type").textValue();
        if(!BooleanDistribution.NAME.equals(type)) {
            throw new IllegalArgumentException();
        }
        String name = node.get("name").textValue();
        long seed = node.get("seed").longValue();
        double threshold = node.get("threshold").doubleValue();
        return new BooleanDistribution(name, seed, threshold);
    }

    public static ObjectNode toNode(ConstantDistribution constantDistribution) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", ConstantDistribution.NAME);
        node.put("name", constantDistribution.getName());
        node.put("value", constantDistribution.getValue());
        return node;
    }

    public static ConstantDistribution toConstantDistribution(ObjectNode node) {
        String type = node.get("type").textValue();
        if(!ConstantDistribution.NAME.equals(type)) {
            throw new IllegalArgumentException();
        }
        String name = node.get("name").textValue();
        double value = node.get("value").doubleValue();
        return new ConstantDistribution(name, value);
    }

    public static ObjectNode toNode(RandomDistribution randomDistribution) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", RandomDistribution.NAME);
        node.put("name", randomDistribution.getName());
        node.put("seed", randomDistribution.getSeed());
        node.put("lowerBound", randomDistribution.getLowerBound());
        node.put("upperBound", randomDistribution.getUpperBound());
        return node;
    }

    public static RandomDistribution toRandomDistribution(ObjectNode node) {
        String type = node.get("type").textValue();
        if(!RandomDistribution.NAME.equals(type)) {
            throw new IllegalArgumentException();
        }
        String name = node.get("name").textValue();
        long seed = node.get("seed").longValue();
        double lowerBound = node.get("lowerBound").doubleValue();
        double upperBound = node.get("upperBound").doubleValue();
        return new RandomDistribution(name, seed, lowerBound, upperBound);
    }

    //=========================
    //product
    //=========================

    public static ObjectNode toNode(ProductGroupAttribute productGroupAttribute) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("name", productGroupAttribute.getName());
        node.set("distribution", toNode(productGroupAttribute.getDistribution()));
        return node;
    }

    public static ProductGroupAttribute toProductGroupAttribute(ObjectNode node) {
        String name = node.get("name").textValue();
        ObjectNode distributionNode = (ObjectNode) node.get("distribution");
        Distribution distribution = toDistribution(distributionNode);
        return new BasicProductGroupAttribute(name, (UnivariateDistribution) distribution);
    }

    public static ObjectNode toNode(ProductGroup productGroup) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("name", productGroup.getName());
        ArrayNode needs = node.putArray("needsSatisfied");
        for(Need need: productGroup.getNeedsSatisfied()) {
            needs.add(need.print());
        }
        ArrayNode attributes = node.putArray("productGroupAttributes");
        for(ProductGroupAttribute attribute: productGroup.getAttributes()) {
            attributes.add(toNode(attribute));
        }
        return node;
    }

    public static ProductGroup toProductGroup(SimulationEnvironment environment, ObjectNode node) {
        String name = node.get("name").textValue();
        Set<Need> needsSatisfied = new HashSet<>();
        ArrayNode needsSatisfiedNode = (ArrayNode) node.get("needsSatisfied");
        for(int i = 0; i < needsSatisfiedNode.size(); i++) {
            needsSatisfied.add(new BasicNeed(needsSatisfiedNode.get(i).textValue()));
        }
        Set<ProductGroupAttribute> productGroupAttributes = new HashSet<>();
        ArrayNode productGroupAttributesNode = (ArrayNode) node.get("needsSatisfied");
        for(int i = 0; i < needsSatisfiedNode.size(); i++) {
            productGroupAttributes.add(toProductGroupAttribute((ObjectNode) productGroupAttributesNode.get(i)));
        }
        return new BasicProductGroup(
                environment,
                new HashSet<>(),
                name,
                productGroupAttributes,
                needsSatisfied
        );
    }

    //=========================
    //policyAgent
    //=========================

    public static ObjectNode toNode(TaxesScheme taxesScheme) {
        if(taxesScheme instanceof NoTaxes) {
            return toNode((NoTaxes) taxesScheme);
        }
        throw new IllegalStateException("unknown TaxesScheme");
    }

    public static ObjectNode toNode(NoTaxes noTaxes) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", NoTaxes.NAME);
        return node;
    }

    public static ObjectNode toNode(PolicyAgent policyAgent) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("name", policyAgent.getName());
        node.put("informationAuthority", policyAgent.getInformationAuthority());
        node.set("taxesScheme", toNode(policyAgent.getTaxesScheme()));
        return node;
    }
}
