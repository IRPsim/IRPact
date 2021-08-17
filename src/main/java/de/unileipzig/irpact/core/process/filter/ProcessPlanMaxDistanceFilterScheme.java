package de.unileipzig.irpact.core.process.filter;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public abstract class ProcessPlanMaxDistanceFilterScheme extends NameableBase implements ProcessPlanNodeFilterScheme {

    protected double maxDistance;
    protected boolean inclusive;

    public ProcessPlanMaxDistanceFilterScheme() {
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }
}
