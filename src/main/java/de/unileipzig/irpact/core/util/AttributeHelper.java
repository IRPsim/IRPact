package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.exception.IRPactNoSuchElementException;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public final class AttributeHelper extends SimulationEntityBase {

    public AttributeHelper(SimulationEnvironment environment) {
        setEnvironment(environment);
    }

    //==================================================
    //static
    //==================================================

    protected static int currentYear(SimulationEnvironment environment) {
        return environment.getTimeModel().getCurrentYear();
    }

    protected static void handleMissingAttribute(Nameable caller, String attrName) throws IRPactNoSuchElementException {
        throw new IRPactNoSuchElementException("missing attribute '{}' for '{}'", attrName, caller.getName());
    }

    //=========================
    //util
    //=========================

    public static double getDoubleValue(SimulationEnvironment environment, Attribute attribute) {
        return getDoubleValue(currentYear(environment), attribute);
    }
    public static double getDoubleValue(int currentYear, Attribute attribute) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear);
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                return attr.asValueAttribute().getDoubleValue();
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            return attribute.asValueAttribute().getDoubleValue();
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no double value", attribute.getName());
    }

    public static void setDoubleValue(SimulationEnvironment environment, Attribute attribute, double value) {
        setDoubleValue(currentYear(environment), attribute, value);
    }
    public static void setDoubleValue(int currentYear, Attribute attribute, double value) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear);
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                attr.asValueAttribute().setDoubleValue(value);
                return;
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            attribute.asValueAttribute().setDoubleValue(value);
            return;
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no double value", attribute.getName());
    }

    public static String getStringValue(SimulationEnvironment environment, Attribute attribute) {
        return getStringValue(currentYear(environment), attribute);
    }
    public static String getStringValue(int currentYear, Attribute attribute) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear);
            if(attr.isValueAttributeWithDataType(DataType.STRING)) {
                return attr.asValueAttribute().getStringValue();
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.STRING)) {
            return attribute.asValueAttribute().getStringValue();
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no double value", attribute.getName());
    }

    //==================================================
    //not static
    //==================================================

    //=========================
    //util
    //=========================

    protected int currentYear() {
        return currentYear(environment);
    }

    //==================================================
    //agent
    //==================================================

    //=========================
    //value
    //=========================

    public boolean getBooleanValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        ConsumerAgentAttribute attribute = agent.getAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getBooleanValue(attribute);
    }
    public boolean findBooleanValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        Attribute attribute = agent.findAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getBooleanValue(attribute);
    }

    public void findAndSetBooleanValue(ConsumerAgent agent, String name, boolean value) {
        Attribute attribute = agent.findAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        setBooleanValue(attribute, value);
    }

    public int getIntValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        ConsumerAgentAttribute attribute = agent.getAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getIntValue(attribute);
    }
    public int findIntValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        Attribute attribute = agent.findAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getIntValue(attribute);
    }

    public long getLongValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        ConsumerAgentAttribute attribute = agent.getAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getLongValue(attribute);
    }
    public long findLongValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        Attribute attribute = agent.findAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getLongValue(attribute);
    }
    public double getDoubleValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        ConsumerAgentAttribute attribute = agent.getAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getDoubleValue(attribute);
    }
    public double getDoubleValue(ConsumerAgent agent, String name, int year) throws IRPactNoSuchElementException {
        ConsumerAgentAttribute attribute = agent.getAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getDoubleValue(year, attribute);
    }
    public double findDoubleValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        Attribute attribute = agent.findAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getDoubleValue(attribute);
    }

    public static double findDoubleValue2(ConsumerAgent agent, String name) {
        Attribute attribute = agent.findAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getDoubleValue(agent.getEnvironment(), attribute);
    }

    public void setDoubleValue(ConsumerAgent agent, String name, double value) throws IRPactNoSuchElementException {
        ConsumerAgentAttribute attribute = agent.getAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        setDoubleValue(attribute, value);
    }

    public String findStringValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
        Attribute attribute = agent.findAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getStringValue(agent.getEnvironment(), attribute);
    }

    //=========================
    //product
    //=========================

    public double getDoubleValue(ConsumerAgent agent, Product product, String name) throws IRPactNoSuchElementException {
        ConsumerAgentAttribute attribute = agent.getProductRelatedAttribute(name);
        if(attribute == null) {
            handleMissingAttribute(agent, name);
        }
        return getDoubleValue(attribute, product);
    }

    //=========================
    //or product
    //=========================

    public double tryFindDoubleValue(ConsumerAgent agent, Product product, String name) throws IRPactNoSuchElementException {
        Attribute attribute = agent.findAttribute(name);
        if(attribute == null) {
            return getDoubleValue(agent, product, name);
        } else {
            return getDoubleValue(attribute);
        }
    }

    //==================================================
    //attribute
    //==================================================

    //=========================
    //value
    //=========================

    public boolean getBooleanValue(Attribute attribute) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                return attr.asValueAttribute().getBooleanValue();
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            return attribute.asValueAttribute().getBooleanValue();
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no boolean value", attribute.getName());
    }

    public void setBooleanValue(Attribute attribute, boolean value) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                attr.asValueAttribute().setBooleanValue(value);
                return;
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            attribute.asValueAttribute().setBooleanValue(value);
            return;
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no boolean value", attribute.getName());
    }

    public int getIntValue(Attribute attribute) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                return attr.asValueAttribute().getIntValue();
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            return attribute.asValueAttribute().getIntValue();
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no int value", attribute.getName());
    }

    public void setIntValue(Attribute attribute, int value) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                attr.asValueAttribute().setIntValue(value);
                return;
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            attribute.asValueAttribute().setIntValue(value);
            return;
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no int value", attribute.getName());
    }

    public long getLongValue(Attribute attribute) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                return attr.asValueAttribute().getLongValue();
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            return attribute.asValueAttribute().getLongValue();
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no long value", attribute.getName());
    }

    public void setLongValue(Attribute attribute, long value) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                attr.asValueAttribute().setLongValue(value);
                return;
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            attribute.asValueAttribute().setLongValue(value);
            return;
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no long value", attribute.getName());
    }

    public double getDoubleValue(Attribute attribute) {
        return getDoubleValue(currentYear(), attribute);
    }

    public void setDoubleValue(Attribute attribute, double value) {
        setDoubleValue(currentYear(), attribute, value);
    }

    //=========================
    //product
    //=========================

    public double getDoubleValue(ConsumerAgentAttribute attribute, Product product) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isProductRelatedAttribute()) {
            ConsumerAgentAttribute pAttr = attribute.asProductRelatedAttribute().getAttribute(product);
            if(pAttr == null) {
                throw new NullPointerException("attr is null");
            }
            else if(pAttr.isAnnualAttribute()) {
                Attribute attr = pAttr.asAnnualAttribute().getAttribute(currentYear());
                if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                    return attr.asValueAttribute().getDoubleValue();
                }
            }
            else if(pAttr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                return pAttr.asValueAttribute().getDoubleValue();
            }
        }

        throw new IRPactIllegalArgumentException("attribute '{}' ist not product related", attribute.getName());
    }

    public void setDoubleValue(ConsumerAgentAttribute attribute, Product product, double value) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isProductRelatedAttribute()) {
            ConsumerAgentAttribute pAttr = attribute.asProductRelatedAttribute().getAttribute(product);
            if(pAttr == null) {
                throw new NullPointerException("attr is null");
            }
            else if(pAttr.isAnnualAttribute()) {
                Attribute attr = pAttr.asAnnualAttribute().getAttribute(currentYear());
                if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                    attr.asValueAttribute().setDoubleValue(value);
                    return;
                }
            }
            else if(pAttr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                pAttr.asValueAttribute().setDoubleValue(value);
                return;
            }
        }

        throw new IRPactIllegalArgumentException("attribute '{}' ist not product related", attribute.getName());
    }
}
