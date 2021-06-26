package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;

/**
 * @author Daniel Abitz
 */
public class AttributeHelper extends SimulationEntityBase {

    public AttributeHelper() {
    }

    protected int currentYear() {
        return environment.getTimeModel().getCurrentYear();
    }

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

    public double getDoubleValue(Attribute attribute) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
            if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                return attr.asValueAttribute().getDoubleValue();
            }
        }
        else if(attribute.isValueAttributeWithDataType(DataType.DOUBLE)) {
            return attribute.asValueAttribute().getDoubleValue();
        }

        throw new IRPactIllegalArgumentException("attribute '{}' has no double value", attribute.getName());
    }

    public void setDoubleValue(Attribute attribute, double value) {
        if(attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        else if(attribute.isAnnualAttribute()) {
            Attribute attr = attribute.asAnnualAttribute().getAttribute(currentYear());
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
