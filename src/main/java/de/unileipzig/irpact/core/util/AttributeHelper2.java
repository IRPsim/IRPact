package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.exception.IRPactNoSuchElementException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public final class AttributeHelper2 extends SimulationEntityBase {

    public AttributeHelper2() {
    }

    public AttributeHelper2(SimulationEnvironment environment) {
        setEnvironment(environment);
    }

    //=========================
    //util
    //=========================

    private static IRPactNoSuchElementException handleMissingAttribute(Nameable caller, String attrName) throws IRPactNoSuchElementException {
        return new IRPactNoSuchElementException("missing attribute '{}' for '{}'", attrName, caller.getName());
    }

    private static IRPactIllegalArgumentException handleWrongTypeAttribute(Nameable caller, String type) {
        return new IRPactIllegalArgumentException("attribute '{}' has no {} value", caller.getName(), type);
    }

    //=========================
    //int
    //=========================

    public static int getIntValue(Attribute attribute, Product product, int year) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }

        if(product != null && attribute instanceof ConsumerAgentAttribute) {
            ConsumerAgentAttribute caAttribute = (ConsumerAgentAttribute) attribute;
            if(caAttribute.isProductRelatedAttribute()) {
                return getIntValue(
                        caAttribute.asProductRelatedAttribute().getAttribute(product),
                        product,
                        year
                );
            }
        }

        if(attribute.isAnnualAttribute()) {
            return getIntValue(
                    attribute.asAnnualAttribute().getAttribute(year),
                    product,
                    year
            );
        }

        if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            return attribute.asValueAttribute().getIntValue();
        } else {
            throw handleWrongTypeAttribute(attribute, "int (double)");
        }
    }

    public long getIntValue(ConsumerAgent agent, Product product, String attributeName) {
        Attribute attribute = agent.findAttribute(attributeName);
        if(attribute == null) {
            throw handleMissingAttribute(agent, attributeName);
        }
        return getIntValue(attribute, product, getCurrentYear());
    }

    public long getIntValue(ConsumerAgent agent, String attributeName) {
        return getIntValue(agent, null, attributeName);
    }

    public static void setIntValue(Attribute attribute, Product product, int year, int newValue) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }

        if(product != null && attribute instanceof ConsumerAgentAttribute) {
            ConsumerAgentAttribute caAttribute = (ConsumerAgentAttribute) attribute;
            if(caAttribute.isProductRelatedAttribute()) {
                setIntValue(
                        caAttribute.asProductRelatedAttribute().getAttribute(product),
                        product,
                        year,
                        newValue
                );
                return;
            }
        }

        if(attribute.isAnnualAttribute()) {
            setIntValue(
                    attribute.asAnnualAttribute().getAttribute(year),
                    product,
                    year,
                    newValue
            );
            return;
        }

        if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            attribute.asValueAttribute().setIntValue(newValue);
        } else {
            throw handleWrongTypeAttribute(attribute, "int (double)");
        }
    }

    public void setIntValue(ConsumerAgent agent, Product product, String attributeName, int newValue) {
        Attribute attribute = agent.findAttribute(attributeName);
        if(attribute == null) {
            throw handleMissingAttribute(agent, attributeName);
        }
        setIntValue(attribute, product, getCurrentYear(), newValue);
    }

    public void setIntValue(ConsumerAgent agent, String attributeName, int newValue) {
        setIntValue(agent, null, attributeName, newValue);
    }
}
