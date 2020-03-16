package de.unileipzig.irpact.jadex.agent.pos.goals;

import de.unileipzig.irpact.core.product.availability.ProductAvailabilityChangeEvent;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;

/**
 * @author Daniel Abitz
 */
@Goal
public class ProductAvailabilityChangeGoal {

    @GoalParameter
    protected ProductAvailabilityChangeEvent event;

    public ProductAvailabilityChangeGoal(ProductAvailabilityChangeEvent event) {
        this.event = event;
    }

    public ProductAvailabilityChangeEvent getEvent() {
        return event;
    }
}
