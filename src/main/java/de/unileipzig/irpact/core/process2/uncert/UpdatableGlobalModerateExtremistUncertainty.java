package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.util.DoubleRange;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class UpdatableGlobalModerateExtremistUncertainty
        extends AbstractGlobalModerateExtremistUncertainty
        implements Uncertainty {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UpdatableGlobalModerateExtremistUncertainty.class);

    public UpdatableGlobalModerateExtremistUncertainty() {
        super();
    }

    public UpdatableGlobalModerateExtremistUncertainty(Set<String> attributeNames, Map<String, DoubleRange> ranges) {
        super(attributeNames, ranges);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void updateOpinion(ConsumerAgentAttribute attribute, double oldValue, double newValue) {
        //not used
    }

    @Override
    public void setUncertainty(ConsumerAgentAttribute attribute, double value) {
        //not used
    }

    @Override
    public UpdatableGlobalModerateExtremistUncertainty createFor(ConsumerAgent agent) {
        if(agent == null) {
            throw new NullPointerException("agent is null");
        }
        checkInitalized();
        return this;
    }

    @Override
    public double getUncertainty(ConsumerAgentAttribute attribute) {
        if(isAllModerate()) return moderateUncertainty;
        if(isAllExtremist()) return extremistUncertainty;

        DoubleRange range = ranges.get(attribute.getName());
        if(range == null) {
            throw new NoSuchElementException("missing range: " + attribute.getName());
        }
        return range.isInRange(attribute.asValueAttribute().getDoubleValue())
                ? moderateUncertainty
                : extremistUncertainty;
    }
}
