package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.NameableBase;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class DiscreteSpatialDistribution extends NameableBase implements SpatialDistribution {

    protected Set<SpatialInformation> unused;
    protected Set<SpatialInformation> used;
    protected Random rnd;

    public DiscreteSpatialDistribution(Set<SpatialInformation> unused, Random rnd) {
        this(unused, new HashSet<>(), rnd);
    }

    public DiscreteSpatialDistribution(Set<SpatialInformation> unused, Set<SpatialInformation> used, Random rnd) {
        this.unused = unused;
        this.used = used;
        this.rnd = rnd;
    }

    public void setUnused(Set<SpatialInformation> unused) {
        this.unused = unused;
    }

    public Set<SpatialInformation> getUnused() {
        return unused;
    }

    public void setUsed(Set<SpatialInformation> used) {
        this.used = used;
    }

    public Set<SpatialInformation> getUsed() {
        return used;
    }

    public void setRandom(Random rnd) {
        this.rnd = rnd;
    }

    public Random getRandom() {
        return rnd;
    }

    @Override
    public SpatialInformation drawValue() {
        if(unused.isEmpty()) {
            throw new NoSuchElementException();
        }
        SpatialInformation info = CollectionUtil.removeRandom(unused, rnd);
        used.add(info);
        return info;
    }
}
