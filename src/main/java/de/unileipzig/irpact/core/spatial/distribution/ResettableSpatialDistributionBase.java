package de.unileipzig.irpact.core.spatial.distribution;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class ResettableSpatialDistributionBase extends NameableBase implements SpatialDistribution {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ResettableSpatialDistributionBase.class);

    protected int requiredNumberOfCalls = 0;
    protected int numberOfCalls = 0;

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    public abstract void reset();

    public abstract void initalize();

    @Override
    public abstract boolean isShareble(SpatialDistribution target);

    @Override
    public abstract void addComplexDataTo(SpatialDistribution target);

    public void call() {
        call(requiredNumberOfCalls);
        requiredNumberOfCalls = 0;
    }

    public void call(int times) {
        LOGGER.debug("call {} times", times);
        for(int i = 0; i < times; i++) {
            drawValue();
        }
    }
}
