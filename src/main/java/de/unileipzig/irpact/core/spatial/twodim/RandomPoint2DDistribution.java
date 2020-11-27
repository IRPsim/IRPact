package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.Util;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class RandomPoint2DDistribution extends NameableBase implements SpatialDistribution {

    protected double lowerX;
    protected double upperX;
    protected double lowerY;
    protected double upperY;
    protected Set<Point2D> used;
    protected long seed;
    protected Random rnd;

    public RandomPoint2DDistribution(double lowerX, double upperX, double lowerY, double upperY, long seed) {
        this(lowerX, upperX, lowerY, upperY, new HashSet<>(), seed);
    }

    public RandomPoint2DDistribution(double lowerX, double upperX, double lowerY, double upperY, Set<Point2D> used, long seed) {
        this.lowerX = lowerX;
        this.upperX = upperX;
        this.lowerY = lowerY;
        this.upperY = upperY;
        this.used = used;
        this.seed = seed;
        rnd = new Random(seed);
    }

    public void setLowerX(double lowerX) {
        this.lowerX = lowerX;
    }

    public double getLowerX() {
        return lowerX;
    }

    public void setUpperX(double upperX) {
        this.upperX = upperX;
    }

    public double getUpperX() {
        return upperX;
    }

    public void setLowerY(double lowerY) {
        this.lowerY = lowerY;
    }

    public double getLowerY() {
        return lowerY;
    }

    public void setUpperY(double upperY) {
        this.upperY = upperY;
    }

    public double getUpperY() {
        return upperY;
    }

    public void setUsed(Set<Point2D> used) {
        this.used = used;
    }

    public Set<Point2D> getUsed() {
        return used;
    }

    public void setRandom(Random rnd) {
        this.rnd = rnd;
    }

    public Random getRandom() {
        return rnd;
    }

    @Override
    public Point2D drawValue() {
        Point2D next = null;
        while(next == null) {
            double x = Util.nextDouble(rnd, lowerX, upperX);
            double y = Util.nextDouble(rnd, lowerY, upperY);
            Point2D temp = new BasicPoint2D(x, y);
            if(used.add(temp)) {
                next = temp;
            }
        }
        return next;
    }
}
