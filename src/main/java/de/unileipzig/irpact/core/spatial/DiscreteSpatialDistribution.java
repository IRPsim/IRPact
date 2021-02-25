package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.Rnd;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class DiscreteSpatialDistribution extends NameableBase implements SpatialDistribution {

    protected Set<SpatialInformation> unused;
    protected Set<SpatialInformation> used;
    protected Rnd rnd;

    public DiscreteSpatialDistribution() {
        this(new HashSet<>(), new HashSet<>());
    }

    public DiscreteSpatialDistribution(Set<SpatialInformation> unused, Set<SpatialInformation> used) {
        this.unused = unused;
        this.used = used;
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

    @Override
    public SpatialInformation drawValue() {
        if(unused.isEmpty()) {
            throw new IllegalStateException("set is empty");
        }
        SpatialInformation info = CollectionUtil.removeRandom(unused, rnd.getRandom());
        used.add(info);
        return info;
    }
}
