package de.unileipzig.irpact.jadex.agent.pos.goals;

import de.unileipzig.irpact.core.product.price.ProductPriceChangeEvent;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;

/**
 * @author Daniel Abitz
 */
@Goal
public class ProductPriceChangeGoal {

    @GoalParameter
    protected ProductPriceChangeEvent event;

    public ProductPriceChangeGoal(ProductPriceChangeEvent event) {
        this.event = event;
    }

    public ProductPriceChangeEvent getEvent() {
        return event;
    }
}
