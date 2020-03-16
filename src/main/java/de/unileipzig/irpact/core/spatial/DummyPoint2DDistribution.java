package de.unileipzig.irpact.core.spatial;

/**
 * @author Daniel Abitz
 */
public class DummyPoint2DDistribution implements SpatialDistribution {

    public static final String NAME = DummyPoint2DDistribution.class.getName();
    public static final DummyPoint2DDistribution INSTANCE = new DummyPoint2DDistribution();
    private static final Point2D POSITION = new Point2D(0, 0);

    public DummyPoint2DDistribution() {
    }

    @Override
    public Point2D drawValue() {
        return POSITION;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
