package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentAttributeUtil {

    public static double getRelatedDoubleValue(ConsumerAgent agent, Product product, String attrName) {
        ConsumerAgentProductRelatedAttribute prAttr = agent.getProductRelatedAttribute(attrName);
        if(prAttr == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "agent '{}' has no attribute '{}'", agent.getName(), attrName);
        }
        ConsumerAgentAttribute attr = prAttr.getAttribute(product);
        if(attr == null) {
            throw ExceptionUtil.create(NoSuchElementException::new, "agent '{}' has no attribute '{}' for product '{}'", agent.getName(), attrName, product.getName());
        }
        if(attr.isNoValueAttribute()) {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' of agent '{}' has no double value", attrName, agent.getName());
        }
        return attr.asValueAttribute().getDoubleValue();
    }
}
