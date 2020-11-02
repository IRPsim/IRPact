package de.unileipzig.irpact.v2.jadex.util;

import de.unileipzig.irpact.v2.commons.attribute.DoubleAttribute;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.v2.core.misc.StandardNames;
import de.unileipzig.irpact.v2.core.product.Product;

/**
 * @author Daniel Abitz
 */
public final class AgentUtil {

    private AgentUtil() {
    }

    public static boolean isAdopter(ConsumerAgent agent, Product product) {
        return agent.getAdoptedProducts().contains(product);
    }

    public static boolean isInterested(ConsumerAgent agent, Product product) {
        return agent.getProductAwareness().isInterested(product);
    }

    public static boolean isAwareOf(ConsumerAgent agent, Product product) {
        return agent.getProductAwareness().isAwareOf(product);
    }

    public static boolean isHouseOwner(ConsumerAgent agent) {
        return isTrue(agent.getAttribute(StandardNames.HOUSE_SHARE))
                && isTrue(agent.getAttribute(StandardNames.OWNERSHIP_STATUS));
    }

    public static boolean isConstructing(ConsumerAgent agent) {
        return isTrue(agent.getAttribute(StandardNames.CONSTRUCTION_RATE));
    }

    public static boolean isTrue(DoubleAttribute attribute) {
        return attribute.getValue() == 1.0;
    }

    public static boolean isFalse(DoubleAttribute attribute) {
        return attribute.getValue() != 1.0;
    }
}
