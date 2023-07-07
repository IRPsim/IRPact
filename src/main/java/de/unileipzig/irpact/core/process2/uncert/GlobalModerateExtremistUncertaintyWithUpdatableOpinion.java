package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.util.DoubleRange;
import de.unileipzig.irpact.commons.util.data.SortedList;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Daniel Abitz
 */
public class GlobalModerateExtremistUncertaintyWithUpdatableOpinion
        extends AbstractGlobalModerateExtremistUncertainty
        implements Uncertainty {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GlobalModerateExtremistUncertaintyWithUpdatableOpinion.class);

    protected Map<String, SortedList<Double>> values;
    protected Map<String, ReadWriteLock> locks;

    public GlobalModerateExtremistUncertaintyWithUpdatableOpinion() {
        this(new HashSet<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public GlobalModerateExtremistUncertaintyWithUpdatableOpinion(
            Set<String> attributeNames,
            Map<String, DoubleRange> ranges,
            Map<String, SortedList<Double>> values,
            Map<String, ReadWriteLock> locks) {
        super(attributeNames, ranges);
        this.values = values;
        this.locks = locks;
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
    protected void put(String attributeName) {
        if(isAllExtremist()) {
            trace("[{}] all extremist, skip range calculation");
            return;
        }

        if(isAllModerate()) {
            trace("[{}] all moderate, skip range calculation");
            return;
        }

        SortedList<Double> slist = new SortedList<>(Double::compareTo);
        collectValues(attributeName, slist::add);
        values.put(attributeName, slist);

        locks.put(attributeName, new ReentrantReadWriteLock());

        updateRange(attributeName, slist);
    }

    @Override
    public void updateOpinion(ConsumerAgentAttribute attribute, double oldValue, double newValue) {
        if(isAllModerate()) return;
        if(isAllExtremist()) return;

        String attributeName = attribute.getName();
        ReadWriteLock lock = locks.get(attributeName);
        if(lock == null) {
            throw new IllegalArgumentException("unknown attribute: " + attributeName);
        }

        SortedList<Double> slist = values.get(attributeName);
        lock.writeLock().lock();
        try {
            slist.remove(oldValue);
            slist.add(newValue);
            updateRange(attributeName, slist);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public GlobalModerateExtremistUncertaintyWithUpdatableOpinion createFor(ConsumerAgent agent) {
        if(agent == null) {
            throw new NullPointerException("agent is null");
        }
        checkInitalized();
        return this;
    }

    @Override
    public void setUncertainty(ConsumerAgentAttribute attribute, double value) {
        //not used
    }

    @Override
    public double getUncertainty(ConsumerAgentAttribute attribute) {
        if(isAllModerate()) return moderateUncertainty;
        if(isAllExtremist()) return extremistUncertainty;

        String attributeName = attribute.getName();
        ReadWriteLock lock = locks.get(attributeName);
        if(lock == null) {
            throw new IllegalArgumentException("unknown attribute: " + attributeName);
        }

        lock.readLock().lock();
        try {
            DoubleRange range = ranges.get(attributeName);
            return range.isInRange(attribute.asValueAttribute().getDoubleValue())
                    ? moderateUncertainty
                    : extremistUncertainty;
        } finally {
            lock.readLock().unlock();
        }
    }
}
