package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.exception.IRPactNoSuchElementException;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductRelatedAttribute;
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
    //util
    //==================================================

    private static IRPactIllegalArgumentException handleWrongTypeAttribute(Nameable nameable, DataType type) {
        return new IRPactIllegalArgumentException("attribute '{}' has no {} value", nameable.getName(), type);
    }

    private static IRPactIllegalArgumentException handleMissingPorduct(Nameable nameable) {
        return new IRPactIllegalArgumentException("missing product for attribute '{}'", nameable.getName());
    }

    private static IRPactNoSuchElementException handleMissingAttribute() throws IRPactNoSuchElementException {
        return new IRPactNoSuchElementException("missing attribute");
    }

    protected int getCurrentYear() {
        if(environment == null) {
            throw new UnsupportedOperationException("missing environment");
        }

        try {
            return environment.getTimeModel().getCurrentYear();
        } catch (NullPointerException e) {
            return environment.getTimeModel().getFirstSimulationYear();
        }
    }

    //=========================
    //general
    //=========================

    private static Attribute getAttributeWithType(int year, Product product, DataType dataType, Attribute attribute) {
        if(attribute == null) {
            throw handleMissingAttribute();
        }

        if(attribute.isAnnualAttribute()) {
            Attribute annualAttribute = attribute.asAnnualAttribute().getAttribute(year);
            return getAttributeWithType(year, product, dataType, annualAttribute);
        }

        if(attribute instanceof ConsumerAgentAttribute) {
            ConsumerAgentAttribute caAttribute = (ConsumerAgentAttribute) attribute;
            if(caAttribute.isProductRelatedAttribute()) {
                if(product == null) {
                    throw handleMissingPorduct(caAttribute);
                }

                ConsumerAgentAttribute productRelatedAttribute = caAttribute.asProductRelatedAttribute().getAttribute(product);
                return getAttributeWithType(year, product, dataType, productRelatedAttribute);
            }
        }

        if(attribute.isValueAttributeWithDataType(dataType)) {
            return attribute;
        }

        throw handleWrongTypeAttribute(attribute, dataType);
    }

    private static Attribute getAttribute(ConsumerAgent agent, String attributeName, boolean find) {
        return find
                ? agent.findAttribute(attributeName)
                : agent.getAttribute(attributeName);
    }

    private static Attribute getAttribute(ConsumerAgent agent, Product product, String attributeName, boolean find) {
        if(product == null) {
            return getAttribute(agent, attributeName, find);
        } else {
            ConsumerAgentProductRelatedAttribute attr = agent.getProductRelatedAttribute(attributeName);
            if(attr == null) {
                return getAttribute(agent, attributeName, find);
            }

            ConsumerAgentAttribute pAttr = attr.getAttribute(product);
            if(pAttr == null) {
                return getAttribute(agent, attributeName, find);
            }

            return pAttr;
        }
    }

    //=========================
    //double
    //=========================

    public static double getDouble(int year, Product product, Attribute attribute) {
        Attribute attr = getAttributeWithType(year, product, DataType.DOUBLE, attribute);
        return attr.asValueAttribute().getDoubleValue();
    }

    public static void setDouble(int year, Product product, Attribute attribute, double value) {
        Attribute attr = getAttributeWithType(year, product, DataType.DOUBLE, attribute);
        attr.asValueAttribute().setDoubleValue(value);
    }

    public double getDouble(ConsumerAgent agent, Product product, String attributeName, boolean find) {
        return getDouble(getCurrentYear(), product, getAttribute(agent, product, attributeName, find));
    }

    public double getDouble(ConsumerAgent agent, String attributeName, boolean find) {
        return getDouble(agent, null, attributeName, find);
    }

    public void setDouble(ConsumerAgent agent, Product product, String attributeName, double value, boolean find) {
        setDouble(getCurrentYear(), product, getAttribute(agent, product, attributeName, find), value);
    }

    public void setDouble(ConsumerAgent agent, String attributeName, double value, boolean find) {
        setDouble(agent, null, attributeName, value, find);
    }

    //=========================
    //int
    //=========================

    public static int getInt(int year, Product product, Attribute attribute) {
        Attribute attr = getAttributeWithType(year, product, DataType.DOUBLE, attribute);
        return attr.asValueAttribute().getIntValue();
    }

    public static void setInt(int year, Product product, Attribute attribute, int value) {
        Attribute attr = getAttributeWithType(year, product, DataType.DOUBLE, attribute);
        attr.asValueAttribute().setIntValue(value);
    }

    public int getInt(ConsumerAgent agent, Product product, String attributeName, boolean find) {
        return getInt(getCurrentYear(), product, getAttribute(agent, product, attributeName, find));
    }

    public int getInt(ConsumerAgent agent, String attributeName, boolean find) {
        return getInt(agent, null, attributeName, find);
    }

    public void setInt(ConsumerAgent agent, Product product, String attributeName, int value, boolean find) {
        setInt(getCurrentYear(), product, getAttribute(agent, product, attributeName, find), value);
    }

    public void setInt(ConsumerAgent agent, String attributeName, int value, boolean find) {
        setInt(agent, null, attributeName, value, find);
    }

    //=========================
    //long
    //=========================

    public static long getLong(int year, Product product, Attribute attribute) {
        Attribute attr = getAttributeWithType(year, product, DataType.DOUBLE, attribute);
        return attr.asValueAttribute().getLongValue();
    }

    public static void setLong(int year, Product product, Attribute attribute, long value) {
        Attribute attr = getAttributeWithType(year, product, DataType.DOUBLE, attribute);
        attr.asValueAttribute().setLongValue(value);
    }

    public long getLong(ConsumerAgent agent, Product product, String attributeName, boolean find) {
        return getLong(getCurrentYear(), product, getAttribute(agent, product, attributeName, find));
    }

    public long getLong(ConsumerAgent agent, String attributeName, boolean find) {
        return getLong(agent, null, attributeName, find);
    }

    public void setLong(ConsumerAgent agent, Product product, String attributeName, long value, boolean find) {
        setLong(getCurrentYear(), product, getAttribute(agent, product, attributeName, find), value);
    }

    public void setLong(ConsumerAgent agent, String attributeName, long value, boolean find) {
        setLong(agent, null, attributeName, value, find);
    }

    //=========================
    //boolean
    //=========================

    public static boolean getBoolean(int year, Product product, Attribute attribute) {
        Attribute attr = getAttributeWithType(year, product, DataType.DOUBLE, attribute);
        return attr.asValueAttribute().getBooleanValue();
    }

    public static void setBoolean(int year, Product product, Attribute attribute, boolean value) {
        Attribute attr = getAttributeWithType(year, product, DataType.DOUBLE, attribute);
        attr.asValueAttribute().setBooleanValue(value);
    }

    public boolean getBoolean(ConsumerAgent agent, Product product, String attributeName, boolean find) {
        return getBoolean(getCurrentYear(), product, getAttribute(agent, product, attributeName, find));
    }

    public boolean getBoolean(ConsumerAgent agent, String attributeName, boolean find) {
        return getBoolean(agent, null, attributeName, find);
    }

    public void setBoolean(ConsumerAgent agent, Product product, String attributeName, boolean value, boolean find) {
        setBoolean(getCurrentYear(), product, getAttribute(agent, product, attributeName, find), value);
    }

    public void setBoolean(ConsumerAgent agent, String attributeName, boolean value, boolean find) {
        setBoolean(agent, null, attributeName, value, find);
    }

