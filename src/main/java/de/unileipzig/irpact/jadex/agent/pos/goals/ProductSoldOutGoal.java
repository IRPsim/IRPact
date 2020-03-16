package de.unileipzig.irpact.jadex.agent.pos.goals;

import de.unileipzig.irpact.core.product.availability.ProductSoldOutEvent;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;

/**
 * @author Daniel Abitz
 */
@Goal
public class ProductSoldOutGoal {

    @GoalParameter
    protected ProductSoldOutEvent event;

    public ProductSoldOutGoal(ProductSoldOutEvent event) {
        this.event = event;
    }

    public ProductSoldOutEvent getEvent() {
        return event;
    }
}
