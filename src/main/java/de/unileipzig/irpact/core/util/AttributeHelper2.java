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
        return getDouble(getCurrentYear(), product, getAttribute(agent, attributeName, find));
    }

    public void setDouble(ConsumerAgent agent, Product product, String attributeName, double value, boolean find) {
        setDouble(getCurrentYear(), product, getAttribute(agent, attributeName, find), value);
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
        return getInt(getCurrentYear(), product, getAttribute(agent, attributeName, find));
    }

    public void setInt(ConsumerAgent agent, Product product, String attributeName, int value, boolean find) {
        setInt(getCurrentYear(), product, getAttribute(agent, attributeName, find), value);
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
        return getLong(getCurrentYear(), product, getAttribute(agent, attributeName, find));
    }

    public void setLong(ConsumerAgent agent, Product product, String attributeName, long value, boolean find) {
        setLong(getCurrentYear(), product, getAttribute(agent, attributeName, find), value);
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
        return getBoolean(getCurrentYear(), product, getAttribute(agent, attributeName, find));
    }

    public void setBoolean(ConsumerAgent agent, Product product, String attributeName, boolean value, boolean find) {
        setBoolean(getCurrentYear(), product, getAttribute(agent, attributeName, find), value);
    }
}