//
//    //==================================================
//    //static
//    //==================================================
//
//    protected static int currentYear(SimulationEnvironment environment) {
//        return environment.getTimeModel().getCurrentYear();
//    }
//
//    protected static void handleMissingAttribute(Nameable caller, String attrName) throws IRPactNoSuchElementException {
//        throw new IRPactNoSuchElementException("missing attribute '{}' for '{}'", attrName, caller.getName());
//    }
//
//    //=========================
//    //util
//    //=========================
//
//    public static double getDoubleValue(SimulationEnvironment environment, Attribute attribute) {
//        return getDoubleValue(currentYear(environment), attribute);
//    }
//    public static double getDoubleValue(int currentYear, Attribute attribute) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear);
//            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                return attr.asValueAttribute().getDoubleValue();
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
//            return attribute.asValueAttribute().getDoubleValue();
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no double value", attribute.getName());
//    }
//
//    public static void setDoubleValue(SimulationEnvironment environment, Attribute attribute, double value) {
//        setDoubleValue(currentYear(environment), attribute, value);
//    }
//    public static void setDoubleValue(int currentYear, Attribute attribute, double value) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear);
//            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                attr.asValueAttribute().setDoubleValue(value);
//                return;
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
//            attribute.asValueAttribute().setDoubleValue(value);
//            return;
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no double value", attribute.getName());
//    }
//
//    public static String getStringValue(SimulationEnvironment environment, Attribute attribute) {
//        return getStringValue(currentYear(environment), attribute);
//    }
//    public static String getStringValue(int currentYear, Attribute attribute) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear);
//            if(attr.isValueAttributeWithDataType(DataType.STRING)) {
//                return attr.asValueAttribute().getStringValue();
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.STRING)) {
//            return attribute.asValueAttribute().getStringValue();
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no double value", attribute.getName());
//    }
//
//    //==================================================
//    //not static
//    //==================================================
//
//    //=========================
//    //util
//    //=========================
//
//    protected int currentYear() {
//        return currentYear(environment);
//    }
//
//    //==================================================
//    //agent
//    //==================================================
//
//    //=========================
//    //value
//    //=========================
//
//    public boolean getBooleanValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        ConsumerAgentAttribute attribute = agent.getAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getBooleanValue(attribute);
//    }
//    public boolean findBooleanValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        Attribute attribute = agent.findAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getBooleanValue(attribute);
//    }
//
//    public void findAndSetBooleanValue(ConsumerAgent agent, String name, boolean value) {
//        Attribute attribute = agent.findAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        setBooleanValue(attribute, value);
//    }
//
//    public int getIntValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        ConsumerAgentAttribute attribute = agent.getAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getIntValue(attribute);
//    }
//    public int findIntValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        Attribute attribute = agent.findAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getIntValue(attribute);
//    }
//
//    public long getLongValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        ConsumerAgentAttribute attribute = agent.getAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getLongValue(attribute);
//    }
//    public long findLongValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        Attribute attribute = agent.findAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getLongValue(attribute);
//    }
//    public double getDoubleValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        ConsumerAgentAttribute attribute = agent.getAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getDoubleValue(attribute);
//    }
//    public double getDoubleValue(ConsumerAgent agent, String name, int year) throws IRPactNoSuchElementException {
//        ConsumerAgentAttribute attribute = agent.getAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getDoubleValue(year, attribute);
//    }
//    public double findDoubleValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        Attribute attribute = agent.findAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getDoubleValue(attribute);
//    }
//
//    public static double findDoubleValue2(ConsumerAgent agent, String name) {
//        Attribute attribute = agent.findAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getDoubleValue(agent.getEnvironment(), attribute);
//    }
//
//    public void setDoubleValue(ConsumerAgent agent, String name, double value) throws IRPactNoSuchElementException {
//        ConsumerAgentAttribute attribute = agent.getAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        setDoubleValue(attribute, value);
//    }
//
//    public String findStringValue(ConsumerAgent agent, String name) throws IRPactNoSuchElementException {
//        Attribute attribute = agent.findAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getStringValue(agent.getEnvironment(), attribute);
//    }
//
//    //=========================
//    //product
//    //=========================
//
//    public double getDoubleValue(ConsumerAgent agent, Product product, String name) throws IRPactNoSuchElementException {
//        ConsumerAgentAttribute attribute = agent.getProductRelatedAttribute(name);
//        if(attribute == null) {
//            handleMissingAttribute(agent, name);
//        }
//        return getDoubleValue(attribute, product);
//    }
//
//    //=========================
//    //or product
//    //=========================
//
//    public double tryFindDoubleValue(ConsumerAgent agent, Product product, String name) throws IRPactNoSuchElementException {
//        Attribute attribute = agent.findAttribute(name);
//        if(attribute == null) {
//            return getDoubleValue(agent, product, name);
//        } else {
//            return getDoubleValue(attribute);
//        }
//    }
//
//    //==================================================
//    //attribute
//    //==================================================
//
//    //=========================
//    //value
//    //=========================
//
//    public boolean getBooleanValue(Attribute attribute) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
//            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                return attr.asValueAttribute().getBooleanValue();
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
//            return attribute.asValueAttribute().getBooleanValue();
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no boolean value", attribute.getName());
//    }
//
//    public void setBooleanValue(Attribute attribute, boolean value) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
//            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                attr.asValueAttribute().setBooleanValue(value);
//                return;
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
//            attribute.asValueAttribute().setBooleanValue(value);
//            return;
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no boolean value", attribute.getName());
//    }
//
//    public int getIntValue(Attribute attribute) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
//            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                return attr.asValueAttribute().getIntValue();
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
//            return attribute.asValueAttribute().getIntValue();
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no int value", attribute.getName());
//    }
//
//    public void setIntValue(Attribute attribute, int value) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
//            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                attr.asValueAttribute().setIntValue(value);
//                return;
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
//            attribute.asValueAttribute().setIntValue(value);
//            return;
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no int value", attribute.getName());
//    }
//
//    public long getLongValue(Attribute attribute) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
//            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                return attr.asValueAttribute().getLongValue();
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
//            return attribute.asValueAttribute().getLongValue();
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no long value", attribute.getName());
//    }
//
//    public void setLongValue(Attribute attribute, long value) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isAnnualAttribute()) {
//            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
//            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                attr.asValueAttribute().setLongValue(value);
//                return;
//            }
//        }
//        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
//            attribute.asValueAttribute().setLongValue(value);
//            return;
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' has no long value", attribute.getName());
//    }
//
//    public double getDoubleValue(Attribute attribute) {
//        return getDoubleValue(currentYear(), attribute);
//    }
//
//    public void setDoubleValue(Attribute attribute, double value) {
//        setDoubleValue(currentYear(), attribute, value);
//    }
//
//    //=========================
//    //product
//    //=========================
//
//    public double getDoubleValue(ConsumerAgentAttribute attribute, Product product) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isProductRelatedAttribute()) {
//            ConsumerAgentAttribute pAttr = attribute.asProductRelatedAttribute().getAttribute(product);
//            if(pAttr == null) {
//                throw new NullPointerException("attr is null");
//            }
//            else if(pAttr.isAnnualAttribute()) {
//                Attribute attr = pAttr.asAnnualAttribute().getAttribute(currentYear());
//                if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                    return attr.asValueAttribute().getDoubleValue();
//                }
//            }
//            else if(pAttr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                return pAttr.asValueAttribute().getDoubleValue();
//            }
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' ist not product related", attribute.getName());
//    }
//
//    public void setDoubleValue(ConsumerAgentAttribute attribute, Product product, double value) {
//        if(attribute == null) {
//            throw new NullPointerException("attribute is null");
//        }
//        else if(attribute.isProductRelatedAttribute()) {
//            ConsumerAgentAttribute pAttr = attribute.asProductRelatedAttribute().getAttribute(product);
//            if(pAttr == null) {
//                throw new NullPointerException("attr is null");
//            }
//            else if(pAttr.isAnnualAttribute()) {
//                Attribute attr = pAttr.asAnnualAttribute().getAttribute(currentYear());
//                if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                    attr.asValueAttribute().setDoubleValue(value);
//                    return;
//                }
//            }
//            else if(pAttr.isValueAttributeWithDataType(DataType.DOUBLE)) {
//                pAttr.asValueAttribute().setDoubleValue(value);
//                return;
//            }
//        }
//
//        throw new IRPactIllegalArgumentException("attribute '{}' ist not product related", attribute.getName());
//    }
}
