package de.unileipzig.irpact.core.spatial;

/**
 * @author Daniel Abitz
 */
public class SquareModel extends SpatialModelBase implements SpatialModel {

    public static final String NAME = SquareModel.class.getName();

    //links open
    private double x0;
    private double y0;
    //rechts unten
    private double x1;
    private double y1;

    public SquareModel(String name, double x0, double y0, double x1, double y1) {
        super(name);
        if(x1 < x0) throw new IllegalArgumentException("x1 < x0: " + x1 + " < " + x0);
        if(y1 < y0) throw new IllegalArgumentException("y1 < y0: " + y1 + " < " + y0);
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public double getX0() {
        return x0;
    }

    public double getY0() {
        return y0;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }
}
