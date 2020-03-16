package de.unileipzig.irpact.core.spatial;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class PredefinedModel2D extends SpatialModelBase implements SpatialModel {

    private List<Point2D> pointList;

    public PredefinedModel2D(String name, List<Point2D> pointList) {
        super(name);
        this.pointList = pointList;
    }

    public List<Point2D> getPointList() {
        return pointList;
    }
}
