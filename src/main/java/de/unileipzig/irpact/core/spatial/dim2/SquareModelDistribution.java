package de.unileipzig.irpact.core.spatial.dim2;

import de.unileipzig.irpact.v2.commons.Check;
import de.unileipzig.irpact.v2.commons.MathUtil;
import de.unileipzig.irpact.commons.distribution.DistributionBase;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class SquareModelDistribution extends DistributionBase implements SpatialDistribution {

    public static final String NAME = SquareModelDistribution.class.getName();

    private SquareModel model;
    private Random rnd;
    private long seed;

    public SquareModelDistribution(String name, SquareModel model, Random rnd) {
        super(name);
        this.model = Check.requireNonNull(model, "model");
        this.rnd = Check.requireNonNull(rnd, "rnd");
        seed = NO_SEED;
    }

    public SquareModelDistribution(String name, SquareModel model, long seed) {
        super(name);
        this.model = Check.requireNonNull(model, "model");
        this.rnd = newRandom(seed);
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    Random getRandom() {
        return rnd;
    }

    SquareModel getModel() {
        return model;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Point2D drawValue() {
        double x = MathUtil.nextDouble(rnd, model.getX0(), model.getX1());
        double y = MathUtil.nextDouble(rnd, model.getY0(), model.getY1());
        return new Point2D(x, y);
    }
}
