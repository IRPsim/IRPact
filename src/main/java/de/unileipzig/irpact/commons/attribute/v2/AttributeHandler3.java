package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v2.simple.Attribute2;
import de.unileipzig.irpact.commons.attribute.v2.simple.AttributeType2;
import de.unileipzig.irpact.core.agent.AttributableAgent;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public class AttributeHandler3 {










    //==================================================
    //default usage
    //==================================================

    public static Attribute2 getAttribute(String attrName, boolean find, AttributableAgent agent) {
        return find
                ? agent.findAttribute2(attrName)
                : agent.getAttribute2(attrName);
    }

    public static Attribute2 get(int year, Product product, String attrName, boolean find, AttributableAgent agent) {
        return get(year, product, getAttribute(attrName, find, agent));
    }

    public static Attribute2 get(int year, Product product, Attribute2 input) {
        AttributeType2 type = input.getType();
        switch (type) {
            case DOUBLE:
            case STRING:
                return input;

            case ANNUAL:
                return get(year, product, input.asAnnual().get(year));

            case PRODUCT:
                return get(year, product, input.asProduct().get(product));

            default:
                throw new IllegalArgumentException("unsupported type: " + type);
        }
    }
}
