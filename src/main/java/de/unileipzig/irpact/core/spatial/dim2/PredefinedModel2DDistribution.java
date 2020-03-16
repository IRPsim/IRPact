package de.unileipzig.irpact.core.spatial.dim2;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.distribution.DistributionBase;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;

import java.util.List;
import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class PredefinedModel2DDistribution extends DistributionBase implements SpatialDistribution {

    public static final String NAME = PredefinedModel2DDistribution.class.getName();

    private PredefinedModel2D model;
    private Random rnd;
    private long seed;
    private boolean allowDuplicates;

    public PredefinedModel2DDistribution(String name, PredefinedModel2D model, Random rnd, boolean allowDuplicates) {
        this(name, model, rnd, NO_SEED, allowDuplicates);
    }

    public PredefinedModel2DDistribution(String name, PredefinedModel2D model, long seed, boolean allowDuplicates) {
        this(name, model, newRandom(seed), seed, allowDuplicates);
    }

    private PredefinedModel2DDistribution(
            String name,
            PredefinedModel2D model,
            Random rnd,
            long seed,
            boolean allowDuplicates) {
        super(name);
        this.model = Check.requireNonNull(model, "model");
        this.rnd = Check.requireNonNull(rnd, "rnd");
        this.seed = seed;
        this.allowDuplicates = allowDuplicates;
    }

    public long getSeed() {
        return seed;
    }

    public boolean isAllowDuplicates() {
        return allowDuplicates;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Point2D drawValue() {
        List<Point2D> pointList = model.getPointList();
        if(pointList.isEmpty()) {
            throw new IllegalStateException("empty list");
        }
        int nextIndex = rnd.nextInt(pointList.size());
        Point2D nextPoint = pointList.get(nextIndex);
        if(!allowDuplicates) {
            pointList.remove(nextIndex);
        }
        return nextPoint;
    }
}
