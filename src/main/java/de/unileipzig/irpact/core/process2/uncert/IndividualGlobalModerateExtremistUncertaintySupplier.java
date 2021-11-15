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
public class IndividualGlobalModerateExtremistUncertaintySupplier
        extends AbstractGlobalModerateExtremistUncertainty {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IndividualGlobalModerateExtremistUncertaintySupplier.class);


    public IndividualGlobalModerateExtremistUncertaintySupplier() {
        super();
    }

    public IndividualGlobalModerateExtremistUncertaintySupplier(Set<String> attributeNames, Map<String, DoubleRange> ranges) {
        super(attributeNames, ranges);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void update() {
        //not used
    }

    @Override
    public Uncertainty createFor(ConsumerAgent agent) {
        if(agent == null) {
            throw new NullPointerException("agent is null");
        }

        checkInitalized();

        IndividualUncertainty uncertainty = new IndividualUncertainty();
        uncertainty.setName(getName() + "@" + agent.getName());
        uncertainty.setSupplier(this);
        return uncertainty;
    }

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
