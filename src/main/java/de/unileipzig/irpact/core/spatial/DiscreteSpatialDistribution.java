package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.Rnd;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class DiscreteSpatialDistribution extends ResettableSpatialDistributionBase {

    protected Set<SpatialInformation> unused;
    protected Set<SpatialInformation> used;
    protected Rnd rnd;

    public DiscreteSpatialDistribution() {
        this(new LinkedHashSet<>(), new LinkedHashSet<>());
    }

    public DiscreteSpatialDistribution(Set<SpatialInformation> unused, Set<SpatialInformation> used) {
        this.unused = unused;
        this.used = used;
    }

    @Override
    public void reset() {
        numberOfCalls = 0;
        unused.clear();
        used.clear();
    }

    @Override
    public boolean isShareble(SpatialDistribution target) {
        return target instanceof DiscreteSpatialDistribution;
    }

    @Override
    public void addComplexDataTo(SpatialDistribution target) {
        if(target instanceof DiscreteSpatialDistribution) {
            DiscreteSpatialDistribution targetDist = (DiscreteSpatialDistribution) target;
            targetDist.getUnused().addAll(getUnused());
            targetDist.getUsed().addAll(getUsed());
        } else {
            throw new IllegalArgumentException("requires " + getClass().getName());
        }
    }

    @Override
    public void initalize() {
        numberOfCalls = 0;
        call();
    }

    public Set<SpatialInformation> getUnused() {
        return unused;
    }

    public Set<SpatialInformation> getUsed() {
        return used;
    }

    public void addAll(Collection<? extends SpatialInformation> information) {
        unused.addAll(information);
    }

    public void add(SpatialInformation information) {
        unused.add(information);
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public int countUnusedEntries() {
        return unused.size();
    }

    public int countUsedEntries() {
        return used.size();
    }

    @Override
    public SpatialInformation drawValue() {
        if(unused.isEmpty()) {
            throw new IllegalStateException("set is empty");
        }
        SpatialInformation info = CollectionUtil.removeRandom(unused, rnd.getRandom());
        used.add(info);
        numberOfCalls++;
        return info;
    }
}
